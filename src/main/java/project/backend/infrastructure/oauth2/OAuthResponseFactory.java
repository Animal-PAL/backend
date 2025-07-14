package project.backend.infrastructure.oauth2;

import org.springframework.stereotype.Component;
import project.backend.infrastructure.oauth2.dto.GoogleResponse;
import project.backend.infrastructure.oauth2.dto.KakaoResponse;
import project.backend.infrastructure.oauth2.dto.NaverResponse;
import project.backend.infrastructure.oauth2.dto.OAuth2Response;

import java.util.Map;

@Component
public class OAuthResponseFactory {

    public static OAuth2Response getOAuth2Response(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "naver" -> new NaverResponse(attributes);
            case "google" -> new GoogleResponse(attributes);
            case "kakao" -> new KakaoResponse(attributes);
            default -> null;
        };
    }
}