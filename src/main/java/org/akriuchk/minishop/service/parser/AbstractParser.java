package org.akriuchk.minishop.service.parser;

import lombok.AllArgsConstructor;
import org.akriuchk.minishop.service.CategoryService;
import org.akriuchk.minishop.service.ProductService;
import org.apache.poi.ss.usermodel.Workbook;

@AllArgsConstructor
public abstract class AbstractParser {
    CategoryService categoryService;
    ProductService productService;

    public abstract void parse(Workbook book);

    public abstract void isFileValid(Workbook book);
}
