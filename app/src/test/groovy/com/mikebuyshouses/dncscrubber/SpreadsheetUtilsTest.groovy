package com.mikebuyshouses.dncscrubber

import com.mikebuyshouses.dncscrubber.utils.SpreadsheetUtils
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import spock.lang.Specification

class SpreadsheetUtilsTest extends Specification {
    def "GetLastNonEmptyCellIndex() should look past null cells in the middle"() {
        setup:
        File excelFile = new File("src/test/resources/testFile.xlsx");
        excelFile.createNewFile();

        excelFile.withOutputStream {  outputStream ->
            new XSSFWorkbook().withCloseable { XSSFWorkbook workbook ->
                Sheet sheet = workbook.createSheet();
                Row headerRow = sheet.createRow(0),
                    firstRow = sheet.createRow(1),
                    secondRow = sheet.createRow(2);

                headerRow.createCell(0).setCellValue("First Name");
                headerRow.createCell(1).setCellValue("Last Name");
                headerRow.createCell(2).setCellValue("Phone Number");

                firstRow.createCell(0).setCellValue("NoLastName");
                firstRow.createCell(2).setCellValue("3175550100");

                workbook.write(outputStream)
            }

        }

        when:
        int lastCellIdx = -1;

        WorkbookFactory.create(excelFile).withCloseable { Workbook workbook ->
            lastCellIdx = SpreadsheetUtils.GetLastCellIndex(workbook.getSheetAt(0).getRow(1));
        }

        then:
        lastCellIdx == 2;

        cleanup:
        excelFile.delete();
    }

    def "GetLastNonEmptyCellIndex() should look past null cells in the middle"() {
        setup:
        File excelFile = new File("src/test/resources/testFile.xlsx");
        excelFile.createNewFile();

        excelFile.withOutputStream {  outputStream ->
            new XSSFWorkbook().withCloseable { XSSFWorkbook workbook ->
                Sheet sheet = workbook.createSheet();
                Row headerRow = sheet.createRow(0),
                    firstRow = sheet.createRow(1);

                headerRow.createCell(0).setCellValue("First Name");
                headerRow.createCell(1).setCellValue("Last Name");
                headerRow.createCell(2).setCellValue("Phone Number");

                firstRow.createCell(0).setCellValue("NoLastName");
                firstRow.createCell(2).setCellValue("3175550100");

                workbook.write(outputStream)
            }

        }

        when:
        int lastCellIdx = -1;

        WorkbookFactory.create(excelFile).withCloseable { Workbook workbook ->
            lastCellIdx = SpreadsheetUtils.GetLastCellIndex(workbook.getSheetAt(0).getRow(1));
        }

        then:
        lastCellIdx == 2;

        cleanup:
        excelFile.delete();
    }

    def "GetLastNonEmptyCellIndex() should work on header row"() {
        setup:
        File excelFile = new File("src/test/resources/testFile.xlsx");
        excelFile.createNewFile();

        excelFile.withOutputStream {  outputStream ->
            new XSSFWorkbook().withCloseable { XSSFWorkbook workbook ->
                Sheet sheet = workbook.createSheet();
                Row headerRow = sheet.createRow(0);

                headerRow.createCell(0).setCellValue("First Name");
                headerRow.createCell(1).setCellValue("Last Name");
                headerRow.createCell(2).setCellValue("Phone Number");

                workbook.write(outputStream)
            }

        }

        when:
        int lastCellIdx = -1;

        WorkbookFactory.create(excelFile).withCloseable { Workbook workbook ->
            lastCellIdx = SpreadsheetUtils.GetLastCellIndex(workbook.getSheetAt(0).getRow(0));
        }

        then:
        lastCellIdx == 2;

        cleanup:
        excelFile.delete();
    }
}
