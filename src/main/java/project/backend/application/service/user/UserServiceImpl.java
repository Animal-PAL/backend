package project.backend.application.service.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.backend.domain.model.user.User;
import project.backend.application.dto.user.LoginInfoRequestDto;
import project.backend.application.dto.user.SignUpRequestDto;
import project.backend.domain.model.user.UserRole;
import project.backend.domain.repository.UserRepository;
import project.backend.common.jwtToken.JwtService;
import project.backend.common.exception.AccountException;
import project.backend.common.exception.errorCode.UserErrorCode;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

     private final UserRepository userRepository;
     private final JwtService jwtService;

    @Override
    public void signUp(SignUpRequestDto signUpRequestDto) {
        // 기존 유저 확인
        if (userRepository.existsByEmail(signUpRequestDto.email())) {
            throw new AccountException(UserErrorCode.USER_ALREADY_EXISTS);
        };
        // 유저 정보 생성
        User user = User.builder()
                .email(signUpRequestDto.email())
                .password(signUpRequestDto.password())
                .userName(signUpRequestDto.username())
                .isSocial(1L) // 1L: 일반 유저
                .role(UserRole.USER)
                .build();

        // 유저 정보 저장
        userRepository.save(user);
    }

    @Override
    public void login(LoginInfoRequestDto loginInfoRequestDto, HttpServletResponse response) {

        // 유저 정보 가져오기 && 소셜 로그인 아닌 유저 확인
        User user = userRepository.findByEmailAndIsSocialNot(
                loginInfoRequestDto.email()).orElseThrow(() -> new AccountException(UserErrorCode.USER_NOT_FOUND)
        );

        boolean equals = user.getPassword().equals(loginInfoRequestDto.password());
        if (!equals) {
            throw new AccountException(UserErrorCode.PASSWORD_MISMATCH);
        }

        // 쿠키에 토큰 저장
        jwtService.issueTokensAndSetCookies(response, user.getId());
    }


}
