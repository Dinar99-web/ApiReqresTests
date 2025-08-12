package specs;

import helpers.CustomApiListener;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specs {
    public static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .addFilter(CustomApiListener.withCustomTemplates())
                .setContentType(ContentType.JSON)
                .addHeader("x-api-key", "reqres-free-v1")
                .log(LogDetail.ALL)
                .build();
    }

    public static ResponseSpecification responseSpec(int statusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .log(LogDetail.ALL)
                .build();
    }

    public static ResponseSpecification responseSpec(int statusCode, ContentType contentType) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .expectContentType(contentType)
                .log(LogDetail.ALL)
                .build();
    }
}