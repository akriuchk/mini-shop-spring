package org.akriuchk.minishop.service.parser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ExcelUtils {

    public static int determineFirstColumn(Row row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            String stringValue = getStringValue(row, i);
            if (!stringValue.isEmpty()) {
                return i;
            }
        }
        String error = String.format("First cell for row %s/%s not found", row.getSheet().getSheetName(), row.getRowNum());
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


    public static String getStringValue(Row row, int index) {
        Cell cell = row.getCell(index);
        return getStringValue(cell);
    }

    public static String getStringValue(Cell cell) {
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
