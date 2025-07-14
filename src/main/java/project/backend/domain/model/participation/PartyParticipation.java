package project.backend.domain.model.participation;

import jakarta.persistence.*;
import lombok.*;
import project.backend.domain.model.common.AuditBaseEntity;
import project.backend.domain.model.party.Party;
import project.backend.domain.model.ticket.Ticket;
import project.backend.domain.model.user.User;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PartyParticipation extends AuditBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    @Enumerated(EnumType.STRING)
    private ParticipationOwnerStatus ownerStatus;

    @Enumerated(EnumType.STRING)
    private ParticipationStatus partyStatus;

    public void cancel() {
        this.partyStatus = ParticipationStatus.CANCELLED;
    }
}
