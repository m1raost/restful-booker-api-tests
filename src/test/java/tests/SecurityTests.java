package tests;

import base.BaseTest;
import models.Booking;
import models.BookingDates;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.nullValue;

public class SecurityTests extends BaseTest {

    @Test
    public void updateWithFakeTokenReturns403(){

        BookingDates dates = new BookingDates("2026-11-01","2026-11-05");
        int bookingId = createBooking(new Booking(
                "John", "Doe", 150, true, dates, "Breakfast"));

        Booking updated = new Booking(
                "Jane", "Doe", 200, false, dates, "Dinner");

        given()
                .contentType("application/json")
                .cookie("token", "abc123")
                .pathParam("id", bookingId)
                .body(updated)
                .when()
                .put("/booking/{id}")
                .then()
                .log().body()
                .statusCode(403);

    }

    @Test
    public void loginWithWrongPasswordReturns401(){

        String invalidCredentials = """
                {
                "username": "admin",
                "password": "wrongpassword"
                }
                """;

        given()
                .contentType("application/json")
                .body(invalidCredentials)
                .when()
                .post("/auth")
                .then()
                .log().body()
                .body("token", nullValue())
                .statusCode(401); // fails: API returns 200 - known bug
    }
}
