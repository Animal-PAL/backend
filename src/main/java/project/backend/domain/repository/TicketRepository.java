package project.backend.domain.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.backend.domain.model.ticket.Ticket;
import project.backend.domain.model.ticket.TicketStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t WHERE t.party.id = :partyId AND t.ownerUser.id = :ownerUserId AND t.ticketStatus = :ticketStatus")
    Optional<Ticket> findFirstByParty_IdAndOwnerUser_IdAndTicketStatus(Long partyId, Long ownerUserId, TicketStatus ticketStatus);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Ticket t WHERE t.id = :id")
    Optional<Ticket> findByIdForUpdate(@Param("id") Long id);

    /**
     * 특정 소유자와 초대된 티켓의 ID로 티켓이 존재하는지 확인합니다.
     * Spring Data JPA가 메소드 이름을 기반으로 `SELECT count(...)` 쿼리를 자동으로 생성합니다.
     */
    // @Query 어노테이션 제거
    boolean existsByOwnerUser_IdAndInvitedByTicket_Id(Long ownerUserId, Long invitedByTicketId);

    List<Ticket> findAllByPartyId(Long partyId);

}