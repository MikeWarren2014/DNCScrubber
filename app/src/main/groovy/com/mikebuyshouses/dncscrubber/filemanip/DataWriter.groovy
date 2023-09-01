package com.mikebuyshouses.dncscrubber.filemanip

import com.mikebuyshouses.dncscrubber.constants.Constants
import com.mikebuyshouses.dncscrubber.csvmanip.CSVSheetReader
import com.mikebuyshouses.dncscrubber.datamanip.YesNoBooleanConverter;
import com.mikebuyshouses.dncscrubber.models.BaseDataRowModel
import com.mikebuyshouses.dncscrubber.models.PhoneModel
import com.mikebuyshouses.dncscrubber.utils.FileUtils
import com.mikebuyshouses.dncscrubber.utils.StringUtils
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import java.util.List;

public trait DataWriter {

    public void write(List<BaseDataRowModel> dataRowModels, String outputFileName) {
        this.prepareDataRowModels(dataRowModels);

        this.writeToFile(dataRowModels,
                FileUtils.CreateFileIfNotExists(outputFileName));
    }

    private void prepareDataRowModels(List<BaseDataRowModel> csvData) {
        YesNoBooleanConverter booleanConverter = new YesNoBooleanConverter();

        csvData.each { BaseDataRowModel dataRowModel ->
            if (dataRowModel.rawPhoneData != null)
                dataRowModel.rawPhoneData.clear();
            else
                dataRowModel.rawPhoneData = new ArrayListValuedHashMap<>();

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
                        StringUtils.NullableObjectToString(childModel.date));
            }
        }
    }

    public abstract void writeToFile(List<BaseDataRowModel> dataRowModels, File file);
}
