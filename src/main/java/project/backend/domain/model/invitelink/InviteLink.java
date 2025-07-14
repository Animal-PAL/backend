package project.backend.domain.model.invitelink;

import jakarta.persistence.*;
import lombok.*;
import project.backend.domain.model.common.AuditBaseEntity;
import project.backend.domain.model.ticket.Ticket;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "invite_links")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class InviteLink extends AuditBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", unique = true, nullable = false)
    private Ticket ticket;

    private String uniqueCode;

    @Enumerated(EnumType.STRING)
    private InviteLinkStatus inviteLinkStatus; // 0: 사용가능, 1: 사용됨, 2: 만료됨

    private LocalDateTime expirationDate; // 만료일자

    private LocalDateTime usedAt; // 사용된 시간

    /**
     * 초대 링크를 '사용됨' 상태로 변경하는 비즈니스 메소드입니다.
     */
    public void useLink() {
        this.inviteLinkStatus = InviteLinkStatus.USED;
        this.usedAt = LocalDateTime.now();
    }

    public void cancel() {
        this.inviteLinkStatus = InviteLinkStatus.CANCELLED;
    }
}
