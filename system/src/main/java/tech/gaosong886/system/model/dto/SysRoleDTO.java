package tech.gaosong886.system.model.dto;

import java.util.List;
import java.util.ArrayList;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.gaosong886.shared.validation.annotation.LetterOrNumber;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleDTO {
    @NotEmpty
    @LetterOrNumber
    @Length(min = 1, max = 31)
    private String name;

    @LetterOrNumber
    @Length(max = 255)
    private String description = "";

    private List<Integer> menuIds = new ArrayList<>();
}
