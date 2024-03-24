package tech.gaosong886.system.model.vo;

import java.time.LocalDateTime;
import java.util.List;

public record SysRoleVO(
        Integer id,
        String name,
        String description,
        LocalDateTime createAt,
        LocalDateTime updateAt,
        List<SysMenuLiteVO> menus) {
}
