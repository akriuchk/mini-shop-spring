package org.akriuchk.minishop.dto;

import lombok.Data;

import java.util.Set;

@Data
public class ProductDto {

    private long id;
    private String name;
    private boolean isSmallAvailable;
    private boolean isMiddleAvailable;
    private boolean isEuroAvailable;
    private boolean isDuoAvailable;
    private Set<Long> images;
    private String linenCatalog;
}
