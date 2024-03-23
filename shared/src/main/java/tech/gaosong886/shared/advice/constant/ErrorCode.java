package tech.gaosong886.shared.advice.constant;

import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * 通用调用状态码
 */
@ToString
@AllArgsConstructor
public enum ErrorCode {
    SUCCESS(0, "success");

    public final int value;
    public final String message;
}
