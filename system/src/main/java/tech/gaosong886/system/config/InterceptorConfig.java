package tech.gaosong886.system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.gaosong886.shared.auth.interceptor.JwtAuthInterceptor;
import tech.gaosong886.system.interceptor.AccessPermissionInterceptor;

/**
 * 拦截器设置
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private JwtAuthInterceptor jwtAuthInterceptor;

    @Autowired
    private AccessPermissionInterceptor aaAccessPermissionInterceptor;

    @SuppressWarnings("null")
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 JWT 鉴权拦截器
        registry.addInterceptor(this.jwtAuthInterceptor)
                .addPathPatterns("/**");

        // 注册访问权限拦截器
        registry.addInterceptor(this.aaAccessPermissionInterceptor)
                .addPathPatterns("/**");
    }
}