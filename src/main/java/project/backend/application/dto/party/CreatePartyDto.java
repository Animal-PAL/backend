package project.backend.application.dto.party;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "파티 생성을 위한 요청 DTO")
public record CreatePartyDto(
        @Schema(description = "파티 이름", example = "한강에서 치맥 파티")
        @NotBlank(message = "파티 이름은 필수입니다.")
        String partyname,

        @Schema(description = "파티에 대한 설명", example = "금요일 저녁, 여의나루역 근처에서 모여요!")
        String description,

        @Schema(description = "파티 종료 시간", example = "2025-08-15T22:00:00")
        @NotNull(message = "파티 종료 시간은 필수입니다.")
        @Future(message = "파티 종료 시간은 현재보다 미래여야 합니다.")
        LocalDateTime endTime,

        @Schema(description = "최대 참여 인원", example = "10")
        @Positive(message = "최대 참여 인원은 0보다 커야 합니다.")
        Integer maxParticipants
) {
}