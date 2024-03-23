package tech.gaosong886.system.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.gaosong886.shared.validation.annotation.EnumValue;
import tech.gaosong886.shared.validation.annotation.LetterOrNumber;
import tech.gaosong886.system.constant.AccountStatus;
import java.util.List;
import java.util.ArrayList;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserUpdateDTO {
    @NotEmpty
    @LetterOrNumber
    @Length(min = 5, max = 15)
    private String username;

    @LetterOrNumber
    @Length(min = 8, max = 15)
    private String password;

    @NotEmpty
    @LetterOrNumber
    @Length(min = 1, max = 15)
    private String nickname;

    @Length(max = 255)
    @URL
    private String photo = "";

    @NotNull
    @EnumValue(enumClass = AccountStatus.class)
    private Integer accountStatus;

    @Length(max = 255)
    @LetterOrNumber
    private String remark = "";

    private List<Integer> roleIds = new ArrayList<>();
}
