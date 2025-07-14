package project.backend.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.common.exception.errorCode.ErrorCode;

@RequiredArgsConstructor
@Getter
public class AccountException extends RuntimeException {
    private final ErrorCode errorCode;
}
