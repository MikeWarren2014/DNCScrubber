package com.mikebuyshouses.dncscrubber.utils

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.WorkbookFactory

public final class FileUtils {
    public static final String TestDirectoryPath = "src/test/resources";

    public static File ExcelToCsv(String excelFileName) {
        File excelFile = new File(excelFileName),
            tmpCsvFile = File.createTempFile("tmp_${this.RemoveExtensionFromFileName(excelFileName)}",
                    '.csv');

        tmpCsvFile.withWriter { writer ->
            final Sheet sheet = WorkbookFactory.create(excelFile).getSheetAt(0);

            final Row headerRow = sheet.getRow(0);
            final int cellCount = SpreadsheetUtils.GetLastCellIndex(headerRow);

            this.WriteToCsv(headerRow, writer, cellCount);
            SpreadsheetUtils.ExtractDataFromSheet(sheet,
                { int rowNum ->
                    this.WriteToCsv(sheet.getRow(rowNum), writer, cellCount);
                })
        }

        tmpCsvFile.deleteOnExit();

        return tmpCsvFile;
    }

    public static String RemoveExtensionFromFileName(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    public static void WriteToCsv(Row row, BufferedWriter csvWriter, int lastCellIdx) {
        (0..lastCellIdx).each { int cellIdx ->
            Cell cell = row.getCell(cellIdx);
            if (cell != null)
                csvWriter.append("\"${SpreadsheetUtils.GetCellValue(cell).toString()}\"");

            csvWriter.append(',');
        }
        csvWriter.append('\n');
    }

    public static File CreateFileIfNotExists(String fileName) {
        File file = new File(fileName);
        if (!file.exists()){
            File parentDirectory = file.getParentFile();
            if (parentDirectory == null)
                parentDirectory = new File('..')

            parentDirectory.mkdirs();
            file.createNewFile();
        }

        return file;
    }
}
