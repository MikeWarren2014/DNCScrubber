package com.mikebuyshouses.dncscrubber.filemanip

import com.mikebuyshouses.dncscrubber.models.BaseDataRowModel
import com.mikebuyshouses.dncscrubber.utils.SpreadsheetUtils
import com.opencsv.bean.MappingStrategy
import com.opencsv.bean.util.OpencsvUtils
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class ExcelDataWriter implements DataWriter {
    private MappingStrategy mappingStrategy;

    @Override
    void writeToFile(List<BaseDataRowModel> dataRowModels, File file) {
        file.withOutputStream { outputStream ->
            new XSSFWorkbook().withCloseable { XSSFWorkbook workbook ->
                Sheet sheet = workbook.createSheet();

                final BaseDataRowModel modelWithMostPhoneEntries = dataRowModels.max { BaseDataRowModel model -> return model.childPhoneModels.size() };
                mappingStrategy = determineMappingStrategy(modelWithMostPhoneEntries);

                writeHeaderRow(sheet,
                     modelWithMostPhoneEntries);


                dataRowModels.eachWithIndex { BaseDataRowModel model, int rowIdx ->
                    final Row dataRow = sheet.createRow(SpreadsheetUtils.FirstDataRowNumber + rowIdx);

                    List<String> rowData = mappingStrategy.transmuteBean(model)
                        .toList();

                    rowData.eachWithIndex { String cellData, int cellIdx ->
                        dataRow.createCell(cellIdx)
                            .setCellValue(cellData);
                    }
                }

                workbook.write(outputStream);
            }
        }

    }

    private MappingStrategy determineMappingStrategy(BaseDataRowModel model) {
        return OpencsvUtils.determineMappingStrategy(model.getClass(),
                Locale.getDefault(),
                // TODO: not sure about the profile stuff yet...
                org.apache.commons.lang3.StringUtils.defaultString(""),
        );
    }

    void writeHeaderRow(Sheet sheet, BaseDataRowModel model) {
        // come up with a way to get the header row
        List<String> headerTitles = this.mappingStrategy
            .generateHeader(model)
            .toList();

        final Row headerRow = sheet.createRow(0);

        headerTitles.eachWithIndex { String title, int idx ->
            headerRow.createCell(idx)
                .setCellValue(title);
        }
    }

    MappingStrategy getMappingStrategy() {
        return mappingStrategy
    }
}
