import io.qameta.allure.*;
import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static specs.Specs.*;

@Feature("Reqres API Tests")
@Story("User Operations")
@Owner("YourName")
public class RegresTests {

    @Test
    @DisplayName("Получение списка пользователей")
    @Severity(SeverityLevel.BLOCKER)
    void testGetUsersList() {
        UserListResponse response = given(request)
                .when()
                .get("/users?page=2")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .extract().as(UserListResponse.class);

        assertThat(response.getPage()).isEqualTo(2);
        assertThat(response.getData()).isNotEmpty();
    }

    @Test
    @DisplayName("Получение одного пользователя")
    @Severity(SeverityLevel.CRITICAL)
    void testGetSingleUser() {
        UserData response = given(request)
                .when()
                .get("/users/2")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .extract().as(UserData.class);

        User user = response.getData();
        assertThat(user.getId()).isEqualTo(2);
        assertThat(user.getEmail()).isEqualTo("janet.weaver@reqres.in");
        assertThat(user.getFirst_name()).isEqualTo("Janet");
        assertThat(user.getLast_name()).isEqualTo("Weaver");
    }

    @Test
    @DisplayName("Пользователь не найден")
    @Severity(SeverityLevel.NORMAL)
    void testGetSingleUserNotFound() {
        given(request)
                .when()
                .get("/users/23")
                .then()
                .spec(responseSpec)
                .statusCode(404)
                .body(equalTo("{}"));
    }

    @Test
    @DisplayName("Создание пользователя")
    @Severity(SeverityLevel.CRITICAL)
    void testCreateUser() {
        UserRequest userRequest = new UserRequest()
                .setName("morpheus")
                .setJob("leader");

        UserResponse response = given(request)
                .body(userRequest)
                .when()
                .post("/users")
                .then()
                .spec(responseSpec)
                .statusCode(201)
                .extract().as(UserResponse.class);

        assertThat(response.getName()).isEqualTo("morpheus");
        assertThat(response.getJob()).isEqualTo("leader");
        assertThat(response.getId()).isNotBlank();
        assertThat(response.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Обновление пользователя (PUT)")
    @Severity(SeverityLevel.CRITICAL)
    void testUpdateUser() {
        UserRequest userRequest = new UserRequest()
                .setName("morpheus")
                .setJob("zion resident");

        UserResponse response = given(request)
                .body(userRequest)
                .when()
                .put("/users/2")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .extract().as(UserResponse.class);

        assertThat(response.getName()).isEqualTo("morpheus");
        assertThat(response.getJob()).isEqualTo("zion resident");
        assertThat(response.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Частичное обновление пользователя (PATCH)")
    @Severity(SeverityLevel.CRITICAL)
    void testUpdateUserWithPatch() {
        UserRequest userRequest = new UserRequest()
                .setJob("leader of rebels");

        UserResponse response = given(request)
                .body(userRequest)
                .when()
                .patch("/users/2")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .extract().as(UserResponse.class);

        assertThat(response.getJob()).isEqualTo("leader of rebels");
        assertThat(response.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Удаление пользователя")
    @Severity(SeverityLevel.CRITICAL)
    void testDeleteUser() {
        given(request)
                .when()
                .delete("/users/2")
                .then()
                .spec(responseSpec)
                .statusCode(204);
    }
}