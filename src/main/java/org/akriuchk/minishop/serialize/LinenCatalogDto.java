package org.akriuchk.minishop.serialize;

import lombok.Builder;
import lombok.Singular;

import java.util.Collection;

@Builder
public class LinenCatalogDto {
    private long id;
    private String name;
    private String displayName;
    @Singular
    private Collection<Long> linens;
}
