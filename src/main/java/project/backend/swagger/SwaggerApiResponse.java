package project.backend.swagger;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "응답 DTO")
public class SwaggerApiResponse {

    @Schema(description = "성공 여부", example = "true")
    private boolean success;
    @Schema(description = "메시지", example = "비밀번호 재설정 성공")
    private String message;

    public SwaggerApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
