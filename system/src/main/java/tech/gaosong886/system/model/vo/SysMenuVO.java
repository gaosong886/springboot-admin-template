package tech.gaosong886.system.model.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysMenuVO {
    private Integer id;
    private String name;
    private Integer type;
    private String icon;
    private Integer parentId;
    private String path;
    private Integer sortWeight;
    private Integer hidden;
    private List<SysPermissionVO> permissions;
}
