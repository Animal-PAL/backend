package project.backend.application.service.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.application.dto.ticket.CreateInviteLinkRequest;
import project.backend.application.dto.ticket.CreateInviteLinkResponse;
import project.backend.application.dto.ticket.ReceiveTicketRequest;
import project.backend.common.exception.AccountException;
import project.backend.common.exception.errorCode.UserErrorCode;
import project.backend.common.jwtToken.JwtService;
import project.backend.domain.model.invitelink.InviteLink;
import project.backend.domain.model.invitelink.InviteLinkStatus;
import project.backend.domain.model.participation.ParticipationOwnerStatus;
import project.backend.domain.model.participation.ParticipationStatus;
import project.backend.domain.model.participation.PartyParticipation;
import project.backend.domain.model.ticket.Ticket;
import project.backend.domain.model.ticket.TicketStatus;
import project.backend.domain.model.user.User;
import project.backend.domain.repository.InviteLinkRepository;
import project.backend.domain.repository.ParticipationRepoisotry;
import project.backend.domain.repository.TicketRepository;
import project.backend.domain.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final InviteLinkRepository inviteLinkRepository;
    private final UserRepository userRepository;
    private final ParticipationRepoisotry participationRepoisotry;
    private final JwtService jwtService;

    @Override
    @Transactional
    public CreateInviteLinkResponse createInviteLink(CreateInviteLinkRequest request) {
        Long currentUserId = jwtService.getCurrentUserId();

        // 초대할 수 있는 '사용 가능한' 티켓이 있는지 확인합니다.
        Ticket ticketToUseForInvite = ticketRepository
                .findFirstByParty_IdAndOwnerUser_IdAndTicketStatus(request.partyId(), currentUserId, TicketStatus.AVAILABLE)
                .orElseThrow(() -> new AccountException(UserErrorCode.NO_AVAILABLE_TICKET_FOR_INVITE));

        // 초대 링크를 생성합니다. (기존 티켓을 사용)
        InviteLink inviteLink = inviteLinkRepository.findByTicket_Id(ticketToUseForInvite.getId())
                .orElseGet(() -> createAndSaveNewInviteLink(ticketToUseForInvite));

        return CreateInviteLinkResponse.builder()
                .inviteUrl(buildInviteUrl(inviteLink.getUniqueCode()))
                .build();
    }

    private InviteLink createAndSaveNewInviteLink(Ticket ticket) {
        InviteLink newLink = InviteLink.builder()
                .ticket(ticket)
                .uniqueCode(UUID.randomUUID().toString())
                .inviteLinkStatus(InviteLinkStatus.AVAILABLE)
                .build();
        return inviteLinkRepository.save(newLink);
    }

    /**
     * 초대 링크를 사용하여 새로운 사용자가 파티에 참여하고 티켓을 발급받습니다.
     */
    @Override
    @Transactional
    public void receiveTicket(ReceiveTicketRequest request) {
        Long newUserId = jwtService.getCurrentUserId();
        User newUser = findUserById(newUserId);

        // 초대 코드로 링크를 찾습니다.
        InviteLink inviteLink = inviteLinkRepository.findByUniqueCode(request.inviteCode())
                .orElseThrow(() -> new AccountException(UserErrorCode.TICKET_ALREADY_RECEIVED));

        // 부모 티켓에 비관적 락을 걸어 동시 접근을 막습니다.
        Ticket parentTicket = ticketRepository.findByIdForUpdate(inviteLink.getTicket().getId())
                .orElseThrow(() -> new AccountException(UserErrorCode.TICKET_NOT_FOUND));

        // 다양한 유효성 검사를 수행합니다.
        validateInvitation(inviteLink, parentTicket, newUser);

        // 상태를 변경합니다. (JPA Dirty Checking 활용)
        parentTicket.use();
        inviteLink.useLink();

        // 새로운 사용자를 위한 자식 티켓을 생성합니다.
        Ticket childTicket = Ticket.builder()
                .ownerUser(newUser)
                .party(parentTicket.getParty())
                .invitedByTicket(parentTicket)
                .ticketStatus(TicketStatus.AVAILABLE)
                .build();
        ticketRepository.save(childTicket);

        // 새로운 사용자의 파티 참여 정보를 생성합니다.
        PartyParticipation participation = PartyParticipation.builder()
                .party(parentTicket.getParty())
                .user(newUser)
                .ownerStatus(ParticipationOwnerStatus.MEMBER)
                .partyStatus(ParticipationStatus.CONFIRMED)
                .build();
        participationRepoisotry.save(participation);
    }

    private void validateInvitation(InviteLink inviteLink, Ticket parentTicket, User newUser) {

        // 이미 초대에 사용된 링크인지 확인
        if (inviteLink.getInviteLinkStatus() != InviteLinkStatus.AVAILABLE) {
            throw new AccountException(UserErrorCode.TICKET_ALREADY_RECEIVED);
        }

        // 초대 링크가 만료되었는지 확인
        if (parentTicket.getTicketStatus() != TicketStatus.AVAILABLE) {
            throw new AccountException(UserErrorCode.PARENT_TICKET_NOT_AVAILABLE);
        }

        // 이미 파티에 참여한 사용자인지 확인
        if (participationRepoisotry.existsByParty_IdAndUser_Id(parentTicket.getParty().getId(), newUser.getId())) {
            throw new AccountException(UserErrorCode.ALREADY_PARTICIPATING_IN_PARTY);
        }
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AccountException(UserErrorCode.INVAILD_USER));
    }

    private String buildInviteUrl(String uniqueCode) {
        // 프론트엔드와 협의된 경로를 사용합니다.
        return "/tickets/receive?inviteCode=" + uniqueCode;
    }
}