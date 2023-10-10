package com.mikebuyshouses.dncscrubber.awsServices.providers

import com.mikebuyshouses.dncscrubber.constants.Constants
import com.mikebuyshouses.dncscrubber.utils.FileUtils

class PathProvider {
    final String basePath;

    PathProvider() {
        this.basePath = '/mnt/dncScrubber';
    }

    PathProvider(String basePath) {
        this.basePath = basePath
    }

    String getBaseInputPath() {
        return "${this.basePath}/${FileUtils.InputPathPart}";
    }

    String getBaseOutputPath() {
        return "${this.basePath}/${FileUtils.OutputPathPart}";
    }
}
