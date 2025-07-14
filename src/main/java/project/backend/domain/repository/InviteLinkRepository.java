package project.backend.domain.repository;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.backend.domain.model.invitelink.InviteLink;
import project.backend.domain.model.invitelink.InviteLinkStatus;
import project.backend.domain.model.ticket.Ticket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InviteLinkRepository extends JpaRepository<InviteLink, Long> {
    @Modifying(clearAutomatically = true) // clearAutomatically 옵션 추가를 강력히 권장합니다.
    @Query("update InviteLink il set il.inviteLinkStatus = :status, il.usedAt = :usedTime where il.id = :inviteLinkId")
    void updateInviteLinkStatus(@Param("inviteLinkId") Long inviteLinkId,
                                @Param("status") InviteLinkStatus status,
                                @Param("usedTime") LocalDateTime usedTime);

    @Query("select il from InviteLink il where il.ticket.id = :ticketId")
    Optional<InviteLink> findByTicket_Id(@Param("ticketId") Long ticketId);

    @Query("select il from InviteLink il where il.uniqueCode = :uniqueCode")
    Optional<InviteLink> findByUniqueCode(@Param("uniqueCode") @NotBlank String uniqueCode);

    List<InviteLink> findAllByTicketIn(List<Ticket> tickets);
}
