package com.mikebuyshouses.dncscrubber.awsServices

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.mikebuyshouses.dncscrubber.awsServices.providers.TestPathProvider
import com.mikebuyshouses.dncscrubber.utils.FileUtils
import spock.lang.Specification

class ApiRequestHandlerTest extends Specification {
    def "parseRequestEvent should actually write the input file from the request event"() {
        setup:
        def stubRequestEvent = Stub(APIGatewayProxyRequestEvent)
        stubRequestEvent.getHeaders() >> ["Content-Type": "multipart/form-data;boundary=YRPyKRi1Bb3sIDcW"]
        stubRequestEvent.getBody() >> """
------WebKitFormBoundaryYRPyKRi1Bb3sIDcW
Content-Disposition: form-data; name="inputFile"; filename="test.csv"
Content-Type: text/csv

Number,Sale Date,Address,City,State,Zip,Parcel,Township,Removed,Removed Date,Bankruptcy #,Redemption Days,Cause #,Receive Date,Receive Time,Total Judgement,Plaintiff,Et al,Attorney,Phone,User Fee,Sheriff Fee,Ad Cost,Delinquent Tax,Total Fees,Defendant,Judgement,Interest 1,Interest 2,Additional Charges,Lien Holder 1,Lien Holder 1 Judgement,Lien Holder 1 Interest 1,Lien Holder 1 Interest 2,Lien Holder 1 Additional Charges,Lien Holder 2,Lien Holder 2 Judgement,Lien Holder 2 Interest 1,Lien Holder 2 Interest 2,Lien Holder 2 Additional Charges,Lien Holder 3,Lien Holder 3 Judgement,Lien Holder 3 Interest 1,Lien Holder 3 Interest 2,Lien Holder 3 Additional Charges,Lien Holder 4,Lien Holder 4 Judgement,Lien Holder 4 Interest 1,Lien Holder 4 Interest 2,Lien Holder 4 Additional Charges,Lien Holder 5,Lien Holder 5 Judgement,Lien Holder 5 Interest 1,Lien Holder 5 Interest 2,Lien Holder 5 Additional Charges,Lien Holder 6,Lien Holder 6 Judgement,Lien Holder 6 Interest 1,Lien Holder 6 Interest 2,Lien Holder 6 Additional Charges,Lien Holder 7,Lien Holder 7 Judgement,Lien Holder 7 Interest 1,Lien Holder 7 Interest 2,Lien Holder 7 Additional Charges,Lien Holder 8,Lien Holder 8 Judgement,Lien Holder 8 Interest 1,Lien Holder 8 Interest 2,Lien Holder 8 Additional Charges,Lien Holder 9,Lien Holder 9 Judgement,Lien Holder 9 Interest 1,Lien Holder 9 Interest 2,Lien Holder 9 Additional Charges,,Grand Total
1,09/15/2023,1001 Main Street,Indianapolis,IN,46220,,BRO,N,,,,123456789MF123456,07/14/2023,,44202.67,FAKE BANK,,CARLISLE,(216) 555-1234,300,66,345.42,0,711.42,DOUGLAS H LOPEZ,44202.67,0,0,0,,0,0,0,0,,0,0,0,0,,0,0,0,0,,0,0,0,0,,0,0,0,0,,0,0,0,0,,0,0,0,0,,0,0,0,0,,0,0,0,0,,44914.09

------WebKitFormBoundaryYRPyKRi1Bb3sIDcW--
""".bytes.encodeBase64()

        ApiRequestHandler apiRequestHandler = new ApiRequestHandler().with { ApiRequestHandler handler ->
            handler.pathProvider = new TestPathProvider();

            return handler;
        }

        when:
        apiRequestHandler.parseRequestEvent(stubRequestEvent)

        then:
        new File("${FileUtils.TestDirectoryPath}/${FileUtils.InputPathPart}/test.csv").exists()

        cleanup:
        new File("${FileUtils.TestDirectoryPath}/${FileUtils.InputPathPart}/test.csv").delete()
    }

}
