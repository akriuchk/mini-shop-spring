package org.akriuchk.minishop.service.parser;

import lombok.AllArgsConstructor;
import org.akriuchk.minishop.dto.importinfo.ImportResultDto;
import org.akriuchk.minishop.service.CategoryService;
import org.akriuchk.minishop.service.ProductService;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

@AllArgsConstructor
public abstract class AbstractParser {
    CategoryService categoryService;
    ProductService productService;

    public abstract List<ImportResultDto> parse(Workbook book);

    public abstract void isFileValid(Workbook book);
}
