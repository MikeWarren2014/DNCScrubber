package com.mikebuyshouses.dncscrubber

import com.mikebuyshouses.dncscrubber.constants.Constants
import com.mikebuyshouses.dncscrubber.enums.PhoneTypes
import com.mikebuyshouses.dncscrubber.filemanip.ExcelDataWriter
import com.mikebuyshouses.dncscrubber.models.AddressModel
import com.mikebuyshouses.dncscrubber.models.BaseDataRowModel
import com.mikebuyshouses.dncscrubber.models.PhoneModel
import com.mikebuyshouses.dncscrubber.models.TestDataRowModel
import com.mikebuyshouses.dncscrubber.utils.FileUtils
import com.mikebuyshouses.dncscrubber.utils.StringUtils
import org.apache.poi.ss.usermodel.*
import spock.lang.Specification

class ExcelDataWriterTest extends Specification {
    def "writeToFile() should write all models to file"() {
        setup:
        List<BaseDataRowModel> models = [
                new TestDataRowModel(
                        firstName: "Alice",
                        lastName: "Simmons",
                        childPhoneModels: [
                                new PhoneModel(
                                        isDNC: false,
                                        score: 100,
                                        phoneType: PhoneTypes.Mobile,
                                        phoneNumber: "3175550101",
                                ),
                                new PhoneModel(
                                        isDNC: false,
                                        score: 90,
                                        phoneType: PhoneTypes.Mobile,
                                        phoneNumber: "3175550103",
                                ),
                        ],
                        propertyAddressModel: new AddressModel(
                                address: "1001 Main Street",
                                city: "Indianapolis",
                                state: "IN",
                                zip: "46220",
                        ),
                        mailingAddressModel: new AddressModel(
                                address: "1001 Main Street",
                                city: "Indianapolis",
                                state: "IN",
                                zip: "46220",
                        ),
                ),
                new TestDataRowModel(
                        firstName: "Bob",
                        lastName: "Rucker",
                        childPhoneModels: [
                                new PhoneModel(
                                        isDNC: false,
                                        score: 100,
                                        phoneType: PhoneTypes.Mobile,
                                        phoneNumber: "3176660101",
                                ),
                        ],
                        propertyAddressModel: new AddressModel(
                                address: "10012 Main Street",
                                city: "Indianapolis",
                                state: "IN",
                                zip: "46220",
                        ),
                        mailingAddressModel: new AddressModel(
                                address: "10012 Main Street",
                                city: "Indianapolis",
                                state: "IN",
                                zip: "46220",
                        ),
                ),
        ]

        final File outputFile = FileUtils.CreateFileIfNotExists("${FileUtils.TestDirectoryPath}/excel.xlsx");

        ExcelDataWriter excelDataWriter = new ExcelDataWriter();

        when:
        excelDataWriter.write(models, outputFile.getPath());

        List<String> titleRowValues, firstRowValues, secondRowValues;

        WorkbookFactory.create(outputFile)
            .withCloseable { Workbook workbook ->
                final Sheet sheet = workbook.getSheetAt(0);

                final Row titleRow = sheet.getRow(0);
                titleRowValues = titleRow
                    .collect { Cell cell -> return cell.getStringCellValue() }

                firstRowValues = sheet.getRow(1)
                    .collect { Cell cell -> return cell.getStringCellValue() }

                secondRowValues = sheet.getRow(2)
                    .collect { Cell cell -> return cell.getStringCellValue() }

            }

        then:
        titleRowValues != null && titleRowValues.size() > 0;
        int firstPhoneCellIdx = titleRowValues.findIndexOf { String title -> return title == "Phone0_${Constants.NumberKeyPart}" }
        firstPhoneCellIdx != -1
        int secondPhoneCellIdx = titleRowValues.findIndexOf { String title -> return title == "Phone1_${Constants.NumberKeyPart}" }
        secondPhoneCellIdx != -1

        firstRowValues[firstPhoneCellIdx] == models[0].childPhoneModels[0].getPhoneNumber();
        firstRowValues[secondPhoneCellIdx] == models[0].childPhoneModels[1].getPhoneNumber();

        secondRowValues[firstPhoneCellIdx] == models[1].childPhoneModels[0].getPhoneNumber();
        StringUtils.IsNullOrEmpty(secondRowValues[secondPhoneCellIdx]);

        cleanup:
        outputFile.delete();

    }

}
