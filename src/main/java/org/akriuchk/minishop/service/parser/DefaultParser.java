package org.akriuchk.minishop.service.parser;

import lombok.extern.slf4j.Slf4j;
import org.akriuchk.minishop.model.Category;
import org.akriuchk.minishop.model.Product;
import org.akriuchk.minishop.service.CategoryService;
import org.akriuchk.minishop.service.ProductService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.*;

import static org.akriuchk.minishop.service.parser.ExcelUtils.*;

@Slf4j
public class DefaultParser extends AbstractParser {
    public DefaultParser(CategoryService categoryService, ProductService productService) {
        super(categoryService, productService);
    }

    @Override
    public void parse(Workbook book) {
        LinkedList<Category> catalogs = new LinkedList<>();
        Set<Product> updatedProducts = new HashSet<>();
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
                    updatedProducts.add(product);
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
        processNotUpdatedProducts(catalogs, updatedProducts);

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
        Product product = productService.findByName(parsedProductName).orElseGet(() -> {
            Product l = new Product();
            l.setName(parsedProductName);
            return l;
        });
        determineAvailability(product, row, firstCellNum);
        return product;
    }

    void determineAvailability(Product product, Row row, int firstCellNum) {
        String NO = "нет";

        log.info("Processing {}", product.getName());
        String smallSizeCellContent = getStringValue(row, firstCellNum + 2).toLowerCase().trim();
        String middleSizeCellContent = getStringValue(row, firstCellNum + 3).toLowerCase().trim();
        String euroSizeCellContent = getStringValue(row, firstCellNum + 4).toLowerCase().trim();
        String duoSizeCellContent = getStringValue(row, firstCellNum + 5).toLowerCase().trim();

        product.setSmallAvailable(!smallSizeCellContent.contains(NO));
        product.setMiddleAvailable(!middleSizeCellContent.contains(NO));
        product.setEuroAvailable(!euroSizeCellContent.contains(NO));
        product.setDuoAvailable(!duoSizeCellContent.contains(NO));
    }


    /**
     * if linen was not in file then set availability to false
     *
     * @param catalogs      prevois catalogs
     * @param updatedLinens new catalogs
     */
    void processNotUpdatedProducts(List<Category> catalogs, Set<Product> updatedLinens) {
        catalogs.stream().flatMap(c -> c.getProducts().stream())
                .filter(l -> !updatedLinens.contains(l))
                .forEach(l -> {
                    l.setSmallAvailable(false);
                    l.setMiddleAvailable(false);
                    l.setDuoAvailable(false);
                    l.setEuroAvailable(false);
                });
    }


    @Override
    public void isFileValid(Workbook ignored) {
        //just default parser
    }
}
