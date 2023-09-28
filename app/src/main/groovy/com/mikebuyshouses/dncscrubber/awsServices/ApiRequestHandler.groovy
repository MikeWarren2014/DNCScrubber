package com.mikebuyshouses.dncscrubber.awsServices

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler

class ApiRequestHandler implements RequestStreamHandler {
    @Override
    void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        // parse the input

    }
}
