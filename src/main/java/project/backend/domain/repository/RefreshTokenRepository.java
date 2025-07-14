package project.backend.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.backend.domain.model.refresh.Refresh;
import project.backend.infrastructure.persistence.repository.RefreshCustomRepository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<Refresh, Long> {

}