package com.mikebuyshouses.dncscrubber.filemanip

import com.mikebuyshouses.dncscrubber.constants.Constants
import com.mikebuyshouses.dncscrubber.csvmanip.CSVSheetReader
import com.mikebuyshouses.dncscrubber.datamanip.YesNoBooleanConverter
import com.mikebuyshouses.dncscrubber.models.AddressModel
import com.mikebuyshouses.dncscrubber.models.BaseDataRowModel
import com.mikebuyshouses.dncscrubber.models.PhoneModel
import com.mikebuyshouses.dncscrubber.utils.FileUtils
import com.mikebuyshouses.dncscrubber.utils.StringUtils
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap

import java.util.regex.Matcher

public trait DataWriter {

    public void write(List<BaseDataRowModel> dataRowModels, String outputFileName) {
        this.prepareDataRowModels(dataRowModels);

        // TODO: we may need to phase this out for some S3/OutputStream stuff
        this.writeToFile(dataRowModels,
                FileUtils.CreateFileIfNotExists(outputFileName));
    }

    private void prepareDataRowModels(List<BaseDataRowModel> dataRowModels) {
        dataRowModels.eachWithIndex { BaseDataRowModel dataRowModel, int idx ->
            if (dataRowModel.rawPhoneData != null)
                dataRowModel.rawPhoneData.clear();
            else
                dataRowModel.rawPhoneData = new ArrayListValuedHashMap<>();

            List<String> allRawPhoneDataColumns = dataRowModels.max {BaseDataRowModel model -> return model.rawPhoneData.size()}
                    .rawPhoneData
                    .keySet()
                    .toList()
                    .toSorted { a, b ->
                        Matcher matcherA = (a =~ Constants.PhoneEntryRegex)
                        Matcher matcherB = (b =~ Constants.PhoneEntryRegex)

                        if (!matcherA.matches())
                            throw new Exception("We have a problem with raw phone data column '${a}'")
                        if (!matcherB.matches())
                            throw new Exception("We have a problem with raw phone data column '${b}'")

                        int numA = matcherA.group(1).toInteger(),
                            numB = matcherB.group(1).toInteger()

                        if (numA != numB) {
                            return numA <=> numB
                        }
                        return a <=> b
                    }

            this.prepareRawPhoneData(dataRowModel,
{ BaseDataRowModel model, List<String> rawPhoneDataColumns ->
                    if (idx > 0)
                        return;

                    rawPhoneDataColumns
                        .subList(model.childPhoneModels.size() * 5, rawPhoneDataColumns.size())
                        .each({ String columnName ->
                            model.rawPhoneData.put(columnName, "");
                        })
                },
                allRawPhoneDataColumns,
            );

            if (dataRowModel.rawAddressData == null)
                dataRowModel.rawAddressData = new ArrayListValuedHashMap<>();

            this.prepareRawAddressData(dataRowModel, dataRowModel.propertyAddressModel, Constants.InputPropertyPart);
            this.prepareRawAddressData(dataRowModel, dataRowModel.mailingAddressModel, Constants.MailingPart);
        }
    }
    
    private void prepareRawPhoneData(BaseDataRowModel model, Closure onPostPreparation, List<String> allRawPhoneDataColumns) {
        YesNoBooleanConverter booleanConverter = new YesNoBooleanConverter();
        
        model.childPhoneModels.eachWithIndex{ PhoneModel childModel, int idx ->
            final int entryNumber = CSVSheetReader.FirstPhoneEntryNumber + idx;

            model.rawPhoneData.put("Phone${entryNumber}_${Constants.DncKeyPart}".toString(),
                    booleanConverter.convertBooleanToString(childModel.isDNC));
            model.rawPhoneData.put("Phone${entryNumber}_${Constants.ScoreKeyPart}".toString(),
                    childModel.score.toString());
            model.rawPhoneData.put("Phone${entryNumber}_${Constants.TypeKeyPart}".toString(),
                    childModel.phoneType.textValue);
            model.rawPhoneData.put("Phone${entryNumber}_${Constants.NumberKeyPart}".toString(),
                    childModel.phoneNumber);
            model.rawPhoneData.put("Phone${entryNumber}_${Constants.DateKeyPart}".toString(),
                    StringUtils.NullableObjectToString(childModel.date));
        }

        onPostPreparation(model, allRawPhoneDataColumns);
    }

    private void prepareRawAddressData(BaseDataRowModel model, AddressModel childAddressModel, String keyPrefix) {
        if (!model.rawAddressData.containsKey("${keyPrefix}_Address".toString()))
            model.rawAddressData.put("${keyPrefix}_Address".toString(),
                childAddressModel.address);
        if (!model.rawAddressData.containsKey("${keyPrefix}_City".toString()))
            model.rawAddressData.put("${keyPrefix}_City".toString(),
                childAddressModel.city);
        if (!model.rawAddressData.containsKey("${keyPrefix}_State".toString()))
            model.rawAddressData.put("${keyPrefix}_State".toString(),
                childAddressModel.state);
        if (!model.rawAddressData.containsKey("${keyPrefix}_Zip".toString()))
            model.rawAddressData.put("${keyPrefix}_Zip".toString(),
                childAddressModel.zip);
    }

    // TODO: we may need to phase this out for some S3/OutputStream stuff
    public abstract void writeToFile(List<BaseDataRowModel> dataRowModels, File file);
}
