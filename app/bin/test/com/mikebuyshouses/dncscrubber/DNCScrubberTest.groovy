package com.mikebuyshouses.dncscrubber

import com.mikebuyshouses.dncscrubber.datamanip.DNCScrubber
import com.mikebuyshouses.dncscrubber.enums.PhoneTypes
import com.mikebuyshouses.dncscrubber.models.AddressModel
import com.mikebuyshouses.dncscrubber.models.BaseDataRowModel
import com.mikebuyshouses.dncscrubber.models.BatchSkipTracingDataRowModel
import com.mikebuyshouses.dncscrubber.models.PhoneModel
import com.mikebuyshouses.dncscrubber.models.TestDataRowModel
import org.apache.poi.ss.formula.functions.Address
import spock.lang.Specification

class DNCScrubberTest extends Specification {
    def "scrubCsvData() should remove only the DNC records and empty rows"() {
        setup:
        List<BatchSkipTracingDataRowModel> csvData = [
                new BatchSkipTracingDataRowModel(
                        firstName: "First",
                        lastName: "TestUser",
                        propertyAddressModel: new AddressModel(
                                address: "1234 Main Street",
                                city: "Indianapolis",
                                state: "IN",
                                zip: "46220",
                        ),
                        inputMailingAddressModel: new AddressModel(
                                address: "1000 Main Street",
                                city: "Indianapolis",
                                state: "IN",
                                zip: "46220",
                        ),
                        mailingAddressModel: new AddressModel(
                                address: "1000 Main Street",
                                city: "Indianapolis",
                                state: "IN",
                                zip: "46220",
                        ),
                        childPhoneModels: [
                                new PhoneModel(
                                        isDNC: false,
                                        score: 100,
                                        phoneType: PhoneTypes.Mobile,
                                        phoneNumber: "3175550123"
                                ),
                                new PhoneModel(
                                        isDNC: true,
                                        score: 90,
                                        phoneType: PhoneTypes.Mobile,
                                        phoneNumber: "3175557123"
                                ),
                        ],
                ),
                new BatchSkipTracingDataRowModel(
                        firstName: "Empty",
                        lastName: "Record",
                        propertyAddressModel: new AddressModel(
                                address: "1243 Main Street",
                                city: "Indianapolis",
                                state: "IN",
                                zip: "46220",
                        ),
                        inputMailingAddressModel: new AddressModel(
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
                        childPhoneModels: [],
                ),
                new BatchSkipTracingDataRowModel(
                        firstName: "Third",
                        lastName: "User",
                        childPhoneModels: [
                                new PhoneModel(
                                        isDNC: false,
                                        score: 100,
                                        phoneType: PhoneTypes.Mobile,
                                        phoneNumber: "3175550000",
                                ),
                        ],
                ),
        ]

        when:
        List<BatchSkipTracingDataRowModel> cleanedCsvData = new DNCScrubber().scrubCsvData(csvData)

        then:
        cleanedCsvData.size() == 2
        cleanedCsvData.first().childPhoneModels.size() == 1
    }

    def "scrubDataRow() should successfully remove child PhoneModels"() {
        setup:
        TestDataRowModel model = new TestDataRowModel(
            firstName: "test",
            lastName: "user",
                childPhoneModels: [
                        new PhoneModel(
                                isDNC: false,
                                score: 100,
                                phoneType: PhoneTypes.Mobile,
                                phoneNumber: "3175550123"
                        ),
                        new PhoneModel(
                                isDNC: true,
                                score: 90,
                                phoneType: PhoneTypes.Mobile,
                                phoneNumber: "3175557123"
                        ),
                ]
        )

        when:
        TestDataRowModel cleanedModel = (TestDataRowModel)new DNCScrubber().scrubDataRow(model);

        then:
        cleanedModel.childPhoneModels.size() == 1
        cleanedModel.childPhoneModels.find {PhoneModel childModel -> childModel.isDNC } == null
    }
}
