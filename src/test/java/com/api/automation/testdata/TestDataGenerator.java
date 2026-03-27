package com.api.automation.testdata;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * One-time utility to generate the test data Excel file.
 * Run this class's main method to create src/test/resources/testdata/posts.xlsx.
 *
 * Usage: Right-click → Run As → Java Application
 */
public class TestDataGenerator {

    public static void main(String[] args) throws IOException {
        Path dir = Path.of("src/test/resources/testdata");
        Files.createDirectories(dir);

        Workbook workbook = new XSSFWorkbook();

        // ── Sheet: CreatePosts ──────────────────────────────────────────
        Sheet createSheet = workbook.createSheet("CreatePosts");
        Row header = createSheet.createRow(0);
        header.createCell(0).setCellValue("title");
        header.createCell(1).setCellValue("body");

        String[][] createData = {
                {"Introduction to Java", "Java is a versatile programming language"},
                {"REST API Best Practices", "Follow these patterns for clean API design"},
                {"Cucumber BDD Guide", "BDD bridges the gap between business and dev"},
                {"CI/CD Pipeline Setup", "Automate your build and deployment process"},
                {"Test Automation Strategy", "A solid strategy ensures quality at scale"}
        };
        for (int i = 0; i < createData.length; i++) {
            Row row = createSheet.createRow(i + 1);
            row.createCell(0).setCellValue(createData[i][0]);
            row.createCell(1).setCellValue(createData[i][1]);
        }

        // ── Sheet: UpdatePosts ──────────────────────────────────────────
        Sheet updateSheet = workbook.createSheet("UpdatePosts");
        Row updateHeader = updateSheet.createRow(0);
        updateHeader.createCell(0).setCellValue("postId");
        updateHeader.createCell(1).setCellValue("title");
        updateHeader.createCell(2).setCellValue("body");

        String[][] updateData = {
                {"1", "Updated Java Guide", "Updated content for Java fundamentals"},
                {"2", "Updated REST Patterns", "Revised best practices for REST APIs"},
                {"3", "Updated BDD Techniques", "New approaches to behavior-driven testing"}
        };
        for (int i = 0; i < updateData.length; i++) {
            Row row = updateSheet.createRow(i + 1);
            row.createCell(0).setCellValue(updateData[i][0]);
            row.createCell(1).setCellValue(updateData[i][1]);
            row.createCell(2).setCellValue(updateData[i][2]);
        }

        // Auto-size columns
        for (Sheet sheet : new Sheet[]{createSheet, updateSheet}) {
            for (int col = 0; col < sheet.getRow(0).getLastCellNum(); col++) {
                sheet.autoSizeColumn(col);
            }
        }

        // Write to file
        Path filePath = dir.resolve("posts.xlsx");
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            workbook.write(fos);
        }
        workbook.close();

        System.out.println("Test data Excel file created at: " + filePath.toAbsolutePath());
    }
}
