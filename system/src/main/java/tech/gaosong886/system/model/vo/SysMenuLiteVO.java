package tech.gaosong886.system.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysMenuLiteVO {
    private Integer id;
    private String name;
    private Integer type;
    private Integer parentId;
}
