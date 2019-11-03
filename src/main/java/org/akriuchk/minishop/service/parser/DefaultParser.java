package org.akriuchk.minishop.service.parser;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.akriuchk.minishop.model.Linen;
import org.akriuchk.minishop.model.LinenCatalog;
import org.akriuchk.minishop.repository.LinenCatalogRepository;
import org.akriuchk.minishop.repository.LinenRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.akriuchk.minishop.service.parser.ExcelUtils.*;

@Slf4j
@AllArgsConstructor
public class DefaultParser extends AbstractParser {

    private final LinenCatalogRepository linenCatalogRepository;
    private final LinenRepository linenRepository;

    @Override
    public boolean isFileValid(Workbook workbook) {
        return false;
    }

    @Override
    public List<LinenCatalog> parse(Workbook book) {
        LinkedList<LinenCatalog> catalogs = new LinkedList<>();
        book.forEach(sheet -> {
            if (sheet.getPhysicalNumberOfRows() == 0) {
                return;
            }
            int firstCellNum = determineFirstColumn(sheet.getRow(sheet.getFirstRowNum()));
            sheet.rowIterator().forEachRemaining(row -> {
                if (hasMergedCellsInRow(sheet, row)) {
                    String linenCatalogName = row.getCell(firstCellNum).getStringCellValue().trim();
                    log.info("Working with '{}'", linenCatalogName);
                    if (linenCatalogName.contains("ЗАКАЗ")) {
                        return;
                    }
                    Optional<LinenCatalog> linenCatalogRequest = linenCatalogRepository.findByName(linenCatalogName);

                    if (!linenCatalogRequest.isPresent()) {
                        log.error("{} not found! Add it first to catalog list", linenCatalogName);
                    }
                    catalogs.add(linenCatalogRequest.get());
                } else if (row.getRowNum() != 0 && row.getPhysicalNumberOfCells() != 0 && !getStringValue(row, firstCellNum).isEmpty()) {
                    Linen linen = rowProceed(firstCellNum, row);
                    catalogs.getLast().getLinens().add(linen);
                    linen.setLinenCatalog(catalogs.getLast());
                } else {
                    log.error("Undefined row: {}", row.getCell(firstCellNum));
                }
            });
        });
        return catalogs;
    }

    @Override
    void determineAvailability(Linen linen, Row row, int firstCellNum) {
        String NO = "нет";

        log.info("Processing {}", linen.getName());
        String smallSizeCellContent = getStringValue(row, firstCellNum + 2).toLowerCase().trim();
        String middleSizeCellContent = getStringValue(row, firstCellNum + 3).toLowerCase().trim();
        String euroSizeCellContent = getStringValue(row, firstCellNum + 4).toLowerCase().trim();
        String duoSizeCellContent = getStringValue(row, firstCellNum + 5).toLowerCase().trim();


        if (smallSizeCellContent.contains(NO)) {
            linen.setSmallAvailable(false);
        } else {
            linen.setSmallAvailable(true);
        }

        if (middleSizeCellContent.contains(NO)) {
            linen.setMiddleAvailable(false);
        } else {
            linen.setMiddleAvailable(true);
        }

        if (euroSizeCellContent.contains(NO)) {
            linen.setEuroAvailable(false);
        } else {
            linen.setEuroAvailable(true);
        }

        if (duoSizeCellContent.contains(NO)) {
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
