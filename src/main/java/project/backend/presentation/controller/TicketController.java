package project.backend.presentation.web.ticket;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.backend.application.dto.ticket.CreateInviteLinkRequest;
import project.backend.application.dto.ticket.CreateInviteLinkResponse;
import project.backend.application.dto.ticket.ReceiveTicketRequest;
import project.backend.application.service.ticket.TicketService;

@Tag(name = "Ticket API", description = "티켓 및 초대 링크 관련 API")
@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @Operation(summary = "초대 링크 생성", description = "사용 가능한 티켓으로 파티 초대 링크를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "초대 링크 생성 성공"),
            @ApiResponse(responseCode = "404", description = "초대 가능한 티켓이 없음 (NO_AVAILABLE_TICKET_FOR_INVITE)")
    })
    @PostMapping("/invite-links")
    public ResponseEntity<CreateInviteLinkResponse> createInviteLink(@RequestBody @Valid CreateInviteLinkRequest request) {
        CreateInviteLinkResponse response = ticketService.createInviteLink(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "초대 링크 사용 (티켓 받기)", description = "초대 링크를 통해 파티에 참여하고 티켓을 발급받습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "티켓 발급 및 파티 참여 성공"),
            @ApiResponse(responseCode = "404", description = "초대 링크 또는 부모 티켓을 찾을 수 없음"),
            @ApiResponse(responseCode = "409", description = "이미 사용된 링크 또는 이미 파티에 참여한 사용자 (Conflict)")
    })
    @PostMapping("/receive")
    public ResponseEntity<Void> receiveTicket(@RequestBody @Valid ReceiveTicketRequest request) {
        ticketService.receiveTicket(request);
        return ResponseEntity.noContent().build();
    }
}
