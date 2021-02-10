package org.akriuchk.minishop.service.parser;

import lombok.extern.slf4j.Slf4j;
import org.akriuchk.minishop.converter.CategoryMapper;
import org.akriuchk.minishop.converter.ProductMapper;
import org.akriuchk.minishop.dto.importinfo.ImportResultDto;
import org.akriuchk.minishop.dto.importinfo.ProductUpdateDetails;
import org.akriuchk.minishop.model.Category;
import org.akriuchk.minishop.model.Product;
import org.akriuchk.minishop.service.CategoryService;
import org.akriuchk.minishop.service.ProductService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.akriuchk.minishop.service.parser.ExcelUtils.*;

@Slf4j
public class DefaultParser extends AbstractParser {
    private final LinkedList<Category> catalogs;
    private final Map<Product, Boolean> updatedAndNewProducts;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;

    private static final Category UNCATEGORIZED = new Category();

    public DefaultParser(CategoryService categoryService, ProductService productService,
                         CategoryMapper categoryMapper, ProductMapper productMapper) {
        super(categoryService, productService);
        this.catalogs = new LinkedList<>();
        this.updatedAndNewProducts = new HashMap<>();
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
    }

    @Override
    public List<ImportResultDto> parse(Workbook book) {
        book.forEach(sheet -> {
            if (sheet.getPhysicalNumberOfRows() == 0) {
                return;
            }
            int firstCellNum = determineFirstColumn(sheet.getRow(sheet.getFirstRowNum()));
            sheet.rowIterator().forEachRemaining(row -> {
                if (isHeader(sheet, row)) {
                    //this is header
                    String categoryName = row.getCell(firstCellNum).getStringCellValue().trim();
                    log.info("Category row: '{}'", categoryName);
                    Optional<Category> category = categoryService.findByName(categoryName);

                    if (!category.isPresent()) {
                        catalogs.add(UNCATEGORIZED);
                        log.error("{} not found! Add it first to catalog list", categoryName);
                    }
                    category.ifPresent(catalogs::add);

                } else if (row.getRowNum() != 0
                        && row.getPhysicalNumberOfCells() != 0
                        && !getStringValue(row, firstCellNum).isEmpty()
                ) {
                    log.info("Product row: {}", getStringValue(row, firstCellNum));
                    //this is product
                    Product product = rowProceed(firstCellNum, row);
                    catalogs.getLast().getProducts().add(product);
                    product.setCategory(catalogs.getLast());

                } else {
                    try {
                        StringBuilder sb = new StringBuilder(" ");
                        sb.append("Undefined row: ");
                        sb.append(row.getRowNum());
                        sb.append("; Content:");
                        if (row.getZeroHeight() || row.getRowStyle().getHidden()) {
                            sb.append("empty hidden row");
                        } else {
                            for (int cellNum = row.getFirstCellNum(); cellNum < cellNum + row.getPhysicalNumberOfCells(); cellNum++) {
                                sb.append(getStringValue(row.getCell(cellNum)).trim());
                            }
                        }
                        log.error(sb.toString());

                    } catch (NullPointerException e) {
                        log.error("Undefined row {}", row.getRowNum());
                    }
                }
            });
        });
        processNotUpdatedProducts(catalogs, updatedAndNewProducts.keySet());

        return createReport();
    }

    private List<ImportResultDto> createReport() {
       return updatedAndNewProducts.entrySet().stream()
                .collect(Collectors.groupingBy(c -> c.getKey().getCategory()))
                .entrySet().stream()
                .map(e -> {
                    ImportResultDto importResultDto = new ImportResultDto();
                    importResultDto.setCategory(categoryMapper.toShortDto(e.getKey()));
                    importResultDto.setUpdates(createUpdates(e.getValue()));
                    return importResultDto;
                })
                .collect(Collectors.toList());
    }

    private List<ProductUpdateDetails> createUpdates(List<Map.Entry<Product, Boolean>> entryList) {
        return entryList.stream()
                .map(e -> new ProductUpdateDetails(
                        productMapper.toDto(e.getKey()),
                        e.getValue()))
                .collect(Collectors.toList());
    }


    private boolean isHeader(Sheet sheet, Row row) {
        return hasMergedCellsInRow(sheet, row);
    }

    Product rowProceed(int firstCellNum, Row row) {
        Cell cell = row.getCell(firstCellNum);
        if (cell == null) {
            String error = String.format("Cell in row '%s':%s not found", row.getSheet().getSheetName(), row.getRowNum());
            throw new RuntimeException(error);
        }

        String parsedProductName = getStringValue(row, firstCellNum);
        AtomicBoolean isNew = new AtomicBoolean(false);

        Product product = productService.findByName(parsedProductName).orElseGet(() -> {
            Product newProduct = new Product();
            newProduct.setName(parsedProductName);
            isNew.set(true);
            return newProduct;
        });

        updateAvailability(isNew, product, row, firstCellNum);

        updatedAndNewProducts.put(product, isNew.get());
        return product;
    }

    void updateAvailability(AtomicBoolean isNew, Product product, Row row, int firstCellNum) {

        log.info("Processing availability of '{}'", product.getName());
        boolean smallSizeNewState = parseAvailability(row, firstCellNum + 2);
        boolean middleSizeNewState = parseAvailability(row, firstCellNum + 3);
        boolean euroSizeNewState = parseAvailability(row, firstCellNum + 4);
        boolean duoSizeNewState = parseAvailability(row, firstCellNum + 5);

        if (nowAvailable(product::isSmallAvailable, smallSizeNewState)
                || nowAvailable(product::isMiddleAvailable, middleSizeNewState)
                || nowAvailable(product::isEuroAvailable, euroSizeNewState)
                || nowAvailable(product::isDuoAvailable, duoSizeNewState)
        ) {
            isNew.set(true);
        }

        product.setSmallAvailable(smallSizeNewState);
        product.setMiddleAvailable(middleSizeNewState);
        product.setEuroAvailable(euroSizeNewState);
        product.setDuoAvailable(duoSizeNewState);
    }

    private boolean parseAvailability(Row row, int idx) {
        String NO = "нет";
        return !getStringValue(row, idx).toLowerCase().contains(NO);
    }

    /**
     * Shows update from not available to available
     *
     * @param previouslyAvailable prev state
     * @param isNowAvailable      new state
     * @return change of state
     */
    private boolean nowAvailable(Supplier<Boolean> previouslyAvailable, boolean isNowAvailable) {
        if (!previouslyAvailable.get()) {
            return isNowAvailable;
        }
        return false;
    }




    @Override
    public void isFileValid(Workbook ignored) {
        //just default parser
    }
}
