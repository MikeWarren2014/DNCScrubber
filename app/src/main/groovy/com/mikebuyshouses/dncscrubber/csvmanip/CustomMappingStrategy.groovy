package com.mikebuyshouses.dncscrubber.csvmanip

import com.opencsv.bean.ColumnPositionMappingStrategy
import com.opencsv.exceptions.CsvRequiredFieldEmptyException

class CustomMappingStrategy extends ColumnPositionMappingStrategy {
    @Override
    String[] generateHeader(Object bean) throws CsvRequiredFieldEmptyException {
        String[] defaultHeader = super.generateHeader(bean);

        /***
         * BatchSkipTracing writes the columns as follows:
         *  - Input_* fields
         *  - Mailing_* fields
         *  - Phone${n}_* fields
         *  - Email* fields
         *  - Owner_* fields
         *  - Data_* fields
         *
         *  We may choose to follow this exact convention, or use our own
         */
        throw new Exception("need to move the header entries around, to how BatchSkipTracing has them")
        throw new Exception("need to call this.headerIndex.initializeHeaderIndex() with the new header array")
    }
}