package project.backend.common.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    // 400 BAD_REQUEST
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "Invalid input value"),
    METHOD_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "Method not allowed"),
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST, "Entity not found"),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "Invalid type value"),
    HANDLE_ACCESS_DENIED(HttpStatus.BAD_REQUEST, "Access denied"),

    // 401 UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),

    // 403 FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden"),

    // 404 NOT_FOUND
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not found"),

    // 500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");

    private final HttpStatus httpStatus;
    private final String message;
}
