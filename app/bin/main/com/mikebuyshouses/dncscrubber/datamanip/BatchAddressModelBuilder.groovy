package com.mikebuyshouses.dncscrubber.datamanip

import com.mikebuyshouses.dncscrubber.constants.Constants
import com.mikebuyshouses.dncscrubber.models.AddressModel
import com.mikebuyshouses.dncscrubber.models.BaseDataRowModel
import com.mikebuyshouses.dncscrubber.models.BatchSkipTracingDataRowModel
import groovy.transform.InheritConstructors
import org.apache.commons.collections4.MultiValuedMap

@InheritConstructors
class BatchAddressModelBuilder extends BaseChildAddressModelBuilder<BatchSkipTracingDataRowModel> {
    AddressModel inputMailingAddressModel;

    @Override
    BaseChildAddressModelBuilder buildPropertyAddressModel() {
        Map<String, Collection<Object>> rawDataMap = this.model.rawAddressData.asMap();

        this.propertyAddressModel = AddressModel.ExtractFromRawData(rawDataMap
            .subMap(rawDataMap.keySet().findAll { String key -> key.startsWith(Constants.InputPropertyPart) }))

        return this;
    }

    @Override
    BaseChildAddressModelBuilder buildMailingAddressModel() {
        Map<String, Collection<Object>> rawDataMap = this.model.rawAddressData.asMap();

        this.mailingAddressModel = AddressModel.ExtractFromRawData(rawDataMap
                .subMap(rawDataMap.keySet().findAll { String key -> key.startsWith(Constants.MailingPart) }))

        return this;
    }

    BaseChildAddressModelBuilder buildInputMailingAddressModel() {
        Map<String, Collection<Object>> rawDataMap = this.model.rawAddressData.asMap();

        this.inputMailingAddressModel = AddressModel.ExtractFromRawData(rawDataMap
                .subMap(rawDataMap.keySet().findAll { String key -> key.startsWith(Constants.InputMailingPart) }))

        return this;
    }

    @Override
    BatchSkipTracingDataRowModel build() {
        return ((BatchSkipTracingDataRowModel)super.build())
            .with { BatchSkipTracingDataRowModel model ->
                model.inputMailingAddressModel = this.inputMailingAddressModel;

                return model;
            }
    }
}
