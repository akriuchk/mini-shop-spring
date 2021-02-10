package org.akriuchk.minishop.service.parser;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.akriuchk.minishop.dto.importinfo.ImportResultDto;
import org.akriuchk.minishop.model.Category;
import org.akriuchk.minishop.model.Product;
import org.akriuchk.minishop.service.CategoryService;
import org.akriuchk.minishop.service.ProductService;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Set;

@Slf4j
@AllArgsConstructor
public abstract class AbstractParser {
    CategoryService categoryService;
    ProductService productService;

    public abstract List<ImportResultDto> parse(Workbook book);

    public abstract void isFileValid(Workbook book);


    /**
     * if linen was not in file then set availability to false
     *
     * @param categories      previous categories
     * @param updatedProducts new categories
     */
    void processNotUpdatedProducts(List<Category> categories, Set<Product> updatedProducts) {
        categories.stream().flatMap(category -> category.getProducts().stream())
                .filter(product -> !updatedProducts.contains(product))
                .forEach(product -> {
                    product.setSmallAvailable(false);
                    product.setMiddleAvailable(false);
                    product.setDuoAvailable(false);
                    product.setEuroAvailable(false);
                    log.info("Product '{}' is out", product.getName());
                });
    }
}
