package project.backend.app.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import project.backend.swagger.SwaggerDto;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
@Tag(name = "User", description = "회원관련 API")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<ApiResponse<Void>> signUp(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "요청 JSON",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SignUpRequestDto.class))
            )
            @RequestBody SignUpRequestDto signUpRequestDto) {
        // 회원가입 로직을 여기에 추가
        userService.signUp(signUpRequestDto);
        return ResponseEntity.ok(ApiResponse.createSuccessNoContent("회원가입이 완료되었습니다."));
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login(@RequestBody LoginInfoRequestDto loginInfoRequestDto, HttpServletResponse response) {
        // 로그인 로직을 여기에 추가
        userService.login(loginInfoRequestDto, response);

        return ResponseEntity.ok(ApiResponse.createSuccessNoContent("로그인 성공"));
    }
}
