package com.mikebuyshouses.dncscrubber

import com.mikebuyshouses.dncscrubber.datamanip.DNCScrubber
import com.mikebuyshouses.dncscrubber.enums.PhoneTypes
import com.mikebuyshouses.dncscrubber.models.BaseDataRowModel
import com.mikebuyshouses.dncscrubber.models.BatchSkipTracingDataRowModel
import com.mikebuyshouses.dncscrubber.models.PhoneModel
import spock.lang.Specification

class DNCScrubberTest extends Specification {
    def "scrubCsvData() should remove only the DNC records and empty rows"() {
        setup:
        List<BatchSkipTracingDataRowModel> csvData = [
                new BatchSkipTracingDataRowModel(
                        firstName: "First",
                        lastName: "TestUser",
                        propertyAddress: "1234 Main Street",
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
                        propertyAddress: "1243 Main Street",
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
        BaseDataRowModel model = new BaseDataRowModel(
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
        BaseDataRowModel cleanedModel = new DNCScrubber().scrubDataRow(model);

        then:
        cleanedModel.childPhoneModels.size() == 1
        cleanedModel.childPhoneModels.find {PhoneModel childModel -> childModel.isDNC } == null
    }
}
