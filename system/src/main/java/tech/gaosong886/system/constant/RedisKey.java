package tech.gaosong886.system.constant;

/**
 * Redis Key 定义
 */
public class RedisKey {
    // 用户信息
    public static String SYS_USER(int userId) {
        return "SYS_USER:" + userId;
    }

    // 角色权限
    public static String SYS_ROLE_PERMISSION(int roleId) {
        return "SYS_ROLE:" + roleId + ":PERMISSION";
    }
}
