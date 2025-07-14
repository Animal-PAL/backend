package project.backend.application.dto.ticket;

public record CreateTicketRequest(
        Long inviteLinkId,
        Long userId
) {
}
