package project.backend.application.dto.ticket;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "초대 링크를 통해 티켓을 발급받기 위한 요청 DTO")
public record ReceiveTicketRequest(
        @Schema(description = "사용할 초대 링크의 고유 코드", example = "a1b2c3d4-e5f6-...")
        @NotBlank(message = "초대 코드는 필수입니다.")
        String inviteCode
) {
}