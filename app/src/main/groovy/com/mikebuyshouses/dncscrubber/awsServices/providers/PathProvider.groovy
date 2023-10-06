package com.mikebuyshouses.dncscrubber.awsServices.providers

import com.mikebuyshouses.dncscrubber.constants.Constants
import com.mikebuyshouses.dncscrubber.utils.FileUtils

class PathProvider {
    String getBaseInputPath() {
        return "${Constants.EfsMountPath}/${FileUtils.InputPathPart}";
    }

    String getBaseOutputPath() {
        return "${Constants.EfsMountPath}/${FileUtils.OutputPathPart}";
    }
}
