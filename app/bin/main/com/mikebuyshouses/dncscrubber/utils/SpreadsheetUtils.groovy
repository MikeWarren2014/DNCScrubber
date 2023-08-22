package com.mikebuyshouses.dncscrubber.utils

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.CreationHelper
import org.apache.poi.ss.usermodel.DataFormat
import org.apache.poi.ss.usermodel.DateUtil
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook

/**
 * Ripped from https://github.com/MikeWarren2014/KatalonStudioSDK/blob/main/Keywords/me/mikewarren/katalonstudiosdk/utils/SpreadsheetUtils.groovy
 *  and updated for the latest Apache POI
 */
public final class SpreadsheetUtils {
    public static final int FirstDataRowNumber = 1;

    public static boolean IsRowEmpty(Row row) {
        return row.getLastCellNum() == -1;
    }

    public static void ExtractDataFromSheet(Sheet sheet, Closure onExtractData) {
        for (int rowNum = this.FirstDataRowNumber; rowNum <= this.GetLastNonEmptyRowNum(sheet); rowNum++) {
            onExtractData(rowNum);
        }
    }

    public static int GetLastNonEmptyRowNum(Sheet sheet) {
        return sheet.findLastIndexOf { Row row -> !this.IsRowEmpty(row) };
    }

    public static Closure<Sheet> OnCreateIfNotExistSheet(Workbook spreadsheet) {
        return { String sheetName ->
            Sheet sheet = spreadsheet.getSheet(sheetName);
            if (sheet != null)
                return sheet;
            return spreadsheet.createSheet(sheetName);
        }
    }

    public static Closure OnCopySheetIntoSpreadsheet(Workbook spreadsheet) {
        return { Sheet sheet ->
            Sheet targetSheet = this.OnCreateIfNotExistSheet(spreadsheet)(sheet.getSheetName());

            sheet.eachWithIndex({ Row row, int rowIdx ->
                row.eachWithIndex({ Cell cell, int colIdx ->
                    Row targetRow = targetSheet.getRow(rowIdx);
                    if (targetRow == null)
                        targetRow = targetSheet.createRow(rowIdx);

                    Cell targetCell = targetRow.getCell(colIdx);
                    if (targetCell == null)
                        targetCell = targetRow.createCell(colIdx);

                    this.CopyCellValue(cell, targetCell);
                })
            })
        }
    }

    // SOURCE: ChatGPT AI, refactored by Mike Warren
    public static void CopyCellValue(Cell sourceCell, Cell targetCell) {
        switch (sourceCell.getCellType()) {
            case CellType.STRING:
                targetCell.setCellValue(sourceCell.getStringCellValue());
                break;
            case CellType.NUMERIC:
                if (DateUtil.isCellDateFormatted(sourceCell)) {
                    targetCell.setCellValue(sourceCell.getDateCellValue());
                } else {
                    targetCell.setCellValue(sourceCell.getNumericCellValue());
                }
                break;
            case CellType.BOOLEAN:
                targetCell.setCellValue(sourceCell.getBooleanCellValue());
                break;
            case CellType.FORMULA:
                targetCell.setCellFormula(sourceCell.getCellFormula());
                break;
            case CellType.ERROR:
                targetCell.setCellErrorValue(sourceCell.getErrorCellValue());
                break;
            case CellType.BLANK:
                targetCell.setCellValue(null);
                break;
            default:
                // If the cell type is unknown, copy the cell value as a string
                CreationHelper creationHelper = targetCell.getSheet().getWorkbook().getCreationHelper();
                targetCell.setCellValue(creationHelper.createRichTextString(sourceCell.toString()));
        }
    }

    public static Row FindRowWithKey(Sheet sheet, Object key) {
        return sheet
                .findResult(sheet.createRow(this.GetLastNonEmptyRowNum(sheet) + 1),
                        { Row row ->
                            if (row.getRowNum() == 0)
                                return null;

                            final Cell firstCell = row.getCell(0);

                            if (firstCell == null)
                                return null;

                            if (!this.CompareKey(firstCell, key))
                                return null;

                            return row;
                        });
    }

    public static boolean CompareKey(Cell keyCell, Object key) {
        if (key.equals(key.toString()))
            return keyCell.getStringCellValue()
                    .equals(key);

        return keyCell.getNumericCellValue() == key;
    }

    public static Cell CreateCellIfNotExistIn(Row row, int cellIdx) {
        final Cell cell = row.getCell(cellIdx);
        if (cell != null) {
            return cell;
        }

        return row.createCell(cellIdx);
    }

    // SOURCE: https://www.phind.com/search?cache=d4085649-039f-4d86-b5cd-963ee40bfc49
    public static void WriteIntegerCellValue(Cell cell, long value) {
        Workbook workbook = cell.getSheet().getWorkbook();

        CellStyle integerStyle = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        integerStyle.setDataFormat(format.getFormat("0"));

        cell.setCellValue(value);
        cell.setCellStyle(integerStyle);

    }

    // TODO: add this to the KatalonStudioSDK
    public static Object GetCellValue(Cell cell) {
        if (cell == null)
            return null;

        switch (cell.getCellType()) {
            case CellType.STRING:
                final String stringCellValue = cell.getStringCellValue();

                if (stringCellValue.empty)
                    return '';

                if (stringCellValue[0].equals('$'))
                    return this.GetDollarAmount(cell);
                return stringCellValue;
            case CellType.NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                }
                return cell.getNumericCellValue();
            case CellType.BOOLEAN:
                return cell.getBooleanCellValue();
            case CellType.FORMULA:
                return cell.getCellFormula();
            case CellType.ERROR:
                return cell.getErrorCellValue();
            case CellType.BLANK:
                return null;
            default:
                // If the cell type is unknown, get the cell value as a string
                CreationHelper creationHelper = cell.getSheet().getWorkbook().getCreationHelper();
                return creationHelper.createRichTextString(cell.toString());
        }
    }

    public static int GetLastCellIndex(Row row) {
        if (this.IsRowEmpty(row))
            return -1;

        return (1..row.getLastCellNum())
            .max { int cellIdx ->
                return (row.getCell(cellIdx) != null) ? cellIdx : -1;
            }
    }
}
