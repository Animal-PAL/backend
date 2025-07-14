package project.backend.domain.model.ticket;

import jakarta.persistence.*;
import lombok.*;
import project.backend.domain.model.common.AuditBaseEntity;
import project.backend.domain.model.party.Party;
import project.backend.domain.model.user.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tickets")
public class Ticket extends AuditBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id", nullable = false)
    private User ownerUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_by_ticket_id")
    private Ticket invitedByTicket; // 이 티켓을 받게 된 상위 티켓 (초대 체인)

    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus; // 티켓 상태 (예: AVAILABLE, USED, EXPIRED)

    private LocalDateTime usedAt; // 업데이트 시간

    public void use() {
        if (this.ticketStatus != TicketStatus.AVAILABLE) {
        }
        this.ticketStatus = TicketStatus.USED;
        this.usedAt = LocalDateTime.now();
    }

    public void cancel() {
        this.ticketStatus = TicketStatus.CANCELLED;
    }
}
