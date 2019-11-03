package org.akriuchk.minishop.service.parser;

import lombok.extern.slf4j.Slf4j;
import org.akriuchk.minishop.model.Linen;
import org.akriuchk.minishop.model.LinenCatalog;
import org.akriuchk.minishop.repository.LinenCatalogRepository;
import org.akriuchk.minishop.repository.LinenRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

import static org.akriuchk.minishop.service.parser.ExcelUtils.getStringValue;

@Slf4j
public abstract class AbstractParser {

    public abstract List<LinenCatalog> parse(Workbook book);
    public abstract boolean isFileValid(Workbook workbook);


    Linen rowProceed(int firstCellNum, Row row) {
        Cell cell = row.getCell(firstCellNum);
        if (cell == null) {
            String error = String.format("Cell in row '%s':%s not found", row.getSheet().getSheetName(), row.getRowNum());
            throw new RuntimeException(error);
        }
        String parsedLinenName = getStringValue(row, firstCellNum);
        Linen linen = getLinenRepository().findByName(parsedLinenName).orElseGet(() -> {
            Linen l = new Linen();
            l.setName(parsedLinenName);
            return l;
        });
        determineAvailability(linen, row, firstCellNum);
        return linen;
    }

    abstract void determineAvailability(Linen linen, Row row, int firstCellNum);
    abstract LinenRepository getLinenRepository();
    abstract LinenCatalogRepository getLinenCatalogRepository();

}
