package project.backend.application.service.refresh;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.backend.domain.repository.RefreshTokenRepository;
import project.backend.common.jwtToken.JwtService;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Override
    public void reGenerateToken(String accessToken, String refreshToken, HttpServletResponse response) {
        log.info("리프레시 토큰 재발급 시작");

//        Refresh refresh = refreshTokenRepository.validatedRefreshToken(accessToken, refreshToken, LocalDateTime.now());
//        if (refresh == null) {
//            throw new AccountException(UserErrorCode.REFRESH_TOKEN_NOT_FOUND);
//        }
//
//        String accessToken1 = jwtService.createAccessToken(refresh.getUser().getId());
//        String refreshToken1 = jwtService.createRefreshToken(refresh.getUser().getId(), false, accessToken);

//        saveCookie(accessToken1, response, "accessToken");
//        saveCookie(refreshToken1, response, "refreshToken");
    }

    // 쿠키 저장
    public void saveCookie(String token, HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, token);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true); // HTTPS를 사용하는 경우에만 true로 설정
        cookie.setPath("/");

        response.addCookie(cookie);
    }
}
