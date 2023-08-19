package com.mikebuyshouses.dncscrubber.models

import com.mikebuyshouses.dncscrubber.constants.Constants
import com.opencsv.bean.CsvBindAndJoinByName
import com.opencsv.bean.CsvBindByName
import org.apache.commons.collections4.MultiValuedMap

public class BaseDataRowModel {
	@CsvBindByName(column = "Input_First_Name")
	String firstName;

	@CsvBindByName(column = "Input_Last_Name")
	String lastName;

	@CsvBindAndJoinByName(column = Constants.PhoneEntryRegex, elementType = String.class)
	MultiValuedMap<String, String> rawPhoneData;

	List<PhoneModel> childPhoneModels;

	public String getFirstName() {
        return firstName;
    }

	public void setFirstName(String firstName) {
        this.firstName = firstName;
	}

    public String getLastName() {
        return lastName;
    }
	public void setLastName(String lastName) {
        this.lastName = lastName;
	}
}