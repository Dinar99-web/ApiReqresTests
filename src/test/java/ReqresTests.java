import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import specs.Specs;

import static helpers.CustomApiListener.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@Feature("Reqres API Tests")
@Story("User Operations")
@Owner("Dinar Aminev")
public class ReqresTests {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
        RestAssured.requestSpecification = given()
                .filter(withCustomTemplates())
                .contentType(ContentType.JSON)
                .header("x-api-key", "reqres-free-v1")
                .log().all();
    }

    @Test
    @Tag("ApiTests")
    @DisplayName("Получение списка пользователей (GET)")
    @Severity(SeverityLevel.BLOCKER)
    void getUsersListTest() {
        UserListResponseModel response = given()
                .queryParam("page", 2)
                .when()
                .get("/users")
                .then()
                .spec(Specs.responseSpec(200, ContentType.JSON))
                .extract().as(UserListResponseModel.class);

        assertThat(response.getPage()).isEqualTo(2);
        assertThat(response.getPerPage()).isPositive();
        assertThat(response.getTotal()).isPositive();
        assertThat(response.getTotalPages()).isPositive();
        assertThat(response.getData())
                .isNotEmpty()
                .allMatch(user -> user.getId() != null)
                .allMatch(user -> user.getEmail() != null && user.getEmail().contains("@"));

        assertThat(response.getSupport().getUrl()).isNotBlank();
        assertThat(response.getSupport().getText()).isNotBlank();
    }

    @Test
    @Tag("ApiTests")
    @DisplayName("Получение одного пользователя (GET)")
    @Severity(SeverityLevel.CRITICAL)
    void getSingleUserTest() {
        UserDataModel response = given()
                .when()
                .get("/users/2")
                .then()
                .spec(Specs.responseSpec(200, ContentType.JSON))
                .extract().as(UserDataModel.class);

        UserModel user = response.getData();
        assertThat(user.getId()).isEqualTo(2);
        assertThat(user.getEmail()).isEqualTo("janet.weaver@reqres.in");
        assertThat(user.getFirstName()).isEqualTo("Janet");
        assertThat(user.getLastName()).isEqualTo("Weaver");
        assertThat(user.getAvatar()).matches("https://.*\\.jpg");

        assertThat(response.getSupport().getUrl()).isNotBlank();
        assertThat(response.getSupport().getText()).isNotBlank();
    }

    @Test
    @Tag("ApiTests")
    @DisplayName("Пользователь не найден (GET)")
    @Severity(SeverityLevel.NORMAL)
    void getSingleUserNotFoundTest() {
        given()
                .when()
                .get("/users/23")
                .then()
                .spec(Specs.responseSpec(404))
                .body(is(equalTo("{}")));
    }

    @Test
    @Tag("ApiTests")
    @DisplayName("Создание пользователя (POST)")
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
                .spec(Specs.responseSpec(201, ContentType.JSON))
                .extract().as(UserResponseModel.class);

        assertThat(response.getName()).isEqualTo("morpheus");
        assertThat(response.getJob()).isEqualTo("leader");
        assertThat(response.getId()).matches("\\d+");
        assertThat(response.getCreatedAt()).isNotNull();
    }

    @Test
    @Tag("ApiTests")
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
                .spec(Specs.responseSpec(200, ContentType.JSON))
                .extract().as(UserResponseModel.class);

        assertThat(response.getName()).isEqualTo("morpheus");
        assertThat(response.getJob()).isEqualTo("zion resident");
        assertThat(response.getUpdatedAt()).isNotNull();
    }

    @Test
    @Tag("ApiTests")
    @DisplayName("Удаление пользователя (DELETE)")
    @Severity(SeverityLevel.CRITICAL)
    void deleteUserTest() {
        given()
                .when()
                .delete("/users/2")
                .then()
                .spec(Specs.responseSpec(204));
    }

    @Test
    @Tag("ApiTests")
    @DisplayName("Успешная регистрация (POST)")
    @Severity(SeverityLevel.CRITICAL)
    void registerSuccessfulTest() {
        RegisterRequestModel request = new RegisterRequestModel()
                .setEmail("eve.holt@reqres.in")
                .setPassword("pistol");

        given()
                .body(request)
                .when()
                .post("/register")
                .then()
                .spec(Specs.responseSpec(200, ContentType.JSON))
                .body("id", is(4))
                .body("token", notNullValue())
                .body("token", matchesRegex("^[a-zA-Z0-9]{15,}$"));
    }

    @Test
    @Tag("ApiTests")
    @DisplayName("Неуспешная регистрация (POST)")
    @Severity(SeverityLevel.NORMAL)
    void registerUnsuccessfulTest() {
        RegisterRequestModel request = new RegisterRequestModel()
                .setEmail("sydney@fife");

        given()
                .body(request)
                .when()
                .post("/register")
                .then()
                .spec(Specs.responseSpec(400, ContentType.JSON))
                .body("error", is("Missing password"));
    }
}