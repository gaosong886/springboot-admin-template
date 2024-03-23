package tech.gaosong886.system.model.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import java.util.List;
import java.util.ArrayList;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.gaosong886.shared.validation.annotation.EnumValue;
import tech.gaosong886.shared.validation.annotation.LetterOrNumber;
import tech.gaosong886.system.constant.AccountStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserCreateDTO {
    @NotEmpty
    @LetterOrNumber
    @Length(min = 5, max = 15)
    private String username;

    @NotEmpty
    @Length(min = 8, max = 15)
    private String password;

    @NotEmpty
    @LetterOrNumber
    @Length(min = 1, max = 15)
    private String nickname;

    @URL
    @Length(max = 255)
    private String photo = "";

    @EnumValue(enumClass = AccountStatus.class)
    private Integer accountStatus = 0;

    @Length(max = 255)
    @LetterOrNumber
    private String remark = "";

    private List<Integer> roleIds = new ArrayList<>();
}
