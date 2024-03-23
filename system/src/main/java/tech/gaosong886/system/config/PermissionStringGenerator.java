package tech.gaosong886.system.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import tech.gaosong886.shared.auth.annotation.AllowAnonymous;
import tech.gaosong886.system.annotation.AvoidPermission;
import tech.gaosong886.system.service.SysPermissionService;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * 初始化权限字符串，格式如 sys-user:update
 */
@Component
public class PermissionStringGenerator implements BeanPostProcessor {

    @Lazy
    @Autowired
    private SysPermissionService sysPermissionService;

    @SuppressWarnings("null")
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RequestMappingHandlerMapping) {
            RequestMappingHandlerMapping handlerMapping = (RequestMappingHandlerMapping) bean;
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

            Set<String> permissionStringSet = new HashSet<>();

            for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
                HandlerMethod handlerMethod = entry.getValue();

                // 跳过带有 @AllowAnonymous 或者 @AvoidPermission 注解的方法
                if (handlerMethod.getMethodAnnotation(AllowAnonymous.class) != null
                        || handlerMethod.getMethodAnnotation(AvoidPermission.class) != null)
                    continue;

                // 权限字符串 = 控制器映射路径 + ":" + 方法名
                String permissionString = this.sysPermissionService.getPermissionStringByHandlerMethod(handlerMethod);
                if (permissionString != null)
                    permissionStringSet.add(permissionString);
            }

            this.sysPermissionService.savePermissions(permissionStringSet);
        }
        return bean;
    }
}