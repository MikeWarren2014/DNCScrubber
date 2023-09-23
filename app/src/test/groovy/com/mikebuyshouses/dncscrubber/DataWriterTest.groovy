package com.mikebuyshouses.dncscrubber

import com.mikebuyshouses.dncscrubber.constants.Constants
import com.mikebuyshouses.dncscrubber.csvmanip.CSVSheetWriter
import com.mikebuyshouses.dncscrubber.enums.PhoneTypes
import com.mikebuyshouses.dncscrubber.filemanip.TestDoubleDataWriter
import com.mikebuyshouses.dncscrubber.models.AddressModel
import com.mikebuyshouses.dncscrubber.models.BaseDataRowModel
import com.mikebuyshouses.dncscrubber.models.PhoneModel
import com.mikebuyshouses.dncscrubber.models.TestDataRowModel
import org.apache.commons.collections4.MultiValuedMap
import spock.lang.Specification

class DataWriterTest extends Specification {
    def "should accurately prepare the BaseRowDataModels, by writing their childPhoneModels to rawPhoneData"() {
        setup:
        List<TestDataRowModel> csvData = [
                new TestDataRowModel(
                        firstName: "Alice",
                        lastName: "User",
                        childPhoneModels: [
                                new PhoneModel(
                                        isDNC: false,
                                        phoneType: PhoneTypes.Mobile,
                                        score: 100,
                                        phoneNumber: "3175550001",
                                ),
                                new PhoneModel(
                                        isDNC: false,
                                        phoneType: PhoneTypes.Mobile,
                                        score: 90,
                                        phoneNumber: "3175550002",
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
                        lastName: "User",
                        childPhoneModels: [
                                new PhoneModel(
                                        isDNC: false,
                                        phoneType: PhoneTypes.Mobile,
                                        score: 100,
                                        phoneNumber: "3175550003",
                                ),
                        ],
                        propertyAddressModel: new AddressModel(
                                address: "1002 Main Street",
                                city: "Indianapolis",
                                state: "IN",
                                zip: "46220",
                        ),
                        mailingAddressModel: new AddressModel(
                                address: "1002 Main Street",
                                city: "Indianapolis",
                                state: "IN",
                                zip: "46220",
                        ),
                ),
        ]

        List<TestDataRowModel> receivedArgs = [];

        TestDoubleDataWriter dataWriter = new TestDoubleDataWriter({ List<BaseDataRowModel> dataRowModels ->
            receivedArgs = dataRowModels as List<TestDataRowModel>;
        })

        when:
        dataWriter.write(csvData, "dummy.csv");

        then:
        receivedArgs.size() == 2

        List<String> firstModelPhoneKeys = this.getMapKeysList(receivedArgs[0].rawPhoneData)
        firstModelPhoneKeys.find { String key -> key.startsWith("Phone0")} != null;
        firstModelPhoneKeys.find { String key -> key.startsWith("Phone1")} != null;
        firstModelPhoneKeys.find { String key -> key.startsWith("Phone2")} == null;

        this.validateAddressKeys(receivedArgs[0],
            { TestDataRowModel model -> return receivedArgs[0].getMailingAddressModel() },
            Constants.MailingPart,
        )
        this.validateAddressKeys(receivedArgs[0],
            { TestDataRowModel model -> return receivedArgs[0].getPropertyAddressModel() },
            Constants.MailingPart,
        )

        this.validateAddressKeys(receivedArgs[1],
            { TestDataRowModel model -> return receivedArgs[1].getMailingAddressModel() },
            Constants.MailingPart,
        )
        this.validateAddressKeys(receivedArgs[1],
            { TestDataRowModel model -> return receivedArgs[1].getPropertyAddressModel() },
            Constants.MailingPart,
        )
    }

    private boolean validateAddressKeys(TestDataRowModel model, Closure<AddressModel> onGetAddressModel, String expectedKeyPrefix) {
        final AddressModel addressModel = onGetAddressModel(model);

        List<String> childAddressMapKeysList = this.getMapKeysList(model.rawAddressData);

        final String expectedAddressKey = "${expectedKeyPrefix}_${Constants.AddressPart}".toString(),
                     expectedCityKey = "${expectedKeyPrefix}_${Constants.CityPart}".toString(),
                     expectedStateKey = "${expectedKeyPrefix}_${Constants.StatePart}".toString(),
                     expectedZipKey = "${expectedKeyPrefix}_${Constants.ZipPart}".toString();

        assert childAddressMapKeysList.indexOf(expectedAddressKey) != -1;
        assert childAddressMapKeysList.indexOf(expectedCityKey) != -1;
        assert childAddressMapKeysList.indexOf(expectedStateKey) != -1;
        assert childAddressMapKeysList.indexOf(expectedZipKey) != -1;

        assert model.rawAddressData
                .get(expectedAddressKey)
                .first() == addressModel.getAddress();
        assert model.rawAddressData
                .get(expectedCityKey)
                .first() == addressModel.getCity();
        assert model.rawAddressData
                .get(expectedStateKey)
                .first() == addressModel.getState();
        assert model.rawAddressData
                .get(expectedZipKey)
                .first() == addressModel.getZip();

        return true;
    }

    private List<String> getMapKeysList(MultiValuedMap map) {
        return map.keySet()
                .asList();
    }
}
