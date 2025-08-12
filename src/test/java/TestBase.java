import helpers.CustomApiListener;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public class TestBase {
    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
        RestAssured.requestSpecification = given()
                .filter(CustomApiListener.withCustomTemplates())
                .contentType(ContentType.JSON)
                .header("x-api-key", "reqres-free-v1")
                .log().all();
    }
}
