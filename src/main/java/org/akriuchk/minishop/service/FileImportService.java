package org.akriuchk.minishop.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.akriuchk.minishop.model.Linen;
import org.akriuchk.minishop.model.LinenCatalog;
import org.akriuchk.minishop.repository.LinenCatalogRepository;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FileImportService {
    private final LinenCatalogRepository linenCatalogRepository;

    public List<LinenCatalog> parse(MultipartFile file) {
        LinkedList<LinenCatalog> catalogs = new LinkedList<>();
        try {
            Workbook book = WorkbookFactory.create(file.getInputStream());
            book.forEach(sheet -> {
                if (sheet.getPhysicalNumberOfRows() == 0) {
                    return;
                }
                int firstCellNum = determineFirstColumn(sheet.getRow(sheet.getFirstRowNum()));
                sheet.rowIterator().forEachRemaining(row -> {
                    if (hasMergedCellsInRow(sheet, row)) {
                        LinenCatalog catalog = new LinenCatalog();
                        catalogs.add(catalog);
                        String linenCatalog = row.getCell(firstCellNum).getStringCellValue().trim();
                        catalog.setName(linenCatalog);
                    }

                    if (row.getRowNum() != 0 && row.getPhysicalNumberOfCells() != 0 && !getStringValue(row, firstCellNum).isEmpty()) {
                        Linen linen = rowProceed(firstCellNum, row);
                        catalogs.getLast().getLinens().add(linen);
                        linen.setLinenCatalog(catalogs.getLast());
                    }
                });
            });
            linenCatalogRepository.saveAll(catalogs);
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException("Error in file reading", e);
        }
        return catalogs;
    }

    private boolean hasMergedCellsInRow(Sheet sheet, Row row) {
        return sheet.getMergedRegions().stream().anyMatch(region ->
                region.containsRow(row.getRowNum())
        );
    }

    private Linen rowProceed(int firstCellNum, Row row) {
        Linen linen = new Linen();
        Cell cell = row.getCell(firstCellNum);
        if (cell == null) {
            String error = String.format("Cell in row '%s':%s not found", row.getSheet().getSheetName(), row.getRowNum());
            throw new RuntimeException(error);
        }
        linen.setName(getStringValue(row, firstCellNum));
        determineAvailability(linen, row, firstCellNum);
        return linen;
    }

    private int determineFirstColumn(Row row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            String stringValue = getStringValue(row, i);
            if (!stringValue.isEmpty()) {
                return i;
            }
        }
        String error = String.format("First cell for row %s/%s not found", row.getSheet().getSheetName(), row.getRowNum());
        throw new RuntimeException(error);
    }

    private static final String NO = "нет";
    private static final String YES = "есть";

    private void determineAvailability(Linen linen, Row row, int firstCellNum) {
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


    private String getStringValue(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) {
            return "";
        }

        switch (cell.getCellTypeEnum()) {
            case BLANK:
                return "";
            case _NONE:
                return "";
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
        }
        return "";
    }

}
