package com.mikebuyshouses.dncscrubber.utils

import com.mikebuyshouses.dncscrubber.constants.Constants

import java.util.regex.Matcher

public final class ListUtils {
    public static List<String> SortPhoneColumnNamesList(List<String> phoneColumnNames) {
        return phoneColumnNames.toSorted { String a, String b ->
            Matcher matcherA = (a =~ Constants.PhoneEntryRegex)
            Matcher matcherB = (b =~ Constants.PhoneEntryRegex)

            if (!matcherA.matches())
                throw new Exception("We have a problem with raw phone data column '${a}'")
            if (!matcherB.matches())
                throw new Exception("We have a problem with raw phone data column '${b}'")

            int numA = matcherA.group(1).toInteger(),
                numB = matcherB.group(1).toInteger();

            if (numA != numB) {
                return numA <=> numB
            }
            return a <=> b
        }
    }
}
