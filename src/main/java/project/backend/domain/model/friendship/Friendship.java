package project.backend.domain.model.friendship;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.backend.domain.model.common.AuditBaseEntity;
import project.backend.domain.model.user.User;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Friendship extends AuditBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_user_id", nullable = false)
    private User requesterUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accepter_user_id", nullable = false)
    private User accepterUser; // 수락한 사용자

    private FreindshipStatus friendshipStatus; // 0: 대기중, 1: 수락됨, 2: 거절됨, 3: 차단됨
}
