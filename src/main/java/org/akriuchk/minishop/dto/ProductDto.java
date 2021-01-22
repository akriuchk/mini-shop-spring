package org.akriuchk.minishop.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class ProductDto {

    private long id;
    @NotEmpty
    private String name;
    private boolean isSmallAvailable;
    private boolean isMiddleAvailable;
    private boolean isEuroAvailable;
    private boolean isDuoAvailable;
    private Set<Long> images;

    @NotEmpty
    private String linenCatalog;
}
