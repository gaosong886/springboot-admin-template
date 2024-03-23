package tech.gaosong886.shared.auth.constant;

import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * Jwt Token 类型
 */
@ToString
@AllArgsConstructor
public enum JwtTokenType {
    ACCESS(0),
    REFRESH(1);

    public final Integer value;
}
