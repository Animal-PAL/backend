package project.backend.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.backend.application.dto.party.CreatePartyDto;
import project.backend.application.dto.party.GetPartyResponse;
import project.backend.application.dto.party.UpdatePartyRequest; // 개선된 DTO
import project.backend.application.service.party.PartyService;
import project.backend.common.jwtToken.JwtService;

import java.util.List;

@Tag(name = "Party API", description = "파티 생성, 조회, 수정, 취소 관련 API")
@RestController
@RequestMapping("/api/v1/parties")
@RequiredArgsConstructor
public class PartyController {

    private final PartyService partyService;
    private final JwtService jwtService;

    @Operation(summary = "파티 생성", description = "새로운 파티를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "파티 생성 성공")
    })
    @PostMapping
    public ResponseEntity<Void> createParty(@RequestBody @Valid CreatePartyDto createPartyDto) {
        partyService.createParty(createPartyDto);
        // 생성 성공 시 201 Created 상태 코드 반환
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "특정 파티 조회", description = "파티 ID로 특정 파티의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "파티를 찾을 수 없음 (PARTY_NOT_FOUND)")
    })
    @GetMapping("/{partyId}")
    public ResponseEntity<GetPartyResponse> getPartyById(@PathVariable Long partyId) {
        GetPartyResponse response = partyService.getPartyById(partyId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내가 참여한 파티 목록 조회", description = "현재 로그인한 사용자가 참여하고 있는 모든 파티 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공 (목록이 비어있을 수 있음)")
    })
    @GetMapping("/my")
    public ResponseEntity<List<GetPartyResponse>> getMyParties() {
        // JWT 토큰에서 현재 사용자 ID를 가져와 서비스에 전달합니다.
        Long currentUserId = jwtService.getCurrentUserId();
        List<GetPartyResponse> parties = partyService.getAllParties(currentUserId);
        return ResponseEntity.ok(parties);
    }

    @Operation(summary = "파티 정보 수정", description = "파티의 호스트만 파티 정보를 수정할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음 (NOT_PARTY_HOST)"),
            @ApiResponse(responseCode = "404", description = "파티를 찾을 수 없음 (PARTY_NOT_FOUND)")
    })
    @PutMapping("/{partyId}")
    public ResponseEntity<Void> updateParty(@RequestBody @Valid UpdatePartyRequest request) {
        // Controller에서 Service가 요구하는 DTO 형태로 변환하여 전달합니다.
        UpdatePartyRequest updatePartyRequest = new UpdatePartyRequest(
                request.partyId(),
                request.partyName(),
                request.description(),
                request.endTime(),
                request.maxParticipants()
        );
        partyService.updateParty(updatePartyRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "파티 취소", description = "파티의 호스트만 파티를 취소 상태로 변경할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "파티 취소 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음 (NOT_PARTY_HOST)"),
            @ApiResponse(responseCode = "404", description = "파티를 찾을 수 없음 (PARTY_NOT_FOUND)")
    })
    @DeleteMapping("/{partyId}")
    public ResponseEntity<Void> cancelParty(@PathVariable Long partyId) {
        partyService.cancelParty(partyId);
        // 성공적으로 처리되었으나 반환할 콘텐츠가 없을 때 204 No Content 상태 코드 사용
        return ResponseEntity.noContent().build();
    }
}