package com.mikebuyshouses.dncscrubber.csvmanip

import com.mikebuyshouses.dncscrubber.constants.Constants
import com.mikebuyshouses.dncscrubber.models.BaseDataRowModel
import com.mikebuyshouses.dncscrubber.models.PhoneModel
import com.opencsv.CSVWriter
import com.opencsv.bean.StatefulBeanToCsv
import com.opencsv.bean.StatefulBeanToCsvBuilder
import groovy.transform.InheritConstructors

@InheritConstructors
public class CSVSheetWriter extends CSVIO {
    public void write(List<BaseDataRowModel> csvData, String outputFileName) {
        this.prepareDataRowModels(csvData);

        FileWriter fileWriter = new FileWriter(outputFileName);

        new CSVWriter(fileWriter).withCloseable { CSVWriter csvWriter ->
            // Create StatefulBeanToCsv
            StatefulBeanToCsv<BaseDataRowModel> beanToCsv = new StatefulBeanToCsvBuilder<BaseDataRowModel>(csvWriter)
                .build();

            // Write data to CSV file
            beanToCsv.write(csvData);
        }
    }

    private void prepareDataRowModels(List<BaseDataRowModel> csvData) {
        YesNoBooleanConverter booleanConverter = new YesNoBooleanConverter();

        csvData.each { BaseDataRowModel dataRowModel ->
            dataRowModel.rawPhoneData.clear();

            dataRowModel.childPhoneModels.eachWithIndex{ PhoneModel childModel, int idx ->
                final int entryNumber = CSVSheetReader.FirstPhoneEntryNumber + idx;

                dataRowModel.rawPhoneData.put("Phone${entryNumber}_${Constants.DncKeyPart}".toString(),
                    booleanConverter.convertBooleanToString(childModel.isDNC));
                dataRowModel.rawPhoneData.put("Phone${entryNumber}_${Constants.ScoreKeyPart}".toString(),
                    childModel.score.toString());
                dataRowModel.rawPhoneData.put("Phone${entryNumber}_${Constants.TypeKeyPart}".toString(),
                    childModel.phoneType.textValue);
                dataRowModel.rawPhoneData.put("Phone${entryNumber}_${Constants.NumberKeyPart}".toString(),
                    childModel.phoneNumber);
                dataRowModel.rawPhoneData.put("Phone${entryNumber}_${Constants.DateKeyPart}".toString(),
                    childModel.date.toString());
            }
        }
    }
}
