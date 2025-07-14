package project.backend.application.service.refresh;

import jakarta.servlet.http.HttpServletResponse;

public interface RefreshTokenService {
    void reGenerateToken(String accessToken, String refreshToken, HttpServletResponse response);
}
