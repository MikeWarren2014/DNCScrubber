package com.mikebuyshouses.dncscrubber.csvmanip

import com.mikebuyshouses.dncscrubber.models.BaseDataRowModel
import com.mikebuyshouses.dncscrubber.models.PhoneModel

class DNCScrubber {
	public List<BaseDataRowModel> scrubCsvData(List<BaseDataRowModel> csvData) { 
		csvData.eachWithIndex { BaseDataRowModel dataRowModel, int idx ->
			println "Scrubbing DNCs from row #${idx + 1}...";
			this.scrubDataRow(dataRowModel);
			println "DNCs have been removed from row #${idx + 1}!";
		}

		println "Removing rows that have no phone numbers...";
		List<Integer> rowIndicesToRemove = csvData
			.findIndexValues { BaseDataRowModel dataRowModel ->
				return dataRowModel.childPhoneModels.size() == 0;
			}
			.collect { number -> return (int)number; }
		rowIndicesToRemove.reverse()
			.each { int rowIdx ->
				csvData.removeAt(rowIdx);
			}

		return csvData;
	}

	public BaseDataRowModel scrubDataRow(BaseDataRowModel dataRowModel) {
		List<Integer> childModelIndicesToRemove = dataRowModel.childPhoneModels
			.findIndexValues { PhoneModel childPhoneModel ->
				return childPhoneModel.isDNC;
			}
			.collect {number -> return (int)number; }

		childModelIndicesToRemove.reverse()
			.each { int idxToRemove ->
				dataRowModel.childPhoneModels.removeAt(idxToRemove);
			}

		return dataRowModel;
	}
}
