package org.akriuchk.minishop.serialize;

import lombok.Builder;

import java.util.Set;

@Builder
public class LinenDto {
    private long id;
    private String name;
    private boolean isSmallAvailable;
    private boolean isMiddleAvailable;
    private boolean isEuroAvailable;
    private boolean isDuoAvailable;
    private Set<Long> images;
    private String linenCatalog;
}
