package project.backend.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import project.backend.domain.model.refresh.Refresh;
import project.backend.domain.model.user.User;
import project.backend.infrastructure.config.QuerydslConfig;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({QuerydslConfig.class, RefreshTokenRepositoryImpl.class})
@DisplayName("Repository - RefreshTokenRepository Test")
class RefreshTokenRepositoryImplTest {

    @Autowired
    private RefreshTokenRepositoryImpl refreshTokenRepository;

    @Autowired
    private EntityManager em;

    private User testUser;
    private final String accessToken = "test-access-token";
    private final String refreshToken = "test-refresh-token";

    // ... rest of your test code remains the same ...
    @BeforeEach
    void setUp() {
        // Persist a common user for all tests
        testUser = User.builder().userName("testuser").email("test@test.com").build();
        em.persist(testUser);
    }

    /**
     * Helper method to create and save a Refresh entity for test scenarios.
     */
    private void createAndSaveRefresh(String accessToken, String refreshToken, LocalDateTime expireDate) {
        Refresh refresh = Refresh.builder()
                .user(testUser)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expireDate(expireDate)
                .build();
        em.persist(refresh);
    }

    @Nested
    @DisplayName("Success Cases")
    class Success {
        @Test
        @DisplayName("Should find the Refresh entity when all conditions (tokens, expiration) are valid")
        void validatedRefreshToken_success() {
            // given: A valid, non-expired token is saved in the database.
            LocalDateTime futureExpireDate = LocalDateTime.now().plusDays(1);
            createAndSaveRefresh(accessToken, refreshToken, futureExpireDate);
            em.flush(); // Ensure data is written to the DB
            em.clear(); // Clear the persistence context to ensure a clean read from the DB

            // when: The repository method is called with the correct parameters.
            Refresh foundRefresh = refreshTokenRepository.validatedRefreshToken(accessToken, refreshToken, LocalDateTime.now());

            // then: The correct Refresh entity is returned.
            assertThat(foundRefresh).isNotNull();
            assertThat(foundRefresh.getAccessToken()).isEqualTo(accessToken);
            assertThat(foundRefresh.getRefreshToken()).isEqualTo(refreshToken);
        }
    }

    @Nested
    @DisplayName("Failure Cases")
    class Failure {

        @Test
        @DisplayName("Should return null when the refresh token does not match")
        void validatedRefreshToken_fail_whenRefreshTokenMismatched() {
            // given
            LocalDateTime futureExpireDate = LocalDateTime.now().plusDays(1);
            createAndSaveRefresh(accessToken, refreshToken, futureExpireDate);
            em.flush();
            em.clear();

            // when: The method is called with an incorrect refresh token.
            String wrongRefreshToken = "wrong-refresh-token";
            Refresh foundRefresh = refreshTokenRepository.validatedRefreshToken(accessToken, wrongRefreshToken, LocalDateTime.now());

            // then: The result should be null.
            assertThat(foundRefresh).isNull();
        }

        @Test
        @DisplayName("Should return null when the access token does not match")
        void validatedRefreshToken_fail_whenAccessTokenMismatched() {
            // given
            LocalDateTime futureExpireDate = LocalDateTime.now().plusDays(1);
            createAndSaveRefresh(accessToken, refreshToken, futureExpireDate);
            em.flush();
            em.clear();

            // when: The method is called with an incorrect access token.
            String wrongAccessToken = "wrong-access-token";
            Refresh foundRefresh = refreshTokenRepository.validatedRefreshToken(wrongAccessToken, refreshToken, LocalDateTime.now());

            // then: The result should be null.
            assertThat(foundRefresh).isNull();
        }

        @Test
        @DisplayName("Should return null when the token is already expired")
        void validatedRefreshToken_fail_whenTokenExpired() {
            // given: An expired token is saved in the database.
            LocalDateTime pastExpireDate = LocalDateTime.now().minusDays(1);
            createAndSaveRefresh(accessToken, refreshToken, pastExpireDate);
            em.flush();
            em.clear();

            // when: The method is called.
            Refresh foundRefresh = refreshTokenRepository.validatedRefreshToken(accessToken, refreshToken, LocalDateTime.now());

            // then: The result should be null because the token's expiration date is in the past.
            assertThat(foundRefresh).isNull();
        }

        @Test
        @DisplayName("Should return null when no matching token exists in the database")
        void validatedRefreshToken_fail_whenNoTokenExists() {
            // given: The database is empty of any matching tokens.

            // when: The method is called with arbitrary values.
            Refresh foundRefresh = refreshTokenRepository.validatedRefreshToken("any-token", "any-token", LocalDateTime.now());

            // then: The result should be null.
            assertThat(foundRefresh).isNull();
        }
    }
}