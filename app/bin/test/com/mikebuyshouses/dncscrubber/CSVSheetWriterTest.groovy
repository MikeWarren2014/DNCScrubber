package com.mikebuyshouses.dncscrubber

import com.mikebuyshouses.dncscrubber.models.TestDataRowModel
import spock.lang.Specification

class CSVSheetWriterTest extends Specification {
    def "should accurately prepare the BaseRowDataModels, by writing their childPhoneModels to rawPhoneData"() {
        setup:
        List<TestDataRowModel> csvData = [
                new TestDataRowModel()
        ]
    }
}
