package project.backend.infrastructure.oauth2;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import project.backend.domain.model.user.User;
import project.backend.domain.model.user.UserRole;
import project.backend.domain.repository.UserRepository;
import project.backend.infrastructure.oauth2.dto.OAuth2Response;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final OAuthResponseFactory oAuthResponseFactory;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = oAuthResponseFactory.getOAuth2Response(registrationId, oAuth2User.getAttributes());

        String email = oAuth2Response.getEmail();

        Optional<User> byUser = userRepository.findByEmail(email);

        if (byUser.isEmpty()) {
            User user = User.builder()
                    .userName(oAuth2Response.getName())
                    .email(oAuth2Response.getEmail())
                    .password("")
                    .isSocial(2L)
                    .role(UserRole.USER)
                    .build();
            userRepository.save(user);
        }
        return oAuth2User;
    }
}
