package project.backend.common.jwtToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import project.backend.domain.model.user.User;
import project.backend.domain.model.user.UserRole;
import project.backend.domain.repository.UserRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Optional<String> accessToken = findAccessToken(request, "accessToken");

        if (accessToken.isEmpty()) {
            log.info("accessToken is empty");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("tokenValue: {}", accessToken.get());

        try {
            TokenInfo tokenInfo = jwtService.getUserId(accessToken.get());

            if (!authenticationUser(request, response, tokenInfo.getUserId())) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized");
                return;
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token");
            return;
        }


        if (accessToken.isPresent()) {
            // 인증 시도
            TokenInfo tokenInfo = jwtService.getUserId(accessToken.get());
            authenticationUser(request, response, tokenInfo.getUserId());
            log.info("인증 성공, 필터 체인 진행");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("컨트롤러 진입 - 인증 객체: {}", auth);
        log.info("isAuthenticated(): {}", auth.isAuthenticated());


        log.info("유저 인증 완료, userId: {}", jwtService.getCurrentUserId());
        filterChain.doFilter(request, response);
    }

    private boolean authenticationUser(HttpServletRequest request, HttpServletResponse response, Long userId) {
        Optional<User> byId = userRepository.findById(userId);

        if (byId.isEmpty()) {
            return false;
        }
        log.info("유저 인증 시작, userId: {}", userId);

        Authentication authentication = createAuthentication(userId);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("SecurityContextHolder에 Authentication 정보");
        return true;
    }

    private Authentication createAuthentication(Long userId) {
        return new UsernamePasswordAuthenticationToken(userId, null, null);
    }

    private Optional<String> findAccessToken(HttpServletRequest request, String accessToken) {
        /**
         *  name String
         *  value String
         *  map Map<String, String>
         *  getCookie() returns array of cookies, or null
         */
        Cookie[] cookies = request.getCookies();

        // 왜 optional.empty()를 사용했는지?
        return cookies == null ? Optional.empty() : Arrays.stream(cookies)
                .filter(cookie -> accessToken.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue);
    }
}
