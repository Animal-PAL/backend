package project.backend.infrastructure.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import project.backend.domain.model.user.User;
import project.backend.domain.repository.UserRepository;
import project.backend.common.jwtToken.JwtService;
import project.backend.common.exception.AccountException;
import project.backend.common.exception.errorCode.UserErrorCode;
import project.backend.infrastructure.oauth2.dto.OAuth2Response;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final OAuthResponseFactory oAuthResponseFactory;


    /**
     * Called when a user has been successfully authenticated.
     *
     * @param request        the request which caused the successful authentication
     * @param response       the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     *                       the authentication process.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("oauth2 성공 핸들러 동작");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // OAuth2User의 속성에서 필요한 정보를 가져옵니다.
        String registrationId = getRegistrationId(authentication);

        // OAuth2Response 객체를 생성합니다.
        OAuth2Response oAuth2Response = oAuthResponseFactory.getOAuth2Response(registrationId, oAuth2User.getAttributes());

        String email = oAuth2Response.getEmail();
        log.info("email = {}", email);

        Optional<User> byEmail = userRepository.findByEmail(email);
        byEmail
                .orElseThrow(() -> new AccountException(UserErrorCode.USER_NOT_FOUND));

        jwtService.issueTokensAndSetCookies(response, byEmail.get().getId());
    }

    private static String getRegistrationId(Authentication authentication) {
        String registrationId = null;
        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            registrationId = oauthToken.getAuthorizedClientRegistrationId();
            System.out.println("registrationId = " + registrationId);
        }
        return registrationId;
    }
}
