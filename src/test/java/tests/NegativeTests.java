package tests;

import base.BaseTest;
import models.Booking;
import models.BookingDates;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

public class NegativeTests extends BaseTest {

    @Test
    public void updateWithoutTokenReturns403() {
        BookingDates dates = new BookingDates("2026-11-01", "2026-11-05");
        int bookingId = createBooking(new Booking(
                "John", "Doe", 150, true, dates, "Breakfast"));

        Booking updated = new Booking(
                "Jane", "Doe", 200, false, dates, "Dinner");

        given()
                .contentType("application/json")
                .pathParam("id", bookingId)
                .body(updated)
                .when()
                .put("/booking/{id}")
                .then()
                .statusCode(403);
    }

    @Test
    public void getNonExistentBookingReturns404() {
        given()
                .pathParam("id", 999999999)
                .when()
                .get("/booking/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    public void createBookingWithMissingFieldsReturns400() {
        String invalidBody = """
                {
                  "firstname": "John"
                }
                """;

        given()
                .contentType("application/json")
                .body(invalidBody)
                .when()
                .post("/booking")
                .then()
                .log().body()
                .statusCode(400);
    }
}
