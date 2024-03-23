package tech.gaosong886.system.constant;

import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * 菜单类型
 */
@ToString
@AllArgsConstructor
public enum MenuType {
    // 目录
    DIRECTORY(0),

    // 页面
    PAGE(1),

    // 权限
    OPERATION(2);

    public final Integer value;
}
