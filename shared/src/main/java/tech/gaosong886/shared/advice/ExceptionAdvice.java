package tech.gaosong886.shared.advice;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import tech.gaosong886.shared.advice.model.ApiResponse;

/**
 * 全局异常 Handler
 */
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<String> handleException(HttpServletResponse res, Exception e) {
        log.error("Internal Server Error.", e);
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error.");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleException(HttpServletResponse res, NoHandlerFoundException e) {
        return new ResponseEntity<>(
                ApiResponse.fail(e.getStatusCode().value(), e.getMessage()), e.getStatusCode());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleException(HttpServletResponse res, NoResourceFoundException e) {
        return new ResponseEntity<>(
                ApiResponse.fail(e.getStatusCode().value(), e.getMessage()), e.getStatusCode());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<String>> handleException(HttpServletResponse res,
            ResponseStatusException e) {
        return new ResponseEntity<>(
                ApiResponse.fail(e.getStatusCode().value(), e.getMessage()), e.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleException(HttpServletResponse res,
            MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        BindingResult bindingResult = e.getBindingResult();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(
                ApiResponse.fail(
                        HttpStatus.BAD_REQUEST.value(),
                        "Validation error.", errors),
                HttpStatus.BAD_REQUEST);
    }
}
