package org.akriuchk.minishop.serialize;

import lombok.Data;

@Data
public class ImageDto {
    private long id;
    private String name;
    private boolean isLinked = true;
    private Long linen;
}
