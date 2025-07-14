package project.backend.domain.model.party;

import jakarta.persistence.*;
import lombok.*;
import project.backend.domain.model.common.AuditBaseEntity;
import project.backend.domain.model.user.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "parties")
public class Party  extends AuditBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_user_id", nullable = false)
    private User host_user_id;

    @Column(nullable = false, length = 255)
    private String partyName;

    private String description;

    private Integer maxParticipants; // 최대 참여 인원 수

    @Enumerated(EnumType.STRING)
    private PartyStatus partyStatus;

    private LocalDateTime endTime;

    public void updateDetails(String partyName, String description, LocalDateTime endTime, Integer maxParticipants) {
        this.partyName = partyName;
        this.description = description;
        this.endTime = endTime;
        this.maxParticipants = maxParticipants;
    }

    public void updatePartyStatus(PartyStatus partyStatus) {
        this.partyStatus = partyStatus;
    }
}
