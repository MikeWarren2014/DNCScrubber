package com.mikebuyshouses.dncscrubber.awsServices.providers

import com.mikebuyshouses.dncscrubber.utils.FileUtils

class TestPathProvider extends PathProvider {
    TestPathProvider() {
        super(FileUtils.TestDirectoryPath);
    }

    @Override
    String getBaseInputPath() {
        return "${FileUtils.TestDirectoryPath}/input";
    }

    @Override
    String getBaseOutputPath() {
        return "${FileUtils.TestDirectoryPath}/output";
    }
}
