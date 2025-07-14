package project.backend.infrastructure.persistence.repository;


import project.backend.domain.model.refresh.Refresh;

import java.time.LocalDateTime;

public interface RefreshCustomRepository {
    Refresh validatedRefreshToken(String accessTokenValue, String refreshTokenValue, LocalDateTime currentDate);
}
