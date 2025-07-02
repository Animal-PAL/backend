package project.backend.app.user.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import project.backend.app.user.dto.LoginInfoRequestDto;
import project.backend.app.user.dto.SignUpRequestDto;
import project.backend.app.user.entity.User;
import project.backend.app.user.entity.UserRole;
import project.backend.app.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

     private final UserRepository userRepository;

    @Override
    public void signUp(SignUpRequestDto signUpRequestDto) {
        User user = User.builder()
                .email(signUpRequestDto.email())
                .userName(signUpRequestDto.userName())
                .password(signUpRequestDto.password())
                .isSocial(0L)
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
    }

    @Override
    public void login(LoginInfoRequestDto loginInfoRequestDto, HttpServletResponse response) {

    }
}
