package com.mikebuyshouses.dncscrubber

import com.mikebuyshouses.dncscrubber.utils.FileUtils
import com.mikebuyshouses.dncscrubber.utils.SpreadsheetUtils
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import spock.lang.Specification

class FileUtilsTest extends Specification {
    def "WriteToCsv() should handle the null Cells in a Row"() {
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
        File testCsvFile = new File("src/test/resources/testNullCells.csv");
        testCsvFile.createNewFile();

        testCsvFile.withWriter { BufferedWriter bufferedWriter ->
            WorkbookFactory.create(excelFile).withCloseable { Workbook workbook ->
                final Sheet sheet = workbook.getSheetAt(0);
                final int cellCount = SpreadsheetUtils.GetLastCellIndex(sheet.getRow(0));

                FileUtils.WriteToCsv(sheet.getRow(1),
                        bufferedWriter,
                        cellCount);
            }

        }

        String line
        testCsvFile.withReader { BufferedReader reader ->
            line = reader.readLine()
        }

        then:
        line == "\"NoLastName\",,\"3175550100\","

        cleanup:
        excelFile.delete()
        testCsvFile.delete()
    }

    def "ExcelToCsv() should return a CSV File whose rows contain the same number of commas"() {
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
        File testCsvFile = new File("src/test/resources/testNullCells.csv");
        testCsvFile.createNewFile();

        testCsvFile.withWriter { BufferedWriter bufferedWriter ->
            WorkbookFactory.create(excelFile).withCloseable { Workbook workbook ->
                final Sheet sheet = workbook.getSheetAt(0);
                final int cellCount = SpreadsheetUtils.GetLastCellIndex(sheet.getRow(0));

                sheet.each { Row row ->
                    FileUtils.WriteToCsv(row,
                        bufferedWriter,
                        cellCount);
                }
            }

        }

        String firstLine, secondLine
        testCsvFile.withReader { BufferedReader reader ->
            firstLine = reader.readLine();
            secondLine = reader.readLine();
        }

        then:
        firstLine != secondLine
        firstLine.split(',').length == secondLine.split(',').length

        cleanup:
        excelFile.delete()
        testCsvFile.delete()
    }

}
