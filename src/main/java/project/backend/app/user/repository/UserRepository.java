package project.backend.app.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.backend.app.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.isSocial != 1")
    Optional<User> findByEmailAndIsSocialNot(String email);

    boolean existsByEmail(String email);
    boolean existsByPassword(String password);
}
