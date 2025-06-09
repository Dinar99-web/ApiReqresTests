import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

public class RegresTests {
    @Test
    @DisplayName("Получение списка пользователей")
    public void testGetUsersList() {
        RestAssured
                .given()
                .baseUri("https://reqres.in")
                .log().all()
                .when()
                .get("/api/users?page=2")
                .then()
                .statusCode(200)
                .body("page", equalTo(2))
                .body("data", hasSize(greaterThan(0)))
                .log().all();
        }
    @Test
    @DisplayName("Получение одного пользователя")
    public void testGetSingleUser() {
        RestAssured
                .given()
                .baseUri("https://reqres.in")
                .when()
                .get("/api/users/2")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.email", equalTo("janet.weaver@reqres.in"));
    }
    @Test
    @DisplayName("Пользователь не найден")
    public void testGetSingleUserNotFound() {
        RestAssured
                .given()
                .header("x-api-key","reqres-free-v1")
                .baseUri("https://reqres.in")
                .log().all()
                .when()
                .get("/api/users/23")
                .then()
                .statusCode(404)
                .body(equalTo("{}"))
                .log().all();
    }
    @Test
    @DisplayName("Создание пользователя")
    public void testCreateUser() {
        String requestBody = """
        {
            "name": "morpheus",
            "job": "leader"
        }
        """;

        RestAssured
                .given()
                .header("x-api-key","reqres-free-v1")
                .baseUri("https://reqres.in")
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("leader"))
                .body("id", notNullValue());
    }
    @Test
    @DisplayName("Удаление пользователя")
    public void testDeleteUser() {
        RestAssured
                .given()
                .header("x-api-key","reqres-free-v1")
                .baseUri("https://reqres.in")
                .when()
                .delete("/api/users/2")
                .then()
                .statusCode(204);
    }
    @Test
    @DisplayName("Обновление пользователя")
    public void testUpdateUser() {
        String requestBody = """
    {
        "name": "morpheus",
        "job": "zion resident"
    }
    """;

        RestAssured
                .given()
                .header("x-api-key","reqres-free-v1")
                .baseUri("https://reqres.in")
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put("/api/users/2")
                .then()
                .statusCode(200)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("zion resident"))
                .body("updatedAt", notNullValue());
    }

    @Test
    @DisplayName("Частичное обновление пользователя")
    public void testUpdateUserWithPatch() {
        String requestBody = """
    {
        "job": "leader of rebels"
    }
    """;

        RestAssured
                .given()
                .header("x-api-key","reqres-free-v1")
                .baseUri("https://reqres.in")
                .contentType("application/json")
                .body(requestBody)
                .when()
                .patch("/api/users/2")
                .then()
                .statusCode(200)
                .body("job", equalTo("leader of rebels"))
                .body("updatedAt", notNullValue());
    }
}
