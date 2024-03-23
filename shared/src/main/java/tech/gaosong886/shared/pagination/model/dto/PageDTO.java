package tech.gaosong886.shared.pagination.model.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.gaosong886.shared.validation.annotation.LetterOrNumber;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO {
    @Min(1)
    private Integer page = 1;

    @Range(min = 20, max = 100)
    private Integer pageSize = 50;

    @LetterOrNumber
    @Length(max = 15)
    private String query = null;
}
