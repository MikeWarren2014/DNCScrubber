package com.mikebuyshouses.dncscrubber

import com.mikebuyshouses.dncscrubber.csvmanip.CSVSheetReader
import com.mikebuyshouses.dncscrubber.models.TestDataRowModel
import spock.lang.Specification

class CSVSheetReaderTest extends Specification { 
	def "should read the CSV file containing a list of child objects"() {
		setup:
		CSVSheetReader reader = new CSVSheetReader(TestDataRowModel.class)

		when:
		def result = reader.read('src/test/resources/testMultiplePhoneNumbers.csv')

		then:
		def firstResult = result[0]

		firstResult.childPhoneModels.size() == 3
		
		firstResult.childPhoneModels[0].phoneNumber == '3172830074'
		firstResult.childPhoneModels[1].phoneNumber == '3179267430'
		firstResult.childPhoneModels[2].phoneNumber == '3179265508'
	}

	def "should read the CSV file containing raw data for child objects"() {
		setup:
        CSVSheetReader reader = new CSVSheetReader(TestDataRowModel.class)

		when:
		def result = reader.read('src/test/resources/testMultiplePhoneNumbers.csv')

		then:
		result[0].rawPhoneData.size() == 4 * 5
	}
} 