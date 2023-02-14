package org.example.tests.api.v1;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.models.user.UserLocation;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import static org.apache.commons.lang3.RandomStringUtils.*;
import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class PUTUsersTest extends ApiBaseClass {
    @Test
    public void updateUser() {

        JSONObject request = new JSONObject();

        request.put("firstName", randomAlphabetic(5));
        request.put("lastName", randomAlphabetic(5));

        Response response = RestAssured.given()
                .header("app-id", ApiBaseClass.APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .put("/user/60d0fe4f5311236168a109ca");
        logResponse(response);

        //Validate user is updated successfully
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("firstName"), request.get("firstName"));
        assertEquals(path.get("lastName"), request.get("lastName"));

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void updateUserEmail() {

        JSONObject request = new JSONObject();

        request.put("email", randomAlphanumeric(4) + "@mail.com");

        Response response = RestAssured.given()
                .header("app-id", ApiBaseClass.APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .put("/user/60d0fe4f5311236168a109ca");
        logResponse(response);

        //Validate user's email is not updated
        JsonPath path = response.body().jsonPath();
        assertNotEquals(path.get("email"), request.get("email"));

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void updateUserLocation() {

        UserLocation userLocation = new UserLocation();

        JSONObject request = new JSONObject();

        String street = randomAlphabetic(6);
        String city = randomAlphabetic(5);
        String state = randomAlphabetic(7);
        String country = randomAlphabetic(8);
        String timezone = "+8:00";

        userLocation.setStreet(street);
        userLocation.setCity(city);
        userLocation.setState(state);
        userLocation.setCountry(country);
        userLocation.setTimezone(timezone);

        request.put("location", userLocation);


        Response response = RestAssured.given()
                .header("app-id", ApiBaseClass.APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .put("/user/60d0fe4f5311236168a109cd");
        logResponse(response);

        //Validate location information updated
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("location.street"), street);
        assertEquals(path.get("location.city"), city);
        assertEquals(path.get("location.state"), state);
        assertEquals(path.get("location.country"), country);
        assertEquals(path.get("location.timezone"), timezone);

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void updateUserTitle() {

        JSONObject request = new JSONObject();

        request.put("title", "dr");

        Response response = RestAssured.given()
                .header("app-id", ApiBaseClass.APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .put("/user/60d0fe4f5311236168a109ca");
        logResponse(response);

        //Validate user's email is not updated
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("title"), request.get("title"));

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void updateUserWithInvalidDateOfBirth() {

        JSONObject request = new JSONObject();

        request.put("dateOfBirth", "now");

        Response response = RestAssured.given()
                .header("app-id", ApiBaseClass.APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .put("/user/60d0fe4f5311236168a109ca");
        logResponse(response);

        //Validate user's date of birth not updated
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "BODY_NOT_VALID");
        assertEquals(path.get("data.dateOfBirth"), "Cast to date failed for value \"now\" (type string) at path \"dateOfBirth\"");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void updateNewUserWithCharactersForPhone() {

        JSONObject request = new JSONObject();

        request.put("phone", randomAlphabetic(9));


        Response response = RestAssured.given()
                .header("app-id", ApiBaseClass.APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .put("/user/60d0fe4f5311236168a109ca");
        logResponse(response);

        //Validate characters for phone not allowed
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "BODY_NOT_VALID");
        assertEquals(path.get("data.phone"), "Path `phone` is invalid.");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void updateUserWithWrongTimezone() {

        UserLocation userLocation = new UserLocation();

        JSONObject request = new JSONObject();

        String timezone = randomAlphanumeric(4);

        userLocation.setTimezone(timezone);

        request.put("location", userLocation);


        Response response = RestAssured.given()
                .header("app-id", ApiBaseClass.APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .put("/user/60d0fe4f5311236168a109ca");
        logResponse(response);

        //Validate invalid timezone format not allowed
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "BODY_NOT_VALID");
        assertEquals(path.get("location.timezone"), "Path `timezone` is invalid.");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void updateNewUserWithInvalidGender() {

        JSONObject request = new JSONObject();

        String gender = randomAlphanumeric(4);
        request.put("gender", gender);

        Response response = RestAssured.given()
                .header("app-id", ApiBaseClass.APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .put("/user/60d0fe4f5311236168a109ca");
        logResponse(response);

        //Validate invalid gender format not allowed
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "BODY_NOT_VALID");
        assertEquals(path.get("data.gender"), "`" + gender + "`" + " is not a valid enum value for path `gender`.");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void updateUserUsingSpacesForMandatoryFields() {

        JSONObject request = new JSONObject();

        request.put("firstName", " ");
        request.put("lastName", " ");

        Response response = RestAssured.given()
                .header("app-id", ApiBaseClass.APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .put("/user/60d0fe4f5311236168a109ca");
        logResponse(response);

        //Validate empty mandatory fields not allowed
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "BODY_NOT_VALID");
        assertEquals(path.get("data.firstName"), "Path `firstName` is required.");
        assertEquals(path.get("data.lastName"), "Path `lastName` is required.");


        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void updateUserWithEmptyMandatoryFields() {

        JSONObject request = new JSONObject();

        request.put("firstName", "");
        request.put("lastName", "");

        Response response = RestAssured.given()
                .header("app-id", ApiBaseClass.APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .put("/user/60d0fe4f5311236168a109ca");
        logResponse(response);

        //Validate empty mandatory fields not allowed
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("firstName"), "Sara");
        assertEquals(path.get("lastName"), "Andersen");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void updateWithoutAuthorization() {

        JSONObject request = new JSONObject();

        request.put("firstName", "D");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .put("/user/60d0fe4f5311236168a109ca");
        logResponse(response);

        //Validate user not updated without app-id
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "APP_ID_MISSING");


        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_FORBIDDEN);
    }

    @Test
    public void updateUserWithFirstNameLongerThan30Char() {
        JSONObject request = new JSONObject();

        String firstName = randomAlphabetic(31);

        request.put("firstName", firstName);

        Response response = RestAssured.given()
                .header("app-id", ApiBaseClass.APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .put("/user/60d0fe4f5311236168a109ca");
        logResponse(response);

        //Validate firstName longer than 30
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "BODY_NOT_VALID");
        assertEquals(path.get("data.firstName"), "Path `firstName` (`" + firstName + "`) is longer than the maximum allowed length (30).");


        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void updateUserWithLastNameLongerThan30Char() {
        JSONObject request = new JSONObject();

        String lastName = randomAlphabetic(31);

        request.put("lastName", lastName);

        Response response = RestAssured.given()
                .header("app-id", ApiBaseClass.APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .put("/user/60d0fe4f5311236168a109ca");
        logResponse(response);

        //Validate lastName longer than 30
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "BODY_NOT_VALID");
        assertEquals(path.get("data.firstName"), "Path `firstName` (`" + lastName + "`) is longer than the maximum allowed length (30).");


        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void updateUserWithWrongTitleFormat() {

        JSONObject request = new JSONObject();

        request.put("title", "unknown");

        Response response = RestAssured.given()
                .header("app-id", ApiBaseClass.APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .put("/user/60d0fe4f5311236168a109ca");
        logResponse(response);

        //Validate wrong title not allowed
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "BODY_NOT_VALID");
        assertEquals(path.get("data.title"), "`unknown` is not a valid enum value for path `title`.");


        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void updateUserId() {

        JSONObject request = new JSONObject();

        request.put("id", randomNumeric(6));


        Response response = RestAssured.given()
                .header("app-id", ApiBaseClass.APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .put("/user/60d0fe4f5311236168a109ca");
        logResponse(response);

        //Validate autogenerated id remained, instead of the one passed
        JsonPath path = response.body().jsonPath();
        assertNotEquals(path.get("id").toString(), request.get("id"));


        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }
}
