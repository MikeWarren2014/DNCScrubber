package com.mikebuyshouses.dncscrubber.models

import com.mikebuyshouses.dncscrubber.datamanip.YesNoBooleanConverter
import com.opencsv.bean.CsvBindAndJoinByName
import com.opencsv.bean.CsvBindByName
import com.opencsv.bean.CsvCustomBindByName
import org.apache.commons.collections4.MultiValuedMap

class BatchSkipTracingDataRowModel extends BaseDataRowModel {
    //TODO: couldn't we clean this up!? create AddressModels and CsvRecurse into those!? Ask Phind, or some other AI, to help with that...
    @CsvBindByName(column = "Input_Mailing_Address")
    String inputMailingAddress

    @CsvBindByName(column = "Input_Mailing_City")
    String inputMailingCity

    @CsvBindByName(column = "Input_Mailing_State")
    String inputMailingState

    @CsvBindByName(column = "Input_Mailing_Zip")
    String inputMailingZip

    @CsvBindByName(column = "Input_Property_Address")
    String propertyAddress

    @CsvBindByName(column = "Input_Property_City")
    String propertyCity

    @CsvBindByName(column = "Input_Property_State")
    String propertyState

    @CsvBindByName(column = "Input_Property_Zip")
    String propertyZip

    @CsvBindByName(column = "Mailing_Address")
    String mailingAddress

    @CsvBindByName(column = "Mailing_City")
    String mailingCity

    @CsvBindByName(column = "Mailing_State")
    String mailingState

    @CsvBindByName(column = "Mailing_Zip")
    String mailingZip

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
}