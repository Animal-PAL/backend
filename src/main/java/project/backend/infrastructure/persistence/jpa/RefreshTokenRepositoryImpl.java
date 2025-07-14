package project.backend.infrastructure.persistence.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.backend.domain.model.refresh.QRefresh;
import project.backend.domain.model.refresh.Refresh;
import project.backend.infrastructure.persistence.repository.RefreshCustomRepository;

import java.time.LocalDateTime;


@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshCustomRepository {

    private final JPAQueryFactory queryFactory;

    // QueryDSL이 생성한 Q-Type 클래스를 static으로 가져오면 코드가 간결해집니다.
    private static final QRefresh refresh = QRefresh.refresh;


    @Override
    public Refresh validatedRefreshToken(String accessTokenValue, String refreshTokenValue, LocalDateTime currentDate) {
        // 주석을 해제하여 실제 쿼리가 동작하도록 합니다.
        return queryFactory.selectFrom(refresh)
                .where(refresh.accessToken.eq(accessTokenValue)
                        .and(refresh.refreshToken.eq(refreshTokenValue))
                        .and(refresh.expireDate.after(currentDate))
                )
                .fetchFirst();
    }
}
