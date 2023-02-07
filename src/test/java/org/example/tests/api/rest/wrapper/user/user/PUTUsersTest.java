package org.example.tests.api.rest.wrapper.user.user;

import io.restassured.response.Response;
import org.example.models.User;
import org.example.models.UserLocation;
import org.example.models.error.UserErrorModel;
import org.example.tests.api.rest.wrapper.user.ApiBaseClass;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.testng.annotations.Test;

import static org.apache.commons.lang3.RandomStringUtils.*;
import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class PUTUsersTest extends ApiBaseClass {
    @Test
    public void updateUser() {

        User user = new User();

        user.setFirstName(randomAlphabetic(5));
        user.setLastName(randomAlphabetic(5));

        Response response = restWrapper.sendRequest(HttpMethod.PUT, "/user/{id}", user, "60d0fe4f5311236168a109cb");

        logResponse(response);

        //Validate user is updated successfully
        User userM = restWrapper.convertResponseToModel(response, User.class);
        assertEquals(userM.getFirstName(), user.getFirstName());
        assertEquals(userM.getLastName(), user.getLastName());

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void updateUserEmail() {

        User user = new User();

        user.setEmail(randomAlphanumeric(4) + "@mail.com");

        Response response = restWrapper.sendRequest(HttpMethod.PUT, "/user/{id}", user, "60d0fe4f5311236168a109cb");

        logResponse(response);

        //Validate user's email is not updated
        User userM = restWrapper.convertResponseToModel(response, User.class);
        assertNotEquals(userM.getEmail(), user.getEmail());

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void updateUserLocation() {

        User user = new User();
        UserLocation userLocation = new UserLocation();

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

        user.setLocation(userLocation);

        Response response = restWrapper.sendRequest(HttpMethod.PUT, "/user/{id}", user, "60d0fe4f5311236168a109cb");

        logResponse(response);

        //Validate location information updated
        User userM = restWrapper.convertResponseToModel(response, User.class);
        assertEquals(userM.getLocation().getStreet(), street);
        assertEquals(userM.getLocation().getCity(), city);
        assertEquals(userM.getLocation().getState(), state);
        assertEquals(userM.getLocation().getCountry(), country);
        assertEquals(userM.getLocation().getTimezone(), timezone);

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void updateUserTitle() {

        User user = new User();

        user.setTitle("dr");

        Response response = restWrapper.sendRequest(HttpMethod.PUT, "/user/{id}", user, "60d0fe4f5311236168a109cb");

        logResponse(response);

        //Validate user's email is not updated
        User userM = restWrapper.convertResponseToModel(response, User.class);
        assertEquals(userM.getTitle(), user.getTitle());

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test(description = "bug, api accepts to put `now` as value for dateOfBirth field")
    public void updateUserWithInvalidDateOfBirth() {

        User user = new User();

        //serialize the object in Json
        JSONObject userS = new JSONObject(user);

        userS.put("dateOfBirth", "now");

        Response response = restWrapper.sendRequest(HttpMethod.PUT, "/user/{id}", userS.toString(), "60d0fe4f5311236168a109cb");

        logResponse(response);

        //Validate user's date of birth not updated
        UserErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, UserErrorModel.class);
        assertEquals(errorResponseModel.getError(), "BODY_NOT_VALID");
        assertEquals(errorResponseModel.getData().getDateOfBirth(), "Cast to date failed for value \"now\" (type string) at path \"dateOfBirth\"");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test(description = "bug, api accepts to put characters for phone field")
    public void updateNewUserWithCharactersForPhone() {

        User user = new User();

        user.setPhone(randomAlphabetic(9));

        Response response = restWrapper.sendRequest(HttpMethod.PUT, "/user/{id}", user, "60d0fe4f5311236168a109cb");

        logResponse(response);

        //Validate characters for phone not allowed
        UserErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, UserErrorModel.class);
        assertEquals(errorResponseModel.getError(), "BODY_NOT_VALID");
        assertEquals(errorResponseModel.getData().getPhone(), "Path `phone` is invalid.");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test(description = "bug, api accepts to put characters for timezone field")
    public void updateUserWithWrongTimezone() {

        UserLocation userLocation = new UserLocation();
        User user = new User();

        String timezone = randomAlphanumeric(4);

        userLocation.setTimezone(timezone);
        user.setLocation(userLocation);

        Response response = restWrapper.sendRequest(HttpMethod.PUT, "/user/{id}", user, "60d0fe4f5311236168a109cb");

        logResponse(response);

        //Validate invalid timezone format not allowed
        UserErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, UserErrorModel.class);
        assertEquals(errorResponseModel.getError(), "BODY_NOT_VALID");
        assertEquals(errorResponseModel.getData().getLocation().getTimezone(), "Path `timezone` is invalid.");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test(description = "bug, api accepts to put invalid gender value")
    public void updateNewUserWithInvalidGender() {

        User user = new User();

        String gender = randomAlphanumeric(4);
        user.setGender(gender);

        Response response = restWrapper.sendRequest(HttpMethod.PUT, "/user/{id}", user, "60d0fe4f5311236168a109cb");

        logResponse(response);

        //Validate invalid gender format not allowed
        UserErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, UserErrorModel.class);
        assertEquals(errorResponseModel.getError(), "BODY_NOT_VALID");
        assertEquals(errorResponseModel.getData().getGender(), "`" + gender + "` is not a valid enum value for path `gender`.");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test(description = "bug, user can be updated with spaces for mandatory fields")
    public void updateUserUsingSpacesForMandatoryFields() {

        User user = new User();

        user.setFirstName(" ");
        user.setLastName(" ");

        Response response = restWrapper.sendRequest(HttpMethod.PUT, "/user/{id}", user, "60d0fe4f5311236168a109cb");

        logResponse(response);

        //Validate empty mandatory fields not allowed
        UserErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, UserErrorModel.class);
        assertEquals(errorResponseModel.getError(), "BODY_NOT_VALID");
        assertEquals(errorResponseModel.getData().getFirstName(), "Path `firstName` is required.");
        assertEquals(errorResponseModel.getData().getLastName(), "Path `lastName` is required.");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void updateUserWithEmptyMandatoryFields() {

        User user = new User();

        user.setFirstName("");
        user.setLastName("");

        Response response = restWrapper.sendRequest(HttpMethod.PUT, "/user/{id}", user, "60d0fe4f5311236168a109cb");

        logResponse(response);

        //Validate empty mandatory fields not allowed
        User userM = restWrapper.convertResponseToModel(response, User.class);
        assertEquals(userM.getFirstName(), "Edita");
        assertEquals(userM.getLastName(), "Vestering");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void updateWithoutAuthorization() {

        User user = new User();

        user.setFirstName("D");

        Response response = restWrapperWithoutAuth.sendRequest(HttpMethod.PUT, "/user/{id}", user, "60d0fe4f5311236168a109cb");

        logResponse(response);

        //Validate user not updated without app-id
        UserErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, UserErrorModel.class);
        assertEquals(errorResponseModel.getError(), "APP_ID_MISSING");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_FORBIDDEN);
    }

    @Test(description = "bug, api accepts to update firstName bigger than 30 characters")
    public void updateUserWithFirstNameLongerThan30Char() {
        User user = new User();

        String firstName = randomAlphabetic(31);

        user.setFirstName(firstName);

        Response response = restWrapper.sendRequest(HttpMethod.PUT, "/user/{id}", user, "60d0fe4f5311236168a109cb");

        logResponse(response);

        //Validate firstName longer than 30
        UserErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, UserErrorModel.class);
        assertEquals(errorResponseModel.getError(), "BODY_NOT_VALID");
        assertEquals(errorResponseModel.getData().getFirstName(), "Path `firstName` (`" + firstName + "`) is longer than the maximum allowed length (30).");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test(description = "bug, api accepts to update lastName bigger than 30 characters")
    public void updateUserWithLastNameLongerThan30Char() {
        User user = new User();

        String lastName = randomAlphabetic(31);

        user.setLastName(lastName);

        Response response = restWrapper.sendRequest(HttpMethod.PUT, "/user/{id}", user, "60d0fe4f5311236168a109cb");

        logResponse(response);

        //Validate lastName longer than 30
        UserErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, UserErrorModel.class);
        assertEquals(errorResponseModel.getError(), "BODY_NOT_VALID");
        assertEquals(errorResponseModel.getData().getLastName(), "Path `lastName` (`" + lastName + "`) is longer than the maximum allowed length (30).");


        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test(description = "bug, api accepts to update with invalid title ")
    public void updateUserWithWrongTitleFormat() {

        User user = new User();

        user.setTitle("unknown");

        Response response = restWrapper.sendRequest(HttpMethod.PUT, "/user/{id}", user, "60d0fe4f5311236168a109cb");

        logResponse(response);

        //Validate wrong title not allowed
        UserErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, UserErrorModel.class);
        assertEquals(errorResponseModel.getError(), "BODY_NOT_VALID");
        assertEquals(errorResponseModel.getData().getTitle(), "`unknown` is not a valid enum value for path `title`.");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void updateUserId() {

        User user = new User();

        user.setId(randomNumeric(6));

        Response response = restWrapper.sendRequest(HttpMethod.PUT, "/user/{id}", user, "60d0fe4f5311236168a109cb");

        logResponse(response);

        //Validate autogenerated id remained, instead of the one passed
        User userM = restWrapper.convertResponseToModel(response, User.class);
        assertNotEquals(userM.getId(), user.getId());

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }
}
