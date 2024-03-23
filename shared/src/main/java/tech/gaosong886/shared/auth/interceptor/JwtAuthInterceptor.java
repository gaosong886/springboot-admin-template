package tech.gaosong886.shared.auth.interceptor;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech.gaosong886.shared.auth.annotation.AllowAnonymous;
import tech.gaosong886.shared.auth.constant.JwtStatics;
import tech.gaosong886.shared.auth.constant.JwtTokenType;
import tech.gaosong886.shared.auth.model.dto.JwtPayloadDTO;
import tech.gaosong886.shared.auth.service.JwtService;

/**
 * Jwt 认证拦截器
 */
@Component
public class JwtAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtService jwtService;

    @SuppressWarnings("null")
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            Object handler) throws Exception {

        // 仅针对 HandlerMethod 进行拦截
        if (!(handler instanceof HandlerMethod))
            return true;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 如果方法带有 @AllowAnonymous 注解，放行
        if (method.isAnnotationPresent(AllowAnonymous.class))
            return true;

        String token = this.jwtService.getTokenFromRequest(httpServletRequest);
        if (token == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access token is required.");

        JwtPayloadDTO payload = this.jwtService.verify(JwtTokenType.ACCESS.value, token, JwtPayloadDTO.class);

        // 把解析出的 Jwt Payload 放入 Request 中
        httpServletRequest.setAttribute(JwtStatics.JWT_PAYLOAD_USER_KEY, payload);

        return true;
    }
}