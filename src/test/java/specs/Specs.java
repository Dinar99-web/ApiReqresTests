package specs;

import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.http.ContentType.JSON;

public class Specs {
    private static RequestSpecification requestSpec;

    @BeforeAll
    public static void setupConfig() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";

        requestSpec = given()
                .log().all()
                .contentType(JSON)
                .header("x-api-key", "reqres-free-v1");
    }

    public static RequestSpecification getRequestSpec() {
        if (requestSpec == null) {
            setupConfig();
        }
        return requestSpec;
    }

    public static ResponseSpecification getResponseSpec(int expectedStatusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(expectedStatusCode)
                .log(ALL)
                .build();
    }
}