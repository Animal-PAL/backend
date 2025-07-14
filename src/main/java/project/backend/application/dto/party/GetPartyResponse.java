package project.backend.application.dto.party;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import project.backend.domain.model.party.Party;
import project.backend.domain.model.party.PartyStatus;

import java.time.LocalDateTime;

@Builder
@Schema(description = "파티 정보 조회를 위한 응답 DTO")
public record GetPartyResponse(
        @Schema(description = "파티 ID", example = "1")
        Long partyId,

        @Schema(description = "파티 이름", example = "한강에서 치맥 파티")
        String partyName,

        @Schema(description = "파티 설명", example = "금요일 저녁, 여의나루역 근처에서 모여요!")
        String description,

        @Schema(description = "파티 주최자 이름", example = "홍길동")
        String hostName,

        @Schema(description = "최대 참여 인원", example = "10")
        Integer maxParticipants,

        @Schema(description = "현재 파티 상태", example = "OPEN")
        PartyStatus partyStatus,

        @Schema(description = "파티 종료 시간", example = "2025-08-15T22:00:00")
        LocalDateTime endTime,

        @Schema(description = "파티 생성 시간", example = "2025-08-14T10:00:00")
        LocalDateTime createdAt
) {
    // 엔티티를 DTO로 변환하는 정적 팩토리 메소드
    public static GetPartyResponse from(Party party) {
        return GetPartyResponse.builder()
                .partyId(party.getId())
                .partyName(party.getPartyName())
                .description(party.getDescription())
                .hostName(party.getHost_user_id().getUserName()) // 연관된 User 엔티티에서 이름 가져오기
                .maxParticipants(party.getMaxParticipants())
                .partyStatus(party.getPartyStatus())
                .endTime(party.getEndTime())
                .createdAt(party.getCreatedAt())
                .build();
    }
}
