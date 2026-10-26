package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class AuthTests extends BaseTest {

    @Test
    public void validCredentialsReturnToken() {
        String credentials = """
                {
                  "username": "admin",
                  "password": "password123"
                }
                """;

        given()
                .contentType("application/json")
                .body(credentials)
                .when()
                .post("/auth")
                .then()
                .log().body()
                .statusCode(200)
                .body("token", notNullValue());
    }
}
