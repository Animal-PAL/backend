package project.backend.application.dto.ticket;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "초대 링크 생성을 위한 요청 DTO")
public record CreateInviteLinkRequest(
        @Schema(description = "초대 링크를 생성할 파티의 ID", example = "1")
        @NotNull(message = "파티 ID는 필수입니다.")
        Long partyId
) {
}