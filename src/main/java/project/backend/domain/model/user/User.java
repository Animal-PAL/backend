package project.backend.domain.model.user;

import jakarta.persistence.*;
import lombok.*;
import project.backend.domain.model.common.AuditBaseEntity;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class User extends AuditBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String userName;

    private String email;

    private UserRole role;

    private String password;

    private Long isSocial; // 1: 일반, 2: 소셜
}