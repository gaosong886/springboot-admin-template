package tech.gaosong886.system.model.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.gaosong886.shared.validation.annotation.LetterOrNumber;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    @NotEmpty
    @LetterOrNumber
    @Length(min = 5, max = 15)
    private String username;

    @NotEmpty
    @LetterOrNumber
    @Length(min = 8, max = 15)
    private String password;
}
