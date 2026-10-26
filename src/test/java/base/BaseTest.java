package base;

import io.restassured.RestAssured;
import models.Booking;
import org.testng.annotations.BeforeClass;
import static io.restassured.RestAssured.given;

public class BaseTest {

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
    }

    protected String getToken() {
        String credentials = """
                {
                  "username": "admin",
                  "password": "password123"
                }
                """;

        return given()
                .contentType("application/json")
                .body(credentials)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .extract().path("token");
    }

    protected int createBooking(Booking booking) {
        return given()
                .contentType("application/json")
                .body(booking)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .extract().path("bookingid");
    }
}
