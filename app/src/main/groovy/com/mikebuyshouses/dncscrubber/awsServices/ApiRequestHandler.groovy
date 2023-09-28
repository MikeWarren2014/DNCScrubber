package com.mikebuyshouses.dncscrubber.awsServices

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.mikebuyshouses.dncscrubber.awsServices.model.ApiResponseModel

// TODO: we may be able to replace the dictionary type with a model type...
class ApiRequestHandler implements RequestHandler<Map<String, Object>, ApiResponseModel> {

    // TODO: may want to move this somewhere else
    public static final String BucketName = "dnc-scrubber-bucket";

    @Override
    ApiResponseModel handleRequest(Map<String, Object> input, Context context) {
        throw new Exception("not implemented")

        return null;
    }
}