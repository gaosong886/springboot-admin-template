package tech.gaosong886.system.model.vo;

import java.util.List;
import java.time.LocalDateTime;

public record SysUserVO(
        Integer id,
        String photo,
        String nickname,
        String username,
        Integer accountStatus,
        String remark,
        List<SysRoleLiteVO> roles,
        LocalDateTime createAt,
        LocalDateTime updateAt) {
}
