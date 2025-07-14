package project.backend.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import project.backend.common.exception.errorCode.ErrorCode;

@Slf4j
@RestControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({AccountException.class})
    public ResponseEntity<ErrorResponse> handlerAccountException(final AccountException e) {
        log.warn("AccountException: {}", e.getErrorCode().getMessage());
        return makeErrorResponse(e.getErrorCode());
    }

    public ResponseEntity<ErrorResponse> makeErrorResponse(final ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .code(errorCode.name())
                        .message(errorCode.getMessage())
                        .build());
    }
}
