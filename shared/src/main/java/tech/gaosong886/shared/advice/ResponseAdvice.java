package tech.gaosong886.shared.advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import tech.gaosong886.shared.advice.model.ApiResponse;

/**
 * 全局响应体封装
 */
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("null")
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @SuppressWarnings("null")
    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType,
            Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
            ServerHttpResponse serverHttpResponse) {

        if (o instanceof String s)
            return objectMapper.writeValueAsString(ApiResponse.success(s));
        if (o instanceof ApiResponse r)
            return r;

        return ApiResponse.success(o);
    }
}
