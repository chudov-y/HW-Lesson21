import models.lombok.LoginBodyLombokModel;
import models.lombok.LoginResponseLombokModel;
import models.pojo.LoginBodyPojoModel;
import models.pojo.ResponseBodyPojoModel;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.LoginSpecs.*;


public class RestApiTest extends TestBase {

    @Test
    void createUsersTest() {
        LoginBodyPojoModel authBody = new LoginBodyPojoModel();
        authBody.setName("morpheus");
        authBody.setJob("leader");

        ResponseBodyPojoModel response = step("Create user request", () ->
                given(loginRequestSpec)
                        .body(authBody)
                        .when()
                        .post("/users")
                        .then()
                        .spec(createUserResponseSpec)
                        .extract().as(ResponseBodyPojoModel.class));

        step("Verify response", () -> {
            assertEquals("morpheus", response.getName());
            assertEquals("leader", response.getJob());
        });

    }

    @Test
    void registerSuccessfulTest() {
        LoginBodyLombokModel authBody = new LoginBodyLombokModel();
        authBody.setEmail("eve.holt@reqres.in");
        authBody.setPassword("pistol");

        LoginResponseLombokModel response = step("Make register request", () ->
                given(loginRequestSpec)
                        .body(authBody)
                        .when()
                        .post("/register")
                        .then()
                        .spec(registerSuccessResponseSpec)
                        .extract().as(LoginResponseLombokModel.class));

        step("Verify response", () -> {
            assertEquals("4", response.getId());
            assertEquals("QpwL5tke4Pnpja7X4", response.getToken());

        });
    }

    @Test
    void registerUnSuccessfulTest() {
        LoginBodyLombokModel authBody = new LoginBodyLombokModel();
        authBody.setEmail("sydney@fife");

        LoginResponseLombokModel response = step("Make register request", () ->
                given(loginRequestSpec)
                        .body(authBody)
                        .when()
                        .post("/register")
                        .then()
                        .spec(missingPasswordResponseSpec)
                        .extract().as(LoginResponseLombokModel.class));

        step("Verify response", () ->
                assertEquals("Missing password", response.getError()));
    }
}
