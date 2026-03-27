package com.api.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Utility to read test data from Excel (.xlsx) files.
 * Returns data as List<Map<String, String>> where each map represents a row
 * with column headers as keys — compatible with data-driven step definitions.
 */
public final class ExcelReader {

    private static final Logger LOG = LogManager.getLogger(ExcelReader.class);

    private ExcelReader() {
    }

    /**
     * Reads all rows from a given sheet in an Excel file.
     *
     * @param filePath  path relative to classpath (e.g., "testdata/users.xlsx")
     * @param sheetName name of the sheet to read
     * @return list of maps, each map representing a row with header→value pairs
     */
    public static List<Map<String, String>> readExcelData(String filePath, String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();

        try (InputStream inputStream = ExcelReader.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new RuntimeException("Excel file not found on classpath: " + filePath);
            }

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in file: " + filePath);
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("No header row found in sheet: " + sheetName);
            }

            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValueAsString(cell));
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> rowData = new LinkedHashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData.put(headers.get(j), getCellValueAsString(cell));
                }
                data.add(rowData);
            }

            workbook.close();
            LOG.info("Read {} rows from sheet '{}' in file '{}'", data.size(), sheetName, filePath);

        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file: " + filePath, e);
        }

        return data;
    }

    /**
     * Reads a specific row by row index (0-based, excluding header).
     */
    public static Map<String, String> readRow(String filePath, String sheetName, int rowIndex) {
        List<Map<String, String>> allData = readExcelData(filePath, sheetName);
        if (rowIndex >= allData.size()) {
            throw new IndexOutOfBoundsException(
                    "Row index " + rowIndex + " out of bounds (total rows: " + allData.size() + ")");
        }
        return allData.get(rowIndex);
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getDateCellValue().toString();
                }
                double numValue = cell.getNumericCellValue();
                if (numValue == Math.floor(numValue)) {
                    yield String.valueOf((long) numValue);
                }
                yield String.valueOf(numValue);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }
}
