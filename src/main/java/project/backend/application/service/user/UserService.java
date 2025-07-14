package project.backend.application.service.user;

import jakarta.servlet.http.HttpServletResponse;
import project.backend.application.dto.user.LoginInfoRequestDto;
import project.backend.application.dto.user.SignUpRequestDto;

public interface UserService {
    void signUp(SignUpRequestDto signUpRequestDto);

    void login(LoginInfoRequestDto loginInfoRequestDto, HttpServletResponse response);
}