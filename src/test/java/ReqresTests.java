import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import specs.Specs;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@Feature("Reqres API Tests")
@Story("User Operations")
@Owner("Dinar Aminev")
public class ReqresTests extends TestBase {

    @Test
    @Tag("ApiTests")
    @DisplayName("Получение списка пользователей (GET)")
    @Severity(SeverityLevel.BLOCKER)
    void getUsersListTest() {
        Allure.step("Подготовка запроса: GET /users?page=2", () -> {
            ValidatableResponse response = given()
                    .queryParam("page", 2)
                    .when()
                    .get("/users")
                    .then()
                    .spec(Specs.responseSpec(200, ContentType.JSON));

            Allure.step("Проверка структуры ответа", () -> {
                UserListResponseModel model = response.extract().as(UserListResponseModel.class);

                assertThat(model.getPage()).isEqualTo(2);
                assertThat(model.getPerPage()).isPositive();
                assertThat(model.getTotal()).isPositive();
                assertThat(model.getTotalPages()).isPositive();
            });

            Allure.step("Проверка данных пользователей", () -> {
                UserListResponseModel model = response.extract().as(UserListResponseModel.class);

                assertThat(model.getData())
                        .isNotEmpty()
                        .allMatch(user -> user.getId() != null)
                        .allMatch(user -> user.getEmail() != null && user.getEmail().contains("@"));
            });

            Allure.step("Проверка support-блока", () -> {
                UserListResponseModel model = response.extract().as(UserListResponseModel.class);

                assertThat(model.getSupport().getUrl()).isNotBlank();
                assertThat(model.getSupport().getText()).isNotBlank();
            });
        });
    }

    @Test
    @Tag("ApiTests")
    @DisplayName("Получение одного пользователя (GET)")
    @Severity(SeverityLevel.CRITICAL)
    void getSingleUserTest() {
        Allure.step("Отправка запроса: GET /users/2", () -> {
            ValidatableResponse response = given()
                    .when()
                    .get("/users/2")
                    .then()
                    .spec(Specs.responseSpec(200, ContentType.JSON));

            Allure.step("Проверка данных пользователя", () -> {
                UserDataModel model = response.extract().as(UserDataModel.class);
                UserModel user = model.getData();

                assertThat(user.getId()).isEqualTo(2);
                assertThat(user.getEmail()).isEqualTo("janet.weaver@reqres.in");
                assertThat(user.getFirstName()).isEqualTo("Janet");
                assertThat(user.getLastName()).isEqualTo("Weaver");
                assertThat(user.getAvatar()).matches("https://.*\\.jpg");
            });

            Allure.step("Проверка support-блока", () -> {
                UserDataModel model = response.extract().as(UserDataModel.class);
                assertThat(model.getSupport().getUrl()).isNotBlank();
                assertThat(model.getSupport().getText()).isNotBlank();
            });
        });
    }

    @Test
    @Tag("ApiTests")
    @DisplayName("Пользователь не найден (GET)")
    @Severity(SeverityLevel.NORMAL)
    void getSingleUserNotFoundTest() {
        Allure.step("Отправка запроса: GET /users/23", () -> {
            given()
                    .when()
                    .get("/users/23")
                    .then()
                    .spec(Specs.responseSpec(404))
                    .body(is(equalTo("{}")));
        });
    }

    @Test
    @Tag("ApiTests")
    @DisplayName("Создание пользователя (POST)")
    @Severity(SeverityLevel.CRITICAL)
    void createUserTest() {
        Allure.step("Подготовка тестовых данных", () -> {
            UserRequestModel userRequest = new UserRequestModel()
                    .setName("morpheus")
                    .setJob("leader");

            Allure.step("Отправка запроса: POST /users", () -> {
                ValidatableResponse response = given()
                        .body(userRequest)
                        .when()
                        .post("/users")
                        .then()
                        .spec(Specs.responseSpec(201, ContentType.JSON));

                Allure.step("Проверка ответа", () -> {
                    UserResponseModel responseModel = response.extract().as(UserResponseModel.class);

                    assertThat(responseModel.getName()).isEqualTo("morpheus");
                    assertThat(responseModel.getJob()).isEqualTo("leader");
                    assertThat(responseModel.getId()).matches("\\d+");
                    assertThat(responseModel.getCreatedAt()).isNotNull();
                });
            });
        });
    }

    @Test
    @Tag("ApiTests")
    @DisplayName("Обновление пользователя (PUT)")
    @Severity(SeverityLevel.CRITICAL)
    void updateUserTest() {
        Allure.step("Подготовка тестовых данных", () -> {
            UserRequestModel userRequest = new UserRequestModel()
                    .setName("morpheus")
                    .setJob("zion resident");

            Allure.step("Отправка запроса: PUT /users/2", () -> {
                ValidatableResponse response = given()
                        .body(userRequest)
                        .when()
                        .put("/users/2")
                        .then()
                        .spec(Specs.responseSpec(200, ContentType.JSON));

                Allure.step("Проверка ответа", () -> {
                    UserResponseModel responseModel = response.extract().as(UserResponseModel.class);

                    assertThat(responseModel.getName()).isEqualTo("morpheus");
                    assertThat(responseModel.getJob()).isEqualTo("zion resident");
                    assertThat(responseModel.getUpdatedAt()).isNotNull();
                });
            });
        });
    }

    @Test
    @Tag("ApiTests")
    @DisplayName("Удаление пользователя (DELETE)")
    @Severity(SeverityLevel.CRITICAL)
    void deleteUserTest() {
        Allure.step("Отправка запроса: DELETE /users/2", () -> {
            given()
                    .when()
                    .delete("/users/2")
                    .then()
                    .spec(Specs.responseSpec(204));
        });
    }

    @Test
    @Tag("ApiTests")
    @DisplayName("Успешная регистрация (POST)")
    @Severity(SeverityLevel.CRITICAL)
    void registerSuccessfulTest() {
        Allure.step("Подготовка тестовых данных", () -> {
            RegisterRequestModel request = new RegisterRequestModel()
                    .setEmail("eve.holt@reqres.in")
                    .setPassword("pistol");

            Allure.step("Отправка запроса: POST /register", () -> {
                ValidatableResponse response = given()
                        .body(request)
                        .when()
                        .post("/register")
                        .then()
                        .spec(Specs.responseSpec(200, ContentType.JSON));

                Allure.step("Проверка ответа", () -> {
                    response.body("id", is(4))
                            .body("token", notNullValue())
                            .body("token", matchesRegex("^[a-zA-Z0-9]{15,}$"));
                });
            });
        });
    }

    @Test
    @Tag("ApiTests")
    @DisplayName("Неуспешная регистрация (POST)")
    @Severity(SeverityLevel.NORMAL)
    void registerUnsuccessfulTest() {
        Allure.step("Подготовка тестовых данных (без пароля)", () -> {
            RegisterRequestModel request = new RegisterRequestModel()
                    .setEmail("sydney@fife");

            Allure.step("Отправка запроса: POST /register", () -> {
                given()
                        .body(request)
                        .when()
                        .post("/register")
                        .then()
                        .spec(Specs.responseSpec(400, ContentType.JSON))
                        .body("error", is("Missing password"));
            });
        });
    }
}