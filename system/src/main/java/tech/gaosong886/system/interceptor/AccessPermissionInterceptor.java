package tech.gaosong886.system.interceptor;

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
import tech.gaosong886.shared.auth.model.dto.JwtPayloadDTO;
import tech.gaosong886.shared.auth.model.dto.JwtPayloadUserRoleDTO;
import tech.gaosong886.system.annotation.AvoidPermission;
import tech.gaosong886.system.service.SysPermissionService;
import tech.gaosong886.system.service.SysRoleService;

/**
 * 访问权限拦截器
 */
@Component
public class AccessPermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysPermissionService sysPermissionService;

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

        // 如果方法带有 @AvoidPermission 注解，放行
        if (method.isAnnotationPresent(AvoidPermission.class))
            return true;

        String permissionString = this.sysPermissionService.getPermissionStringByHandlerMethod(handlerMethod);
        if (permissionString == null)
            return true;

        JwtPayloadDTO payload = (JwtPayloadDTO) httpServletRequest.getAttribute(JwtStatics.JWT_PAYLOAD_USER_KEY);

        if (payload != null && payload.roles() != null && payload.roles().size() > 0) {
            for (JwtPayloadUserRoleDTO role : payload.roles()) {
                int roleId = role.id();

                // 管理员角色直接放行
                if (roleId == 1)
                    return true;

                if (this.sysRoleService.hasPermission(roleId, permissionString))
                    return true;
            }
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                "Permission denied.");
    }
}
