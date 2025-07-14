package project.backend.application.dto.party;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@Schema(description = "파티 정보 수정을 위한 요청 DTO")
public record UpdatePartyRequest(

        @Schema(description = "파티 ID", example = "1")
        Long partyId,

        @Schema(description = "새로운 파티 이름", example = "잠실에서 보드게임 파티")
        @NotBlank(message = "파티 이름은 필수입니다.")
        String partyName,


        @Schema(description = "새로운 파티 설명", example = "토요일 오후, 보드게임 카페에서 만나요!")
        String description,

        @Schema(description = "새로운 파티 종료 시간", example = "2025-08-16T18:00:00")
        @Future(message = "파티 종료 시간은 현재보다 미래여야 합니다.")
        LocalDateTime endTime,

        @Schema(description = "새로운 최대 참여 인원", example = "8")
        @Positive(message = "최대 참여 인원은 0보다 커야 합니다.")
        Integer maxParticipants
) {
}