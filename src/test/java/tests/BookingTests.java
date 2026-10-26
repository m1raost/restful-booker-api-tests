package tests;

import base.BaseTest;
import models.Booking;
import models.BookingDates;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BookingTests extends BaseTest {

    @Test
    public void getAllBookingsReturns200(){
        given()
                .when()
                .get("/booking")
                .then()
                .log().body()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    public void createBookingReturnsSameData() {

        BookingDates dates = new BookingDates("2026-11-01", "2026-11-05");
        Booking booking = new Booking(
                "John", "Doe", 150, true, dates, "Breakfast");

        given()
                .contentType("application/json")
                .body(booking)
                .when()
                .post("/booking")
                .then()
                .log().body()
                .statusCode(200)
                .body("bookingid", notNullValue())
                .body("booking.firstname", equalTo("John"))
                .body("booking.totalprice", equalTo(150))
                .body("booking.bookingdates.checkin", equalTo("2026-11-01"));
    }

    @Test
    public void updateBookingChangesData() {
        BookingDates dates = new BookingDates("2026-11-01", "2026-11-05");
        int bookingId = createBooking(new Booking(
                "John", "Doe", 150, true, dates, "Breakfast"));
        String token = getToken();

        Booking updated = new Booking(
                "Jane", "Doe", 200, false, dates, "Dinner");

        given()
                .contentType("application/json")
                .accept("application/json")
                .cookie("token", token)
                .pathParam("id", bookingId)
                .body(updated)
                .when()
                .put("/booking/{id}")
                .then()
                .log().body()
                .statusCode(200)
                .body("firstname", equalTo("Jane"))
                .body("totalprice", equalTo(200))
                .body("additionalneeds", equalTo("Dinner"));
    }
}
