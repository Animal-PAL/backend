package project.backend.presentation.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.backend.application.common.ApiResponse;
import project.backend.application.service.refresh.RefreshTokenService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class RefreshController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<Void>> refreshToken(@CookieValue(value = "accessToken") String accessToken,
                                                          @CookieValue(value = "refreshToken") String refreshToken,
                                                          HttpServletResponse response) {
        log.info("리프레시 토큰 재발급 시작");
        refreshTokenService.reGenerateToken(accessToken, refreshToken, response);

        return ResponseEntity.ok(ApiResponse.createSuccessNoContent("토큰 재발급 완료"));
    }
}
