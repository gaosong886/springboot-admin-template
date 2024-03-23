package tech.gaosong886.shared.advice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import tech.gaosong886.shared.advice.constant.ErrorCode;
import lombok.AllArgsConstructor;

/**
 * 通用响应体封装
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> resultData = new ApiResponse<>();
        resultData.setCode(ErrorCode.SUCCESS.value);
        resultData.setMessage(ErrorCode.SUCCESS.message);
        resultData.setData(data);
        return resultData;
    }

    public static <T> ApiResponse<T> fail(int code, String message) {
        return fail(code, message, null);
    }

    public static <T> ApiResponse<T> fail(int code, String message, T data) {
        ApiResponse<T> resultData = new ApiResponse<>();
        resultData.setCode(code);
        resultData.setMessage(message);
        resultData.setData(data);
        return resultData;
    }
}