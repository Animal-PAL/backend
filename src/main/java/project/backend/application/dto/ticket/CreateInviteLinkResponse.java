package project.backend.application.dto.ticket;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "생성된 초대 링크 정보를 담는 응답 DTO")
public record CreateInviteLinkResponse(
        @Schema(description = "생성된 전체 초대 URL", example = "/tickets/receive?inviteCode=a1b2c3d4-e5f6-...")
        String inviteUrl
) {
}