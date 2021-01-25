package org.akriuchk.minishop.dto.importinfo;

import lombok.Data;
import org.akriuchk.minishop.dto.CategoryDto;

import java.util.List;

/**
 * ImportResult
 */
@Data
public class ImportResultDto {
    private CategoryDto category;
    private List<ProductUpdateDetails> updates;
}