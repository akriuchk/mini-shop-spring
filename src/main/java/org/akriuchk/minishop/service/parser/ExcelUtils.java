package org.akriuchk.minishop.service.parser;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Optional;

@Slf4j
public class ExcelUtils {

    public static int determineFirstColumn(Row row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            String stringValue = getStringValue(row, i);
            if (!stringValue.isEmpty()) {
                return i;
            }
        }
        String error = String.format("First cell for row '%s/%s' not found", row.getSheet().getSheetName(), row.getRowNum());
        throw new RuntimeException(error);
    }

    public static boolean hasMergedCellsInRow(Sheet sheet, Row row) {
        return sheet.getMergedRegions().stream().anyMatch(region ->
                region.containsRow(row.getRowNum())
        );
    }

    public static boolean rowContainsValue(Row row, String textToCheck) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            String parsedCellValue = getStringValue(row, i).toLowerCase().trim();
            if (parsedCellValue.contains(textToCheck.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRowEmpty(Row row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            String stringValue = getStringValue(row, i);
            if (!stringValue.isEmpty()) {
                return false;
            }
        }
        return true;
    }


    public static String getStringValue(Row row, int index) {
        Cell cell = row.getCell(index);
        return getStringValue(cell);
    }

    public static String getStringValue(Cell cell) {
        return Optional.ofNullable(cell).map(c -> {
            switch (c.getCellTypeEnum()) {
                case STRING:
                    return c.getStringCellValue().trim();
                case NUMERIC:
                    return String.valueOf(c.getNumericCellValue());
                default:
                    return "";
            }
        }).orElse("");
    }

    public static void extractError(Row row) {
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
}
