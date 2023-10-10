package com.mikebuyshouses.dncscrubber.filemanip

import com.mikebuyshouses.dncscrubber.models.BaseDataRowModel

class TestDoubleDataWriter implements DataWriter {
    Closure onWriteToFile;

    TestDoubleDataWriter(Closure onWriteToFile) {
        this.onWriteToFile = onWriteToFile
    }

    @Override
    void write(List<BaseDataRowModel> dataRowModels, String outputFileName) {
        DataWriter.super.write(dataRowModels, outputFileName);
        new File(outputFileName).delete();
    }

    @Override
    void writeToFile(List<BaseDataRowModel> dataRowModels, File file) {
        this.onWriteToFile(dataRowModels);
    }
}
