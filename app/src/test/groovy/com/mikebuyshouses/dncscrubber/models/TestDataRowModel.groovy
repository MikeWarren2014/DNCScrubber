package com.mikebuyshouses.dncscrubber.models

import com.opencsv.bean.CsvBindByName

public class TestDataRowModel extends BaseDataRowModel {

	@CsvBindByName(column = "ID")
	int id;

	@Override
	BaseDataRowModel buildChildAddressModels() {
		return null
	}
}