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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.akriuchk.minishop.service.parser.ExcelUtils.*;

@Slf4j
public class VasilisaBjazParser extends AbstractParser {
    private final LinkedList<Category> catalogs;
    private final Map<Product, Boolean> updatedAndNewProducts;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;

    public static final String VASI_BJAZ_NAME = "КПБ\" Василиса\"";

    public VasilisaBjazParser(CategoryService categoryService, ProductService productService,
                              CategoryMapper categoryMapper, ProductMapper productMapper) {
        super(categoryService, productService);
        this.catalogs = new LinkedList<>();
        this.updatedAndNewProducts = new LinkedHashMap<>();
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
    }

    @Override
    public List<ImportResultDto> parse(Workbook book) {
        catalogs.add(categoryService.getByName(VASI_BJAZ_NAME));

        book.forEach(sheet -> {
            if (sheet.getPhysicalNumberOfRows() == 0) {
                return;
            }

            int firstCellIdxSheet = defineFirstColumnIdx(sheet);

            sheet.rowIterator().forEachRemaining(row -> {
                if (isHeader(sheet, row) || emptyOrHeaders(row)) {
                    //this is header
                    //skip it

                } else if (row.getRowNum() != 0
                        && row.getPhysicalNumberOfCells() != 0
                        && !emptyOrHeaders(row)
                ) {
                    log.info("Product row: {}", getStringValue(row, firstCellIdxSheet));
                    //this is product
                    Product product = extractProduct(firstCellIdxSheet, row);
                    catalogs.getLast().getProducts().add(product);
                    product.setCategory(catalogs.getLast());

                } else {
                    extractError(row);
                }
            });
        });
//        processNotUpdatedProducts(catalogs, updatedProducts);

        return createReport();
    }

    private int defineFirstColumnIdx(Sheet sheet) {
        AtomicInteger firstColumnIdx = new AtomicInteger(-1);

        sheet.rowIterator().forEachRemaining(row -> {
            if (row.getRowNum() == sheet.getFirstRowNum()) {
                firstColumnIdx.set(determineFirstColumn(row));
            }

            if (emptyOrHeaders(row)) return;

            int firstCellIdxRow = determineFirstColumn(row);
            if (firstColumnIdx.get() != firstCellIdxRow) {
                firstColumnIdx.set(firstCellIdxRow);
                log.info("New idx: {}", firstCellIdxRow);
            }
        });
        return firstColumnIdx.get();
    }

    private Product extractProduct(int firstCellNum, Row row) {
        String parsedProductName = getProductName(firstCellNum, row);

        AtomicBoolean isNew = new AtomicBoolean(false);

        Product product = productService.findByName(parsedProductName).orElseGet(() -> {
            Product newProduct = new Product();
            newProduct.setName(parsedProductName);
            isNew.set(true);
            return newProduct;
        });

        updateAvailability(product, isNew, row, firstCellNum);

        updatedAndNewProducts.put(product, isNew.get());
        return product;
    }

    private void extractError(Row row) {
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
                });
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

    void updateAvailability(Product product, AtomicBoolean isNew, Row row, int firstCellNum) {

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

    /**
     * Shows update from not available to available
     *
     * @param previouslyAvailable prev state
     * @param isNowAvailable      new state
     * @return is product newly available
     */
    private boolean nowAvailable(Supplier<Boolean> previouslyAvailable, boolean isNowAvailable) {
        if (!previouslyAvailable.get()) {
            return isNowAvailable;
        }
        return false;
    }

    private boolean emptyOrHeaders(Row row) {
        //ignore empty row
        if (isRowEmpty(row)) {
            return true;
        }

        return rowContainsValue(row, "рис") || rowContainsValue(row, "евро");
    }

    private boolean isHeader(Sheet sheet, Row row) {
        return hasMergedCellsInRow(sheet, row);
    }

    private String getProductName(int firstCellNum, Row row) {
        Cell cell = row.getCell(firstCellNum);
        if (cell == null) {
            String error = String.format("Cell in row '%s':%s not found", row.getSheet().getSheetName(), row.getRowNum());
            throw new RuntimeException(error);
        }
        return getStringValue(row, firstCellNum);
    }

    private boolean parseAvailability(Row row, int idx) {
        String NO = "нет";
        return !getStringValue(row, idx).toLowerCase().contains(NO);
    }

    @Override
    public void isFileValid(Workbook ignored) {
        //just default parser
    }
}
