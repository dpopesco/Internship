package org.example.tests.api.v1;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.models.user.User;
import org.example.models.user.UserLocation;
import org.json.simple.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.apache.commons.lang3.RandomStringUtils.*;
import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class POSTUsersTest extends ApiBaseClass {

    private JSONObject createRequestObject() {
        String email = randomAlphanumeric(6) + "@mail.com";
        String firstName = randomAlphabetic(5);
        String lastName = randomAlphabetic(6);

        JSONObject request = new JSONObject();
        request.put("firstName", firstName);
        request.put("lastName", lastName);
        request.put("email", email);
        return request;
    }

    @DataProvider(name = "userMandatoryFields")
    public Object[][] createData() {
        return new Object[][]{
                {new User(randomAlphabetic(4), randomAlphabetic(5), randomAlphabetic(5) + "@mail.com")},
                {new User("Milo", "Miloshevici", "milo@mail.com")}

        };
    }

    @Test(dataProvider = "userMandatoryFields")
    public void createUsers(User user) {

        Response response = RestAssured.given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .body(user)
                .post("/user/create");
        logResponse(response);

        //Validate user is created successfully
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("firstName"), user.getFirstName());
        assertEquals(path.get("lastName"), user.getLastName());
        assertEquals(path.get("email"), user.getEmail().toLowerCase());

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_CREATED);
    }

    @Test
    public void createNewUser() {

        JSONObject request = createRequestObject();

        Response response = RestAssured.given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .post("/user/create");
        logResponse(response);

        //Validate user is created successfully
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("firstName"), request.get("firstName"));
        assertEquals(path.get("lastName"), request.get("lastName"));
        assertEquals(path.get("email"), request.get("email").toString().toLowerCase());


        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_CREATED);
    }

    @Test
    public void createNewUserUsingExistingEmail() {

        JSONObject request = new JSONObject();

        request.put("firstName", "Aron");
        request.put("lastName", "Radu");
        request.put("email", "aron@mail.com");

        Response response = RestAssured.given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .post("/user/create");
        logResponse(response);

        //Validate email already used error
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "BODY_NOT_VALID");
        assertEquals(path.get("data.email"), "Email already used");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserUsingSpacesForMandatoryFields() {

        JSONObject request = new JSONObject();

        request.put("firstName", " ");
        request.put("lastName", " ");
        request.put("email", " ");

        Response response = RestAssured.given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .post("/user/create");
        logResponse(response);

        //Validate spaces for mandatory fields not allowed
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "BODY_NOT_VALID");
        assertEquals(path.get("data.email"), "Path `email` is required.");
        assertEquals(path.get("data.firstName"), "Path `firstName` is required.");
        assertEquals(path.get("data.lastName"), "Path `lastName` is required.");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithInvalidEmail() {

        JSONObject request = new JSONObject();

        request.put("firstName", "Mariana");
        request.put("lastName", "Ricky");
        request.put("email", "mariana@mail");

        Response response = RestAssured.given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .post("/user/create");
        logResponse(response);

        //Validate invalid email structure not allowed
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "BODY_NOT_VALID");
        assertEquals(path.get("data.email"), "Path `email` is invalid (mariana@mail).");
        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithMissingMandatoryFields() {

        Response response = RestAssured.given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .post("/user/create");

        logResponse(response);

        //Validate user isn't created without mandatory fields entered
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "BODY_NOT_VALID");
        assertEquals(path.get("data.email"), "Path `email` is required.");
        assertEquals(path.get("data.firstName"), "Path `firstName` is required.");
        assertEquals(path.get("data.lastName"), "Path `lastName` is required.");
        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserUsingXSSForFirstName() {

        JSONObject request = createRequestObject();

        request.put("firstName", "<script>alert(\\'H\\')</script> ");

        Response response = RestAssured.given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .post("/user/create");
        logResponse(response);

        //Validate user is not created when entering xss script for firstName field
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "BODY_NOT_VALID");
        assertEquals(path.get("data.firstName"), "Path `firstName` is invalid (<script>alert('H')</script>).");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithEmptyMandatoryFields() {

        JSONObject request = new JSONObject();

        request.put("firstName", "");
        request.put("lastName", "");
        request.put("email", "");

        Response response = RestAssured.given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .post("/user/create");
        logResponse(response);

        //Validate empty mandatory fields not allowed
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "BODY_NOT_VALID");
        assertEquals(path.get("data.email"), "Path `email` is required.");
        assertEquals(path.get("data.firstName"), "Path `firstName` is required.");
        assertEquals(path.get("data.lastName"), "Path `lastName` is required.");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithoutAuthorization() {

        JSONObject request = createRequestObject();

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/user/create");
        logResponse(response);

        //Validate user not created without app-id
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "APP_ID_MISSING");


        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_FORBIDDEN);
    }

    @Test
    public void createNewUserWithFirstNameLongerThan30Char() {
        JSONObject request = createRequestObject();

        String firstName = randomAlphabetic(31);

        request.put("firstName", firstName);

        Response response = RestAssured.given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .post("/user/create");
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
    public void createNewUserWithLastNameLongerThan30Char() {
        JSONObject request = createRequestObject();

        String lastName = randomAlphabetic(31);

        request.put("lastName", lastName);

        Response response = RestAssured.given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .post("/user/create");
        logResponse(response);

        //Validate lastName longer than 30
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "BODY_NOT_VALID");
        assertEquals(path.get("data.lastName"), "Path `lastName` (`" + lastName + "`) is longer than the maximum allowed length (30).");


        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithEmailLongerThan50Char() {
        JSONObject request = createRequestObject();

        String email = randomAlphanumeric(47) + "@mail.com";

        request.put("email", email);

        Response response = RestAssured.given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .post("/user/create");
        logResponse(response);

        //Validate email longer than 50
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "BODY_NOT_VALID");
        assertEquals(path.get("data.email"), "Path `email` (`" + email + "`) is longer than the maximum allowed length (50).");


        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithWrongTitleFormat() {

        JSONObject request = createRequestObject();

        request.put("title", "unknown");

        Response response = RestAssured.given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .post("/user/create");
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
    public void createNewUserWithId() {

        JSONObject request = createRequestObject();

        request.put("id", randomNumeric(6));


        Response response = RestAssured.given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .post("/user/create");
        logResponse(response);

        //Validate autogenerated id is created, instead of the one passed
        JsonPath path = response.body().jsonPath();
        assertNotEquals(path.get("id").toString(), request.get("id"));


        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_CREATED);
    }

    @Test
    public void createNewUserWithCharactersForPhone() {

        JSONObject request = createRequestObject();

        request.put("phone", randomAlphabetic(9));


        Response response = RestAssured.given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .post("/user/create");
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
    public void createUserWithInvalidDateOfBirth() {

        JSONObject request = createRequestObject();

        request.put("dateOfBirth", "now");

        Response response = RestAssured.given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .post("/user/create");
        logResponse(response);

        //Validate user's email is not updated
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "BODY_NOT_VALID");
        assertEquals(path.get("data.dateOfBirth"), "Cast to date failed for value \"now\" (type string) at path \"dateOfBirth\"");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithLocation() {

        UserLocation userLocation = new UserLocation();

        JSONObject request = createRequestObject();

        String street = randomAlphabetic(6);
        String city = randomAlphabetic(5);
        String state = randomAlphabetic(7);
        String country = randomAlphabetic(8);
        String timezone = "+9:00";

        userLocation.setStreet(street);
        userLocation.setCity(city);
        userLocation.setState(state);
        userLocation.setCountry(country);
        userLocation.setTimezone(timezone);

        request.put("location", userLocation);


        Response response = RestAssured.given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .post("/user/create");
        logResponse(response);

        //Validate location information created
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("location.street"), street);
        assertEquals(path.get("location.city"), city);
        assertEquals(path.get("location.state"), state);
        assertEquals(path.get("location.country"), country);
        assertEquals(path.get("location.timezone"), timezone);

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_CREATED);
    }

    @Test
    public void createNewUserWithWrongTimezone() {

        UserLocation userLocation = new UserLocation();

        JSONObject request = createRequestObject();

        String timezone = randomAlphanumeric(4);

        userLocation.setTimezone(timezone);

        request.put("location", userLocation);


        Response response = RestAssured.given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .post("/user/create");
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
    public void createNewUserWithInvalidGender() {

        JSONObject request = createRequestObject();

        String gender = randomAlphanumeric(4);
        request.put("gender", gender);

        Response response = RestAssured.given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .body(request)
                .post("/user/create");
        logResponse(response);

        //Validate invalid gender format not allowed
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "BODY_NOT_VALID");
        assertEquals(path.get("data.gender"), "`" + gender + "`" + " is not a valid enum value for path `gender`.");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }
}
