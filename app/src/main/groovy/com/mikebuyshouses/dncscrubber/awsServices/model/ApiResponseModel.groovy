package com.mikebuyshouses.dncscrubber.awsServices.model

class ApiResponseModel {
    String bodyString;
    int statusCode;

    ApiResponseModel(String bodyString, int statusCode) {
        this.bodyString = bodyString
        this.statusCode = statusCode
    }

    String getBodyString() {
        return bodyString
    }

    void setBodyString(String bodyString) {
        this.bodyString = bodyString
    }

    int getStatusCode() {
        return statusCode
    }

    void setStatusCode(int statusCode) {
        this.statusCode = statusCode
    }
}
