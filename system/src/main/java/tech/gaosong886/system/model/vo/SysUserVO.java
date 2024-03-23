package tech.gaosong886.system.model.vo;

import java.util.List;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserVO {
    private Integer id;
    private String photo;
    private String nickname;
    private String username;
    private Integer accountStatus;
    private String remark;
    private List<SysRoleLiteVO> roles;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
