package org.akriuchk.minishop.service.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.akriuchk.minishop.model.Linen;
import org.akriuchk.minishop.model.LinenCatalog;
import org.akriuchk.minishop.repository.LinenCatalogRepository;
import org.akriuchk.minishop.repository.LinenRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.*;

import static org.akriuchk.minishop.service.parser.ExcelUtils.*;


@Slf4j
@RequiredArgsConstructor
public class VasilisaBjazParser extends AbstractParser {
    private final LinenCatalogRepository linenCatalogRepository;
    private final LinenRepository linenRepository;

    private Map<String, Boolean> checkMap = new HashMap<String, Boolean>() {{
        put("КПБ\" Василиса\"".toLowerCase(), false);
        put("нэкст".toLowerCase(), false);
        put("Ботаническая гравюра".toLowerCase(), false);
        put("ЛЕТО,МОРЕ".toLowerCase(), false);
    }};

    private static final String ZAKAZ = "заказ";
    private static final String VASILISA = "КПБ\" Василиса\"";


    private int firstCellNum = 0;

    @Override
    public boolean isFileValid(Workbook workbook) {
        workbook.forEach(sheet -> {
            if (sheet.getPhysicalNumberOfRows() == 0) {
                return;
            }
            firstCellNum = determineFirstColumn(sheet.getRow(sheet.getFirstRowNum()));
            sheet.rowIterator().forEachRemaining(this::checkRow);
        });
        return checkMap.values().stream().allMatch(Boolean::booleanValue);
    }

    private void checkRow(Row row) {
        row.forEach(cell -> {
            if (cell == null) {
                return;
            }
            String parsedCellValue = getStringValue(cell).toLowerCase().trim();
            checkMap.computeIfPresent(parsedCellValue, (k, v) -> true);
        });
    }

    @Override
    public List<LinenCatalog> parse(Workbook book) {
        LinkedList<LinenCatalog> catalogs = new LinkedList<>();
        Set<Linen> updatedLinens = new HashSet<>();
        book.forEach(sheet -> {
            if (sheet.getPhysicalNumberOfRows() == 0) {
                return;
            }
            if (rowContainsValue(sheet.getRow(sheet.getFirstRowNum()), ZAKAZ)) {
                firstCellNum = determineFirstColumn(sheet.getRow(sheet.getFirstRowNum() + 1));
            } else {
                firstCellNum = determineFirstColumn(sheet.getRow(sheet.getFirstRowNum()));
            }
            sheet.rowIterator().forEachRemaining(row -> {
                if (rowContainsValue(row, ZAKAZ)) {
                    return;
                }

                if (hasMergedCellsInRow(sheet, row) || rowContainsValue(row, VASILISA)) {
                    String linenCatalogName = getStringValue(row, firstCellNum + 1);
                    if (linenCatalogName.isEmpty()) {
                        return;
                    }
                    log.info("Working with '{}'", linenCatalogName);

                    LinenCatalog catalog = linenCatalogRepository.findByName(linenCatalogName).orElseGet(() -> {
                        LinenCatalog newCatalog = new LinenCatalog();
                        newCatalog.setName(linenCatalogName);
                        return newCatalog;
                    });
                    catalogs.add(catalog);

                } else if (row.getRowNum() != 0 && row.getPhysicalNumberOfCells() != 0 && !getStringValue(row, firstCellNum).isEmpty()) {
                    Linen linen = rowProceed(firstCellNum, row);
                    catalogs.getLast().getLinens().add(linen);
                    linen.setLinenCatalog(catalogs.getLast());
                    updatedLinens.add(linen);

                } else {
                    log.error("Undefined row: {}", row.getCell(firstCellNum));
                }
            });
        });

        processNotUpdatedLinens(catalogs, updatedLinens);

        return catalogs;
    }

    @Override
    void determineAvailability(Linen linen, Row row, int firstCellNum) {
        log.info("Processing {}", linen.getName());
        String smallSizeCellContent = getStringValue(row, firstCellNum + 2).toLowerCase().trim();
        String middleSizeCellContent = getStringValue(row, firstCellNum + 3).toLowerCase().trim();
        String euroSizeCellContent = getStringValue(row, firstCellNum + 4).toLowerCase().trim();
        String duoSizeCellContent = getStringValue(row, firstCellNum + 5).toLowerCase().trim();


        if (smallSizeCellContent.isEmpty()) {
            linen.setSmallAvailable(false);
        } else {
            linen.setSmallAvailable(true);
        }

        if (middleSizeCellContent.isEmpty()) {
            linen.setMiddleAvailable(false);
        } else {
            linen.setMiddleAvailable(true);
        }

        if (euroSizeCellContent.isEmpty()) {
            linen.setEuroAvailable(false);
        } else {
            linen.setEuroAvailable(true);
        }

        if (duoSizeCellContent.isEmpty()) {
            linen.setDuoAvailable(false);
        } else {
            linen.setDuoAvailable(true);
        }
    }


    @Override
    public LinenCatalogRepository getLinenCatalogRepository() {
        return linenCatalogRepository;
    }

    @Override
    public LinenRepository getLinenRepository() {
        return linenRepository;
    }
}
