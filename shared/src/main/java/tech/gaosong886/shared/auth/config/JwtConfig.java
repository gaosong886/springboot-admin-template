package tech.gaosong886.shared.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Jwt 配置
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private Long accessTokenExpiresInSec;
    private Long refreshTokenExpiresInSec;
    private String publicKeyBase64;
    private String privateKeyBase64;
}
