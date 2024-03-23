package tech.gaosong886.system.model.dto;

import org.hibernate.validator.constraints.Length;
import java.util.List;
import java.util.ArrayList;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.gaosong886.shared.validation.annotation.EnumValue;
import tech.gaosong886.shared.validation.annotation.FilePath;
import tech.gaosong886.shared.validation.annotation.LetterOrNumber;
import tech.gaosong886.system.constant.MenuType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysMenuDTO {
    @NotEmpty
    @LetterOrNumber
    @Length(min = 1, max = 15)
    private String name;

    @NotNull
    @EnumValue(enumClass = MenuType.class)
    private Integer type;

    @Length(max = 255)
    @LetterOrNumber
    private String icon = "";

    private Integer parentId = 0;

    @FilePath
    @Length(max = 255)
    private String path = "";

    private Integer sortWeight = 0;

    private Integer hidden = 0;

    private List<Integer> permissionIds = new ArrayList<>();
}
