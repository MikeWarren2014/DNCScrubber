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

public trait DataWriter {

    public void write(List<BaseDataRowModel> dataRowModels, String outputFileName) {
        this.prepareDataRowModels(dataRowModels);

        // TODO: we may need to phase this out for some S3/OutputStream stuff
        this.writeToFile(dataRowModels,
                FileUtils.CreateFileIfNotExists(outputFileName));
    }

    private void prepareDataRowModels(List<BaseDataRowModel> dataRowModels) {
        YesNoBooleanConverter booleanConverter = new YesNoBooleanConverter();

        dataRowModels.each { BaseDataRowModel dataRowModel ->
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

            if (dataRowModel.rawAddressData == null)
                dataRowModel.rawAddressData = new ArrayListValuedHashMap<>();

            this.prepareRawAddressData(dataRowModel, dataRowModel.propertyAddressModel, Constants.InputPropertyPart);
            this.prepareRawAddressData(dataRowModel, dataRowModel.mailingAddressModel, Constants.MailingPart);
        }
    }

    private void prepareRawAddressData(BaseDataRowModel model, AddressModel childAddressModel, String keyPrefix) {
        model.rawAddressData.put("${keyPrefix}_Address".toString(),
            childAddressModel.address);
        model.rawAddressData.put("${keyPrefix}_City".toString(),
            childAddressModel.city);
        model.rawAddressData.put("${keyPrefix}_State".toString(),
            childAddressModel.state);
        model.rawAddressData.put("${keyPrefix}_Zip".toString(),
            childAddressModel.zip);
    }

    // TODO: we may need to phase this out for some S3/OutputStream stuff
    public abstract void writeToFile(List<BaseDataRowModel> dataRowModels, File file);
}
