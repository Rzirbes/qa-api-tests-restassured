
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import models.*;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class BookingTests {

    protected static Auth auth;
    protected static Booking booking;
    protected static BookingDates bookingDates;
    protected static String token;
    protected static Integer bookingId;
    private RequestSpecification request;


    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        auth = new Auth("admin", "password123");

        bookingDates = new BookingDates("2026-04-10", "2026-04-15");

        booking = new Booking(
                "Romulo",
                "Zirbes",
                1500,
                true,
                bookingDates,
                "Breakfast"
        );

        RestAssured.filters(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }

    @BeforeEach
    void setRequest() {
        request = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new ErrorLoggingFilter())
                .build()
                .auth()
                .preemptive()
                .basic("admin", "password123");
    }

    @Test
    @Order(1)
    public void shouldCreateBookingSuccessfully() {
        BookingResponse response =
                given()
                        .contentType(ContentType.JSON)
                        .body(booking)
                        .when()
                        .post("/booking")
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(BookingResponse.class);

        bookingId = response.getBookingid();

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBookingid());
        Assertions.assertNotNull(response.getBooking());

        Assertions.assertEquals(booking.getFirstname(), response.getBooking().getFirstname());
        Assertions.assertEquals(booking.getLastname(), response.getBooking().getLastname());
        Assertions.assertEquals(booking.getTotalprice(), response.getBooking().getTotalprice());
        Assertions.assertEquals(booking.isDepositpaid(), response.getBooking().isDepositpaid());
        Assertions.assertEquals(booking.getAdditionalneeds(), response.getBooking().getAdditionalneeds());
        Assertions.assertEquals(booking.getBookingdates().getCheckin(), response.getBooking().getBookingdates().getCheckin());
        Assertions.assertEquals(booking.getBookingdates().getCheckout(), response.getBooking().getBookingdates().getCheckout());
    }

    @Test
    @Order(2)
    public void shouldGetBookingIdsSuccessfully() {
        List<BookingIdResponse> response =
                given()
                        .contentType(ContentType.JSON)
                        .when()
                        .get("/booking")
                        .then()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .getList("", BookingIdResponse.class);

        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isEmpty());

        Assertions.assertNotNull(response.get(0).getBookingid());
    }

    @Test
    @Order(3)
    public void shouldGetBookingIdsByFirstnameAndLastnameSuccessfully() {
        List<BookingIdResponse> response =
                given()
                        .contentType(ContentType.JSON)
                        .queryParam("firstname", booking.getFirstname())
                        .queryParam("lastname", booking.getLastname())
                        .when()
                        .get("/booking")
                        .then()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .getList("", BookingIdResponse.class);

        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isEmpty());
        Assertions.assertTrue(
                response.stream().anyMatch(item -> item.getBookingid() != null)
        );
    }

    @Test
    @Order(4)
    public void shouldGetBookingByIdSuccessfully() {

        Booking response =
                given()
                        .contentType(ContentType.JSON)
                        .pathParam("id", bookingId)
                        .when()
                        .get("/booking/{id}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(Booking.class);

        Assertions.assertNotNull(response);

        Assertions.assertEquals(booking.getFirstname(), response.getFirstname());
        Assertions.assertEquals(booking.getLastname(), response.getLastname());
        Assertions.assertEquals(booking.getTotalprice(), response.getTotalprice());
        Assertions.assertEquals(booking.isDepositpaid(), response.isDepositpaid());
        Assertions.assertEquals(booking.getAdditionalneeds(), response.getAdditionalneeds());

        Assertions.assertEquals(
                booking.getBookingdates().getCheckin(),
                response.getBookingdates().getCheckin()
        );

        Assertions.assertEquals(
                booking.getBookingdates().getCheckout(),
                response.getBookingdates().getCheckout()
        );
    }

}