package project.backend.application.service.party;

import jakarta.transaction.Transactional; // Spring의 @Transactional을 사용하는 것을 권장합니다.
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.backend.application.dto.party.CreatePartyDto;
import project.backend.application.dto.party.GetPartyResponse;
import project.backend.application.dto.party.UpdatePartyRequest;
import project.backend.common.exception.AccountException;
import project.backend.common.exception.errorCode.UserErrorCode;
import project.backend.common.jwtToken.JwtService;
import project.backend.domain.model.invitelink.InviteLink;
import project.backend.domain.model.invitelink.InviteLinkStatus;
import project.backend.domain.model.participation.ParticipationOwnerStatus;
import project.backend.domain.model.participation.ParticipationStatus;
import project.backend.domain.model.participation.PartyParticipation;
import project.backend.domain.model.party.Party;
import project.backend.domain.model.party.PartyStatus;
import project.backend.domain.model.ticket.Ticket;
import project.backend.domain.model.ticket.TicketStatus;
import project.backend.domain.model.user.User;
import project.backend.domain.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartyServiceImpl implements PartyService {

    private final PartyRepository partyRepository;
    private final TicketRepository ticketRepository;
    private final InviteLinkRepository inviteLinkRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ParticipationRepoisotry participationRepoisotry;

    // 파티 생성 시 기본으로 제공되는 티켓 수 (상수로 관리)
    private static final int INITIAL_TICKET_COUNT = 2;

    @Override
    @Transactional
    public void createParty(CreatePartyDto createPartyDto) {
        Long currentUserId = jwtService.getCurrentUserId();
        User user = findUserById(currentUserId);

        Party party = Party.builder()
                .host_user_id(user)
                .partyName(createPartyDto.partyname())
                .description(createPartyDto.description())
                .endTime(createPartyDto.endTime())
                .maxParticipants(createPartyDto.maxParticipants())
                .partyStatus(PartyStatus.OPEN)
                .build();
        partyRepository.save(party);

        // 티켓 및 초대 링크 생성 로직을 별도 메소드로 분리하여 가독성 향상
        createInitialTicketsAndInviteLinks(user, party);

        // 파티 참여 정보 생성
        PartyParticipation participation = PartyParticipation.builder()
                .party(party)
                .user(user)
                .ownerStatus(ParticipationOwnerStatus.HOST)
                .partyStatus(ParticipationStatus.CONFIRMED)
                .build();
        participationRepoisotry.save(participation);
    }

    private void createInitialTicketsAndInviteLinks(User user, Party party) {
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < INITIAL_TICKET_COUNT; i++) {
            tickets.add(Ticket.builder()
                    .ownerUser(user)
                    .party(party)
                    .ticketStatus(TicketStatus.AVAILABLE)
                    .build());
        }
        List<Ticket> savedTickets = ticketRepository.saveAll(tickets);

        List<InviteLink> inviteLinks = savedTickets.stream()
                .map(savedTicket -> InviteLink.builder()
                        .ticket(savedTicket)
                        .uniqueCode(generateUniqueCode())
                        .inviteLinkStatus(InviteLinkStatus.AVAILABLE)
                        .build())
                .collect(Collectors.toList());

        inviteLinkRepository.saveAll(inviteLinks);
    }

    private String generateUniqueCode() {
        return UUID.randomUUID().toString();
    }

    @Override
    public GetPartyResponse getPartyById(Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new AccountException(UserErrorCode.PARTY_NOT_FOUND));

        return GetPartyResponse.from(party); // DTO 변환 로직을 DTO 내부 정적 메소드로 위임
    }

    @Override
    public List<GetPartyResponse> getAllParties(Long userId) {
        List<PartyParticipation> participations = participationRepoisotry.findAllByUserId(userId);

        // 참여한 파티가 없는 경우, 예외 대신 빈 리스트를 반환하는 것이 REST API 설계상 더 일반적입니다.
        return participations.stream()
                .map(PartyParticipation::getParty)
                .map(GetPartyResponse::from) // 정적 팩토리 메소드 사용
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateParty(UpdatePartyRequest updatePartyDto) {
        Long currentUserId = jwtService.getCurrentUserId();
        User currentUser = findUserById(currentUserId);

        Party party = partyRepository.findById(updatePartyDto.partyId())
                .orElseThrow(() -> new AccountException(UserErrorCode.PARTY_NOT_FOUND));

        // 중요: 파티를 수정하려는 사용자가 실제 파티의 호스트인지 확인 (인가)
        if (!party.getHost_user_id().equals(currentUser)) {
            throw new AccountException(UserErrorCode.NOT_PARTY_HOST); // 권한 없음 예외
        }

        // 엔티티의 상태를 변경하는 메소드를 호출 (Dirty Checking으로 업데이트)
        party.updateDetails(
                updatePartyDto.partyName(),
                updatePartyDto.description(),
                updatePartyDto.endTime(),
                updatePartyDto.maxParticipants()
        );
    }

    @Override
    @Transactional
    public void cancelParty(Long partyId) {
        Long currentUserId = jwtService.getCurrentUserId();
        User currentUser = findUserById(currentUserId);

        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new AccountException(UserErrorCode.PARTY_NOT_FOUND));

        // Code Quality: host_user_id -> hostUser 필드명 변경을 권장합니다.
        // 파티를 취소하려는 사용자가 실제 파티의 호스트인지 확인 (인가)
        if (!party.getHost_user_id().equals(currentUser)) {
            throw new AccountException(UserErrorCode.NOT_PARTY_HOST);
        }

        // 1. 이 파티에 대한 모든 참여 기록을 'CANCELLED'로 변경
        List<PartyParticipation> participations = participationRepoisotry.findAllByPartyId(partyId);
        participations.forEach(PartyParticipation::cancel);

        // 2. 이 파티에 속한 모든 티켓을 조회
        List<Ticket> tickets = ticketRepository.findAllByPartyId(partyId);

        if (!tickets.isEmpty()) {
            // 3. 티켓에 연결된 모든 초대 링크를 'CANCELLED'로 변경
            List<InviteLink> inviteLinks = inviteLinkRepository.findAllByTicketIn(tickets);
            inviteLinks.forEach(InviteLink::cancel);

            // 4. 모든 티켓을 'CANCELLED'로 변경
            tickets.forEach(Ticket::cancel);
        }

        // 5. 마지막으로 파티의 상태를 'CANCELLED'로 변경
        party.updatePartyStatus(PartyStatus.CANCELLED);

    }



    // 중복 코드를 줄이기 위한 private helper method
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AccountException(UserErrorCode.INVAILD_USER));
    }
}