package tech.gaosong886.system.model.vo;

import java.util.List;

public record SysMenuVO(
        Integer id,
        String name,
        Integer type,
        String icon,
        Integer parentId,
        String path,
        Integer sortWeight,
        Integer hidden,
        List<SysPermissionVO> permissions) {
}
