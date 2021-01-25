package org.akriuchk.minishop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private long id;
    @NotEmpty
    private String name;
    private boolean isSmallAvailable;
    private boolean isMiddleAvailable;
    private boolean isEuroAvailable;
    private boolean isDuoAvailable;
    private List<ImageDto> images;

    @NotEmpty
    private String category;
}
