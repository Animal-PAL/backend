package project.backend.common.jwtToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.backend.common.exception.AccountException;
import project.backend.common.exception.errorCode.UserErrorCode;
import project.backend.domain.model.refresh.Refresh;
import project.backend.domain.model.user.User;
import project.backend.domain.repository.RefreshTokenRepository;
import project.backend.domain.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpirationTime;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    // 엑세스토큰 생성
    public String createAccessToken(Long userId) {
        Claims claims = Jwts.claims();
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken(Long userId, String accessToken) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AccountException(UserErrorCode.INVAILD_USER));

        String refresh = Jwts.builder()
                .setClaims(Jwts.claims())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        Refresh refreshBuild = Refresh.builder()
                .user(user)
                .refreshToken(refresh)
                .accessToken(accessToken)
                .expireDate(LocalDateTime.now().plusDays(14))
                .build();

        refreshTokenRepository.save(refreshBuild);
        return refresh;
    }


    public TokenInfo getUserId(String tokenValue) {
        try {
            Long userId = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(tokenValue)
                    .getBody().get("userId", Long.class);
            return TokenInfo.builder()
                    .userId(userId)
                    .build();
        } catch (ExpiredJwtException e) {
            Long userId = e.getClaims().get("userId", Long.class);
            return TokenInfo.builder()
                    .userId(userId)
                    .build();
        } catch (Exception e) {
            log.error("Invalid token: {}", e.getMessage());
            throw new RuntimeException("Invalid token");
        }
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof User) {
                return ((User) principal).getId();
            } else if (principal instanceof Long) {
                return (Long) principal;
            }
        }
        throw new AccountException(UserErrorCode.OAUTH2_NOT_FOUND);
    }

    public void issueTokensAndSetCookies(HttpServletResponse response, Long userId) {
        String accessToken = createAccessToken(userId);
        String refreshToken = createRefreshToken(userId, accessToken);

        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        // accessTokenCookie.setSecure(true); // HTTPS 환경에서만 활성화

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        // refreshTokenCookie.setSecure(true); // HTTPS 환경에서만 활성화

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }
}
