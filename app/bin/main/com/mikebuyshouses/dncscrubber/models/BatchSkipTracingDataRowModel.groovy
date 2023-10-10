package com.mikebuyshouses.dncscrubber.models

import com.mikebuyshouses.dncscrubber.datamanip.BatchAddressModelBuilder
import com.mikebuyshouses.dncscrubber.datamanip.YesNoBooleanConverter
import com.opencsv.bean.CsvBindAndJoinByName
import com.opencsv.bean.CsvBindByName
import com.opencsv.bean.CsvCustomBindByName
import org.apache.commons.collections4.MultiValuedMap

class BatchSkipTracingDataRowModel extends BaseDataRowModel {
    AddressModel inputMailingAddressModel;

    @CsvBindAndJoinByName(column = "Email\\d", elementType = String.class)
    MultiValuedMap<String, String> emails

    @CsvBindByName(column = "OWNER_LAST_NAME")
    String ownerLastName

    @CsvBindByName(column = "OWNER_FIRST_NAME")
    String ownerFirstName

    @CsvBindByName(column = "OWNER_2_LAST_NAME")
    String owner2LastName

    @CsvBindByName(column = "OWNER_2_FIRST_NAME")
    String owner2FirstName

    @CsvCustomBindByName(column = "LITIGATOR", converter = YesNoBooleanConverter.class)
    boolean isLitigator;

    @Override
    BaseDataRowModel buildChildAddressModels() {
        return new BatchAddressModelBuilder(this)
            .buildInputMailingAddressModel()
            .buildMailingAddressModel()
            .buildPropertyAddressModel()
            .build();
    }
}