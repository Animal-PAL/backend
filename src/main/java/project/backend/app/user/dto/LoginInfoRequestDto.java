package project.backend.app.user.dto;

import lombok.Builder;

@Builder
public record LoginInfoRequestDto (
        String email,
        String password
) {
}
