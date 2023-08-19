package com.mikebuyshouses.dncscrubber.csvmanip

import com.mikebuyshouses.dncscrubber.constants.Constants
import com.mikebuyshouses.dncscrubber.models.BaseDataRowModel
import com.mikebuyshouses.dncscrubber.models.PhoneModel
import com.mikebuyshouses.dncscrubber.utils.NumberUtils
import com.opencsv.bean.CsvToBeanBuilder
import com.opencsv.bean.HeaderColumnNameMappingStrategy
import com.opencsv.bean.MappingStrategy
import groovy.transform.InheritConstructors

@InheritConstructors
public class CSVSheetReader extends CSVIO {
	public static int FirstPhoneEntryNumber;

	public List<BaseDataRowModel> read(String fileName) {

		List<BaseDataRowModel> dataRows = [];

		new File(fileName).withReader{ reader ->
			dataRows = new CsvToBeanBuilder(reader)
				.withType(this.clazz)
				.build()
				.parse();

			MappingStrategy mappingStrategy = new HeaderColumnNameMappingStrategy();
			mappingStrategy.setType(this.clazz)

			List<Integer> phoneEntryNumbers = mappingStrategy
				.generateHeader(dataRows[0])
				.toList()
				.findAll { String header -> return header.matches(Constants.PhoneEntryRegex); }
				.collect { String header -> return NumberUtils.ExtractNumber(header); }
				.unique()

			this.FirstPhoneEntryNumber = phoneEntryNumbers
				.min();
		}

		dataRows.each { BaseDataRowModel dataRowModel ->
			dataRowModel.with { BaseDataRowModel model ->
				model.childPhoneModels = PhoneModel.ExtractFromRawPhoneData(model.rawPhoneData);

				return model;
			}
		}

		return dataRows;
	}
}