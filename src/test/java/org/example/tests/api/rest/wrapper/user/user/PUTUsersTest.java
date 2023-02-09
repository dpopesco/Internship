package org.example.tests.api.rest.wrapper.user.user;

import org.example.enums.Title;
import org.example.models.User;
import org.example.models.UserLocation;
import org.example.models.error.UserErrorModel;
import org.example.requests.UsersRequests;
import org.example.tests.api.rest.wrapper.user.ApiBaseClass;
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

        String userId = "60d0fe4f5311236168a109cb";
        UsersRequests request = new UsersRequests(restWrapper);
        User response = request.updateUser(user, userId);

        //Validate user is updated successfully
        assertEquals(response.getFirstName(), user.getFirstName());
        assertEquals(response.getLastName(), user.getLastName());

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void updateUserEmail() {

        User user = new User();
        user.setEmail(randomAlphanumeric(4) + "@mail.com");

        String userId = "60d0fe4f5311236168a109cb";
        UsersRequests request = new UsersRequests(restWrapper);
        User response = request.updateUser(user, userId);

        //Validate user's email is not updated
        assertNotEquals(response.getEmail(), user.getEmail());

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
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

        String userId = "60d0fe4f5311236168a109cb";
        UsersRequests request = new UsersRequests(restWrapper);
        User response = request.updateUser(user, userId);

        //Validate location information updated
        assertEquals(response.getLocation().getStreet(), street);
        assertEquals(response.getLocation().getCity(), city);
        assertEquals(response.getLocation().getState(), state);
        assertEquals(response.getLocation().getCountry(), country);
        assertEquals(response.getLocation().getTimezone(), timezone);

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void updateUserTitle() {

        User user = new User();
        user.setTitle(Title.DR.getUserTitle());

        String userId = "60d0fe4f5311236168a109cb";
        UsersRequests request = new UsersRequests(restWrapper);
        User response = request.updateUser(user, userId);

        //Validate user's email is not updated
        assertEquals(response.getTitle(), user.getTitle());

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test(description = "bug, api accepts to put `now` as value for dateOfBirth field")
    public void updateUserWithInvalidDateOfBirth() {

        User user = new User();
        user.setDateOfBirth("now");

        String userId = "60d0fe4f5311236168a109cb";
        UsersRequests request = new UsersRequests(restWrapper);
        UserErrorModel response = request.updateUserWithFailure(user, userId);

        //Validate user's date of birth not updated
        assertEquals(response.getError(), "BODY_NOT_VALID");
        assertEquals(response.getData().getDateOfBirth(), "Cast to date failed for value \"now\" (type string) at path \"dateOfBirth\"");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test(description = "bug, api accepts to put characters for phone field")
    public void updateNewUserWithCharactersForPhone() {

        User user = new User();
        user.setPhone(randomAlphabetic(9));

        String userId = "60d0fe4f5311236168a109cb";
        UsersRequests request = new UsersRequests(restWrapper);
        UserErrorModel response = request.updateUserWithFailure(user, userId);

        //Validate characters for phone not allowed
        assertEquals(response.getError(), "BODY_NOT_VALID");
        assertEquals(response.getData().getPhone(), "Path `phone` is invalid.");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test(description = "bug, api accepts to put characters for timezone field")
    public void updateUserWithWrongTimezone() {

        UserLocation userLocation = new UserLocation();
        User user = new User();

        String timezone = randomAlphanumeric(4);
        userLocation.setTimezone(timezone);
        user.setLocation(userLocation);

        String userId = "60d0fe4f5311236168a109cb";
        UsersRequests request = new UsersRequests(restWrapper);
        UserErrorModel response = request.updateUserWithFailure(user, userId);

        //Validate invalid timezone format not allowed
        assertEquals(response.getError(), "BODY_NOT_VALID");
        assertEquals(response.getData().getLocation().getTimezone(), "Path `timezone` is invalid.");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test(description = "bug, api accepts to put invalid gender value")
    public void updateNewUserWithInvalidGender() {

        User user = new User();
        String gender = randomAlphanumeric(4);
        user.setGender(gender);

        String userId = "60d0fe4f5311236168a109cb";
        UsersRequests request = new UsersRequests(restWrapper);
        UserErrorModel response = request.updateUserWithFailure(user, userId);

        //Validate invalid gender format not allowed
        assertEquals(response.getError(), "BODY_NOT_VALID");
        assertEquals(response.getData().getGender(), "`" + gender + "` is not a valid enum value for path `gender`.");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test(description = "bug, user can be updated with spaces for mandatory fields")
    public void updateUserUsingSpacesForMandatoryFields() {

        User user = new User();
        user.setFirstName(" ");
        user.setLastName(" ");

        String userId = "60d0fe4f5311236168a109cb";
        UsersRequests request = new UsersRequests(restWrapper);
        UserErrorModel response = request.updateUserWithFailure(user, userId);

        //Validate empty mandatory fields not allowed
        assertEquals(response.getError(), "BODY_NOT_VALID");
        assertEquals(response.getData().getFirstName(), "Path `firstName` is required.");
        assertEquals(response.getData().getLastName(), "Path `lastName` is required.");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void updateUserWithEmptyMandatoryFields() {

        User user = new User();
        user.setFirstName("");
        user.setLastName("");

        String userId = "60d0fe4f5311236168a109cb";
        UsersRequests request = new UsersRequests(restWrapper);
        User response = request.updateUser(user, userId);

        //Validate empty mandatory fields not allowed
        assertEquals(response.getFirstName(), "Edita");
        assertEquals(response.getLastName(), "Vestering");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void updateWithoutAuthorization() {

        User user = new User();
        user.setFirstName("D");

        String userId = "60d0fe4f5311236168a109cb";
        UsersRequests request = new UsersRequests(restWrapperWithoutAuth);
        UserErrorModel response = request.updateUserWithFailure(user, userId);

        //Validate user not updated without app-id
        assertEquals(response.getError(), "APP_ID_MISSING");

        // Validate status code
        int statusCode = restWrapperWithoutAuth.getStatusCode();
        assertEquals(statusCode, SC_FORBIDDEN);
    }

    @Test(description = "bug, api accepts to update firstName bigger than 30 characters")
    public void updateUserWithFirstNameLongerThan30Char() {

        User user = new User();
        String firstName = randomAlphabetic(31);
        user.setFirstName(firstName);

        String userId = "60d0fe4f5311236168a109cb";
        UsersRequests request = new UsersRequests(restWrapper);
        UserErrorModel response = request.updateUserWithFailure(user, userId);

        //Validate firstName longer than 30
        assertEquals(response.getError(), "BODY_NOT_VALID");
        assertEquals(response.getData().getFirstName(), "Path `firstName` (`" + firstName + "`) is longer than the maximum allowed length (30).");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test(description = "bug, api accepts to update lastName bigger than 30 characters")
    public void updateUserWithLastNameLongerThan30Char() {

        User user = new User();
        String lastName = randomAlphabetic(31);
        user.setLastName(lastName);

        String userId = "60d0fe4f5311236168a109cb";
        UsersRequests request = new UsersRequests(restWrapper);
        UserErrorModel response = request.updateUserWithFailure(user, userId);

        //Validate lastName longer than 30
        assertEquals(response.getError(), "BODY_NOT_VALID");
        assertEquals(response.getData().getLastName(), "Path `lastName` (`" + lastName + "`) is longer than the maximum allowed length (30).");


        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test(description = "bug, api accepts to update with invalid title ")
    public void updateUserWithWrongTitleFormat() {

        User user = new User();
        user.setTitle("unknown");

        String userId = "60d0fe4f5311236168a109cb";
        UsersRequests request = new UsersRequests(restWrapper);
        UserErrorModel response = request.updateUserWithFailure(user, userId);

        //Validate wrong title not allowed
        assertEquals(response.getError(), "BODY_NOT_VALID");
        assertEquals(response.getData().getTitle(), "`unknown` is not a valid enum value for path `title`.");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void updateUserId() {

        User user = new User();
        user.setId(randomNumeric(6));

        String userId = "60d0fe4f5311236168a109cb";
        UsersRequests request = new UsersRequests(restWrapper);
        User response = request.updateUser(user, userId);

        //Validate autogenerated id remained, instead of the one passed
        assertNotEquals(response.getId(), user.getId());

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }
}
