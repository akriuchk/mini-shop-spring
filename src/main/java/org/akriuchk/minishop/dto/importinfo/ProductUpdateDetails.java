package org.akriuchk.minishop.dto.importinfo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.akriuchk.minishop.dto.ImageDto;
import org.akriuchk.minishop.dto.ProductDto;

import javax.validation.constraints.NotEmpty;
import java.util.List;


/**
 * ProductUpdateDetails
 */
@Data
@NoArgsConstructor
public class ProductUpdateDetails extends ProductDto {

    public ProductUpdateDetails(long id, @NotEmpty String name, boolean isSmallAvailable, boolean isMiddleAvailable, boolean isEuroAvailable, boolean isDuoAvailable, List<ImageDto> images, @NotEmpty String category, Boolean isNew) {
        super(id, name, isSmallAvailable, isMiddleAvailable, isEuroAvailable, isDuoAvailable, images, category);
        this.isNew = isNew;
    }

    public ProductUpdateDetails(ProductDto productDto, Boolean isNew) {
        super(productDto.getId(), productDto.getName(),
                productDto.isSmallAvailable(), productDto.isMiddleAvailable(),
                productDto.isEuroAvailable(), productDto.isDuoAvailable(),
                productDto.getImages(), productDto.getCategory()
        );
        this.isNew = isNew;
    }

    private Boolean isNew;

}