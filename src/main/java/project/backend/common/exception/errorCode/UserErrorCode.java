package project.backend.common.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode{

    INVAILD_USER(HttpStatus.BAD_REQUEST, "Invalid user"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "User already exists"),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "Password mismatch"),

    // Refresh
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "Refresh token not found"),


    // security
    OAUTH2_NOT_FOUND(HttpStatus.NOT_FOUND, "OAuth2 not found"),
    OAUTH2_NOT_MATCH(HttpStatus.UNAUTHORIZED, "OAuth2 not match"),
    OAUTH2_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "OAuth2 Login Again"),
    OAUTH2_NOT_SUPPORT(HttpStatus.UNAUTHORIZED, "OAuth2 not support"),


    // ticket
    TICKET_NOT_FOUND(HttpStatus.NOT_FOUND, "Ticket not found"),
    ROOT_TICKET_NOT_FOUND(HttpStatus.NOT_FOUND, "Root ticket not found"),
    NO_AVAILABLE_TICKET_FOR_INVITE(HttpStatus.NOT_FOUND, "No available ticket for invite"),
    TICKET_ALREADY_RECEIVED(HttpStatus.CONFLICT, "Ticket already received"),
    PARENT_TICKET_NOT_AVAILABLE(HttpStatus.CONFLICT, "Parent ticket not available"),
    ALREADY_PARTICIPATING_IN_PARTY(HttpStatus.CONFLICT, "Already participating in party"),

    // party
    PARTY_NOT_FOUND(HttpStatus.NOT_FOUND, "Party not found"),
    NOT_PARTY_HOST(HttpStatus.FORBIDDEN, "Not party host"),

    // participation

    PARTICIPATIONHISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Participation history not found"),
;

    private final HttpStatus httpStatus;
    private final String message;
}
