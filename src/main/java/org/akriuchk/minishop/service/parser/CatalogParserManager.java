package org.akriuchk.minishop.service.parser;

import lombok.RequiredArgsConstructor;
import org.akriuchk.minishop.converter.CategoryMapper;
import org.akriuchk.minishop.converter.ProductMapper;
import org.akriuchk.minishop.dto.importinfo.ImportResultDto;
import org.akriuchk.minishop.model.SupportedCategories;
import org.akriuchk.minishop.rest.ValidationException;
import org.akriuchk.minishop.service.CategoryService;
import org.akriuchk.minishop.service.ProductService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogParserManager {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;

    //todo output: category, product with diff: new products(!), changes in availability

    public List<ImportResultDto> importNewCatalog(MultipartFile importFile, SupportedCategories category) {
        Workbook book;
        try {
            book = WorkbookFactory.create(importFile.getInputStream());
        } catch (IOException | InvalidFormatException e) {
            throw new ValidationException("Could not read excel file", e);
        }
        AbstractParser parser = getParserFor(category);
        parser.isFileValid(book);
        List<ImportResultDto> importResultDto = parser.parse(book);

        return importResultDto;

    }

    private AbstractParser getParserFor(SupportedCategories catalog) {
        AbstractParser parser;
        if (catalog == SupportedCategories.DEFAULT) {
            parser = new DefaultParser(categoryService, productService, categoryMapper, productMapper);
        } else if (catalog == SupportedCategories.VASILISA_BYAZ) {
//            parser = new VasilisaBjazParser(categoryService, productService);

            throw new ValidationException("Parser for catalog is not supported: " + catalog);
        } else if (catalog == SupportedCategories.DISNEY) {
//            parser = new DisneyParser(categoryService, productService);

            throw new ValidationException("Parser for catalog is not supported: " + catalog);
        } else {
            throw new ValidationException("Parser for catalog is not supported: " + catalog);
        }
        return parser;
    }
}
