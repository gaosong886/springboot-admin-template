package tech.gaosong886.system.constant;

import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * 账户状态
 */
@ToString
@AllArgsConstructor
public enum AccountStatus {
    // 活动
    ACTIVE(0),

    // 封禁
    BANNED(-1);

    public final Integer value;
}
