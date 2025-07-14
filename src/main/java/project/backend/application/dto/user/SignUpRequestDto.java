package project.backend.application.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "회원가입 요청 DTO")
public record SignUpRequestDto(
        @Schema(description = "사용자 이메일 주소", example = "user@example.com")
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        String email,

        @Schema(description = "사용자 이름 (닉네임)", example = "홍길동")
        @NotBlank(message = "사용자 이름은 필수입니다.")
        String username,

        @Schema(description = "사용자 비밀번호", example = "password123!")
        @NotBlank(message = "비밀번호는 필수입니다.")
        String password
) {
}