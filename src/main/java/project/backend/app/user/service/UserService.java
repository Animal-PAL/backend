package project.backend.app.user.service;

import jakarta.servlet.http.HttpServletResponse;
import project.backend.app.user.dto.LoginInfoRequestDto;
import project.backend.app.user.dto.SignUpRequestDto;

public interface UserService {
    void signUp(SignUpRequestDto signUpRequestDto);

    void login(LoginInfoRequestDto loginInfoRequestDto, HttpServletResponse response);
}
