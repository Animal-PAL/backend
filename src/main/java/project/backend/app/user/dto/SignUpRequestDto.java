package project.backend.app.user.dto;

import lombok.Builder;

@Builder
public record SignUpRequestDto(
        String email,
        String userName,
        String password
) {

}
