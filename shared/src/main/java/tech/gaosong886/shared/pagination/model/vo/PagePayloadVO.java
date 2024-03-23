package tech.gaosong886.shared.pagination.model.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagePayloadVO<T> {
    private Integer page;
    private Integer pageSize;
    private List<T> data;
    private Long totalItems;
    private Integer totalPages;
}
