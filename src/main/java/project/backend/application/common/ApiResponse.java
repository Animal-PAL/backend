package project.backend.application.common;

import lombok.Getter;

@Getter
public class ApiResponse<T>{

    private static final String SUCCESS_STATUS = "success";
    private static final String FAIL_STATUS = "fail";
    private static final String ERROR_STATUS = "error";

    private String Status;
    private T data;
    private String message;

    public ApiResponse(String status, T data, String message) {
        this.Status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> ApiResponse<T> createSuccess(T data, String message) {
        return new ApiResponse<>(SUCCESS_STATUS, data, message);
    }

    public static <T> ApiResponse<T> createSuccessNoContent(String message) {
        return new ApiResponse<>(SUCCESS_STATUS, null, message);
    }
}
