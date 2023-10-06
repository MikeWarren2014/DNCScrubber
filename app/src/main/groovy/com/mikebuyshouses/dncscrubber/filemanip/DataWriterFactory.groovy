package com.mikebuyshouses.dncscrubber.filemanip

import com.mikebuyshouses.dncscrubber.csvmanip.CSVSheetWriter
import com.mikebuyshouses.dncscrubber.models.BatchSkipTracingDataRowModel

public final class DataWriterFactory {
    public static DataWriter GetDataWriter(String fileExtension) {
        switch (fileExtension) {
            case "csv":
                return new CSVSheetWriter(BatchSkipTracingDataRowModel.class);
            case "xlsx":
                return new ExcelDataWriter();
        }

        throw new IllegalArgumentException("DataWriter for file with extension '${fileExtension}' not yet implemented!")
    }
}
