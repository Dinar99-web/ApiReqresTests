import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import specs.Specs;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Feature("Reqres API Tests")
@Story("User Operations")
@Owner("YourName")
public class ReqresTests {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
        RestAssured.requestSpecification = given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("x-api-key", "reqres-free-v1");
    }

    @Test
    @Tag("HomeWork")
    @DisplayName("Получение списка пользователей")
    @Severity(SeverityLevel.BLOCKER)
    void getUsersListTest() {
        UserListResponseModel response = given()
                .when()
                .get("/users?page=2")
                .then()
                .spec(Specs.responseSpec(200))
                .extract().as(UserListResponseModel.class);

        assertThat(response.getPage()).isEqualTo(2);
        assertThat(response.getData()).isNotEmpty();
    }

    @Test
    @Tag("HomeWork")
    @DisplayName("Получение одного пользователя")
    @Severity(SeverityLevel.CRITICAL)
    void getSingleUserTest() {
        UserDataModel response = given()
                .when()
                .get("/users/2")
                .then()
                .spec(Specs.responseSpec(200))
                .extract().as(UserDataModel.class);

        UserModel user = response.getData();
        assertThat(user.getId()).isEqualTo(2);
        assertThat(user.getEmail()).isEqualTo("janet.weaver@reqres.in");
        assertThat(user.getFirstName()).isEqualTo("Janet");
        assertThat(user.getLastName()).isEqualTo("Weaver");
    }

    @Test
    @Tag("HomeWork")
    @DisplayName("Пользователь не найден")
    @Severity(SeverityLevel.NORMAL)
    void getSingleUserNotFoundTest() {
        given()
                .when()
                .get("/users/23")
                .then()
                .spec(Specs.responseSpec(404))
                .body(equalTo("{}"));
    }

    @Test
    @Tag("HomeWork")
    @DisplayName("Создание пользователя")
    @Severity(SeverityLevel.CRITICAL)
    void createUserTest() {
        UserRequestModel userRequest = new UserRequestModel()
                .setName("morpheus")
                .setJob("leader");

        UserResponseModel response = given()
                .body(userRequest)
                .when()
                .post("/users")
                .then()
                .spec(Specs.responseSpec(201))
                .extract().as(UserResponseModel.class);

        assertThat(response.getName()).isEqualTo("morpheus");
        assertThat(response.getJob()).isEqualTo("leader");
        assertThat(response.getId()).isNotBlank();
        assertThat(response.getCreatedAt()).isNotNull();
    }

    @Test
    @Tag("HomeWork")
    @DisplayName("Обновление пользователя (PUT)")
    @Severity(SeverityLevel.CRITICAL)
    void updateUserTest() {
        UserRequestModel userRequest = new UserRequestModel()
                .setName("morpheus")
                .setJob("zion resident");

        UserResponseModel response = given()
                .body(userRequest)
                .when()
                .put("/users/2")
                .then()
                .spec(Specs.responseSpec(200))
                .extract().as(UserResponseModel.class);

        assertThat(response.getName()).isEqualTo("morpheus");
        assertThat(response.getJob()).isEqualTo("zion resident");
        assertThat(response.getUpdatedAt()).isNotNull();
    }

    @Test
    @Tag("HomeWork")
    @DisplayName("Частичное обновление пользователя (PATCH)")
    @Severity(SeverityLevel.CRITICAL)
    void patchUserTest() {
        UserRequestModel userRequest = new UserRequestModel()
                .setJob("leader of rebels");

        UserResponseModel response = given()
                .body(userRequest)
                .when()
                .patch("/users/2")
                .then()
                .spec(Specs.responseSpec(200))
                .extract().as(UserResponseModel.class);

        assertThat(response.getJob()).isEqualTo("leader of rebels");
        assertThat(response.getUpdatedAt()).isNotNull();
    }

    @Test
    @Tag("HomeWork")
    @DisplayName("Удаление пользователя")
    @Severity(SeverityLevel.CRITICAL)
    void deleteUserTest() {
        given()
                .when()
                .delete("/users/2")
                .then()
                .spec(Specs.responseSpec(204));
    }

    @Test
    @Tag("Additional")
    @DisplayName("Успешная регистрация")
    @Severity(SeverityLevel.CRITICAL)
    void registerSuccessfulTest() {
        String requestBody = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\"}";

        given()
                .body(requestBody)
                .when()
                .post("/register")
                .then()
                .spec(Specs.responseSpec(200))
                .body("id", equalTo(4))
                .body("token", equalTo("QpwL5tke4Pnpja7X4"));
    }

    @Test
    @Tag("Additional")
    @DisplayName("Неуспешная регистрация")
    @Severity(SeverityLevel.NORMAL)
    void registerUnsuccessfulTest() {
        String requestBody = "{\"email\": \"sydney@fife\"}";

        given()
                .body(requestBody)
                .when()
                .post("/register")
                .then()
                .spec(Specs.responseSpec(400))
                .body("error", equalTo("Missing password"));
    }
}