package com.mikebuyshouses.dncscrubber.awsServices

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.LambdaLogger
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.mikebuyshouses.dncscrubber.awsServices.models.RequestBodyModel
import com.mikebuyshouses.dncscrubber.awsServices.providers.PathProvider
import com.mikebuyshouses.dncscrubber.csvmanip.CSVSheetReader
import com.mikebuyshouses.dncscrubber.datamanip.DNCScrubber
import com.mikebuyshouses.dncscrubber.filemanip.DataWriterFactory
import com.mikebuyshouses.dncscrubber.models.BatchSkipTracingDataRowModel
import com.mikebuyshouses.dncscrubber.utils.FileUtils
import com.mikebuyshouses.dncscrubber.utils.StringUtils
import delight.fileupload.FileUpload
import org.apache.commons.fileupload.FileItem

class ApiRequestHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public static final String MultipartContentType = "multipart/form-data";
    PathProvider pathProvider = new PathProvider();

    @Override
    APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {
        LambdaLogger logger = context.getLogger()

        // validate the request

        // for right now, we *only* support multipart form-data requests
        if (!requestEvent.getHeaders().get('Content-Type').startsWith(this.MultipartContentType))
            return this.createBadRequestResponseEvent("Request should have multipart/form-data in its headers")

        // parse the request body
        RequestBodyModel requestBodyModel = this.parseRequestEvent(requestEvent);
        if (requestBodyModel.inputFile == null)
            return this.createBadRequestResponseEvent("Missing input file from request");

        if (StringUtils.IsNullOrEmpty(requestBodyModel.outputFileExtension))
            return this.createBadRequestResponseEvent("Missing the output file extension from request");

        if (!requestBodyModel.outputFileExtension.toLowerCase().matches(/^(csv|xlsx)$/))
            return this.createBadRequestResponseEvent("Output file extension '${requestBodyModel.outputFileExtension}' not supported");

        File outputFile = this.handleValidRequestBodyModel(requestBodyModel, logger);

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withHeaders(this.createResponseHeaders(outputFile.getName()))
                .withIsBase64Encoded(true)
                .withBody(Base64.getEncoder()
                        .encodeToString(outputFile.bytes))
    }

    APIGatewayProxyResponseEvent createBadRequestResponseEvent(String message) {
        return new APIGatewayProxyResponseEvent()
            .withIsBase64Encoded(false)
            .withStatusCode(400)
            .withBody(message)
    }

    RequestBodyModel parseRequestEvent(APIGatewayProxyRequestEvent request) {
        byte[] bodyBytes = Base64.getDecoder()
                .decode(request.getBody().getBytes("UTF-8"));

        RequestBodyModel model = new RequestBodyModel();

        FileUpload.parse(bodyBytes, request.getHeaders().get('Content-Type'))
            .each { FileItem fileItem ->
                if (fileItem.getFieldName().equals("inputFile")) {
                    File inputFile = FileUtils.CreateFileIfNotExists("${this.pathProvider.getBaseInputPath()}/${fileItem.getName()}");
                    fileItem.write(inputFile);
                    model.inputFile = inputFile;
                    return;
                }

                if (fileItem.getFieldName().equals("outputFileExtension")) {
                    model.outputFileExtension = fileItem.getString();
                    return;
                }

                if (fileItem.getFieldName().equals("shouldExportDncRecords")) {
                    model.shouldExportDncRecords = Boolean.valueOf(fileItem.getString());
                    return;
                }
            }

        return model;
    }

    File handleValidRequestBodyModel(RequestBodyModel requestBodyModel, LambdaLogger logger) {
        logger.log("Reading in the CSV file '${requestBodyModel.inputFile.getPath()}'...")
        List<BatchSkipTracingDataRowModel> csvData = new CSVSheetReader(BatchSkipTracingDataRowModel.class)
                .read(FileUtils.GetInputCsvFileName(requestBodyModel.inputFile.getPath()));

        logger.log("Scrubbing the DNC records...");
        List<BatchSkipTracingDataRowModel> scrubbedCsvData = new DNCScrubber().scrubCsvData(csvData);

        final String outputFileName = requestBodyModel.inputFile.getPath()
                .replace(this.pathProvider.getBaseInputPath(), this.pathProvider.getBaseOutputPath())
                .replace(FileUtils.GetFileExtension(requestBodyModel.inputFile), requestBodyModel.outputFileExtension);

        logger.log("Outputting to output file '${outputFileName}'...");
        DataWriterFactory.GetDataWriter(requestBodyModel.outputFileExtension)
                .write(scrubbedCsvData, outputFileName);

        logger.log("All done!");

        return new File(outputFileName);
    }

    Map<String, String> createResponseHeaders(String outputFileName) {
        return [
                ('Content-Type') : this.getContentTypeHeader(outputFileName),
                ('Content-Disposition') : "attachment; filename=\"${outputFileName}\"".toString(),
        ]
    }

    private String getContentTypeHeader(String outputFileName) {
        final String fileExtension = FileUtils.GetFileExtension(outputFileName).toLowerCase();
        switch (fileExtension) {
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "csv":
                return "text/csv";
        }

        throw new IllegalArgumentException("Content type for extension '${fileExtension}' not yet implemented");
    }

}