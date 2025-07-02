package project.backend.app.user.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.backend.Util.ApiResponse;
import project.backend.app.user.dto.LoginInfoRequestDto;
import project.backend.app.user.dto.SignUpRequestDto;
import project.backend.app.user.service.UserService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        // 회원가입 로직을 여기에 추가
        userService.signUp(signUpRequestDto);
        return ResponseEntity.ok(ApiResponse.createSuccessNoContent("회원가입이 완료되었습니다."));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> login(@RequestBody LoginInfoRequestDto loginInfoRequestDto, HttpServletResponse response) {
        // 로그인 로직을 여기에 추가
        userService.login(loginInfoRequestDto, response);

        return ResponseEntity.ok(ApiResponse.createSuccessNoContent("로그인 성공"));
    }
}
