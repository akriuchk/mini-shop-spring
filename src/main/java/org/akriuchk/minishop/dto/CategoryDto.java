package org.akriuchk.minishop.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class CategoryDto {

    @NotEmpty
    private String name;
    @NotEmpty
    private String displayName;
    private Set<ProductDto> products;
}
