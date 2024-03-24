package com.mikebuyshouses.dncscrubber.enums

import spock.lang.Specification

class PhoneTypesTest extends Specification {
    def "FromTextValue() should return Unknown for empty string"() {
        expect:
        PhoneTypes.FromTextValue("") == PhoneTypes.Unknown;
    }
}
