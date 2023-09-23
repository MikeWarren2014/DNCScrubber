package com.mikebuyshouses.dncscrubber.models

import com.mikebuyshouses.dncscrubber.constants.Constants
import org.apache.commons.collections4.MultiValuedMap

class AddressModel {
    String address, address2, city, state, zip;

    String getAddress() {
        return address
    }

    void setAddress(String address) {
        this.address = address
    }

    String getAddress2() {
        return address2
    }

    void setAddress2(String address2) {
        this.address2 = address2
    }

    String getCity() {
        return city
    }

    void setCity(String city) {
        this.city = city
    }

    String getState() {
        return state
    }

    void setState(String state) {
        this.state = state
    }

    String getZip() {
        return zip
    }

    void setZip(String zip) {
        this.zip = zip
    }

    public static AddressModel ExtractFromRawData(Map<String,Collection<Object>> rawData) {
        AddressModel model = new AddressModel();

        rawData.keySet()
            .each { String key ->
                model = this.BuildAddressModel(model, rawData.get(key)[0], key);
            }

        return model;
    }

    private static AddressModel BuildAddressModel(AddressModel model, String rawValue, String key) {
        if (key.contains(Constants.AddressPart)) {
            model.address = rawValue;

            return model;
        }
        if (key.contains(Constants.CityPart)) {
            model.city = rawValue;

            return model;
        }
        if (key.contains(Constants.StatePart)) {
            model.state = rawValue;

            return model;
        }
        if (key.contains(Constants.ZipPart)) {
            model.zip = rawValue;

            return model;
        }

        throw new IllegalArgumentException("Unrecognized key '${key}'");
    }
}
