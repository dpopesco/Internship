package org.example.tests.api.rest.wrapper.user;

import lombok.extern.slf4j.Slf4j;
import org.example.enums.Gender;
import org.example.enums.Title;
import org.example.models.error.ErrorModel;
import org.example.models.error.UserErrorModel;
import org.example.models.user.User;
import org.example.models.user.UserLocation;
import org.example.tests.api.rest.wrapper.ApiBaseClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.apache.commons.lang3.RandomStringUtils.*;
import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

@Slf4j
public class POSTUsersTest extends ApiBaseClass {

    @DataProvider(name = "userMandatoryFields")
    public Object[][] createData() {
        return new Object[][]{
                {User.generateRandomUser()},
                {User.generateRandomUser()},
                {User.generateRandomUser()}
        };
    }

    @Test(dataProvider = "userMandatoryFields")
    public void createUsers(User user) {

        User userResponse = restWrapper.usingUsers().createItem(user);

        log.info("Validate user is created successfully!");
        assertEquals(userResponse.getFirstName(), user.getFirstName());
        assertEquals(userResponse.getLastName(), user.getLastName());
        assertEquals(userResponse.getEmail(), user.getEmail().toLowerCase());

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_CREATED);
    }

    @Test
    public void createNewUser() {

        User user = User.generateRandomUser();
        User userResponse = restWrapper.usingUsers().createItem(user);

        log.info("Validate user is created successfully!");
        assertEquals(userResponse.getFirstName(), user.getFirstName());
        assertEquals(userResponse.getLastName(), user.getLastName());
        assertEquals(userResponse.getEmail(), user.getEmail().toLowerCase());


        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_CREATED);
    }

    @Test
    public void createNewUserUsingExistingEmail() {

        User user = User.generateAlreadyRegisteredUser();
        UserErrorModel userResponse = restWrapper.usingUsers().createItemWithFailure(user);

        log.info("Validate email already used error!");
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getEmail(), "Email already used");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }


    @Test
    public void createNewUserUsingSpacesForMandatoryFields() {

        User emptyUser = new User(" ", " ", " ");
        emptyUser.setGender(Gender.FEMALE.getUserGender());
        emptyUser.setTitle(Title.MISS.getUserTitle());
        UserErrorModel userResponse = restWrapper.usingUsers().createItemWithFailure(emptyUser);

        log.info("Validate spaces for mandatory fields not allowed!");
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getEmail(), "Path `email` is required.");
        assertEquals(userResponse.getData().getFirstName(), "Path `firstName` is required.");
        assertEquals(userResponse.getData().getLastName(), "Path `lastName` is required.");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithInvalidEmail() {

        User userInvalid = new User("Mariana", "Ricky", "mariana@mail");
        userInvalid.setGender(Gender.FEMALE.getUserGender());
        userInvalid.setTitle(Title.MISS.getUserTitle());
        UserErrorModel userResponse = restWrapper.usingUsers().createItemWithFailure(userInvalid);

        log.info("Validate invalid email structure not allowed!");
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getEmail(), "Path `email` is invalid (mariana@mail).");
        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithMissingMandatoryFields() {

        UserErrorModel userResponse = restWrapper.usingUsers().createItemWithoutBody();

        log.info("Validate user isn't created without mandatory fields entered!");
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getEmail(), "Path `email` is required.");
        assertEquals(userResponse.getData().getFirstName(), "Path `firstName` is required.");
        assertEquals(userResponse.getData().getLastName(), "Path `lastName` is required.");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test(description = "bug, user is created with xss injection")
    public void createNewUserUsingXSSForFirstName() {

        User user = User.generateRandomUser();
        user.setFirstName("<script>alert(\\'H\\')</script>");
        UserErrorModel userResponse = restWrapper.usingUsers().createItemWithFailure(user);

        log.info("Validate user is not created when entering xss script for firstName field!");
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getFirstName(), "Path `firstName` is invalid (<script>alert('H')</script>).");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithEmptyMandatoryFields() {

        User user = new User("", "", "");

        log.info("Added mandatory fields title and gender!");
        user.setTitle(Title.MISS.getUserTitle());
        user.setGender(Gender.FEMALE.getUserGender());

        UserErrorModel userResponse = restWrapper.usingUsers().createItemWithFailure(user);

        log.info("Validate empty mandatory fields not allowed!");
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getEmail(), "Path `email` is required.");
        assertEquals(userResponse.getData().getFirstName(), "Path `firstName` is required.");
        assertEquals(userResponse.getData().getLastName(), "Path `lastName` is required.");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithoutAuthorization() {

        User user = new User("Ella", "Miro", "ella@mail.com");

        ErrorModel userResponse = restWrapperWithoutAuth.usingUsers().createItemWithFailure(user);

        log.info("Validate user not created without app-id!");
        assertEquals(userResponse.getError(), "APP_ID_MISSING");

        log.info("Validate status code!");
        int statusCode = restWrapperWithoutAuth.getStatusCode();
        assertEquals(statusCode, SC_FORBIDDEN);
    }

    @Test
    public void createNewUserWithFirstNameLongerThan30Char() {
        User user = User.generateRandomUser();
        String firstName = randomAlphabetic(31);
        user.setFirstName(firstName);

        UserErrorModel userResponse = restWrapper.usingUsers().createItemWithFailure(user);

        log.info("Validate firstName longer than 30 not allowed!");
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getFirstName(), "Path `firstName` (`" + firstName + "`) is longer than the maximum allowed length (30).");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithLastNameLongerThan30Char() {
        User user = User.generateRandomUser();
        String lastName = randomAlphabetic(31);
        user.setLastName(lastName);

        UserErrorModel userResponse = restWrapper.usingUsers().createItemWithFailure(user);

        log.info("Validate lastName longer than 30 not allowed!");
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getLastName(), "Path `lastName` (`" + lastName + "`) is longer than the maximum allowed length (30).");


        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test(description = "server error 504")
    public void createNewUserWithEmailLongerThan50Char() {
        User user = User.generateRandomUser();
        String email = randomAlphabetic(51);
        user.setEmail(email);


        UserErrorModel userResponse = restWrapper.usingUsers().createItemWithFailure(user);

        log.info("Validate email longer than 50 not allowed!");
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getEmail(), "Path `email` (`" + email + "`) is longer than the maximum allowed length (50).");


        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithWrongTitleFormat() {

        User user = User.generateRandomUser();
        user.setTitle("unknown");

        UserErrorModel userResponse = restWrapper.usingUsers().createItemWithFailure(user);

        log.info("Validate wrong title format not allowed!");
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getTitle(), "`unknown` is not a valid enum value for path `title`.");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithId() {

        User user = User.generateRandomUser();
        user.setId(randomNumeric(6));

        User userResponse = restWrapper.usingUsers().createItem(user);

        log.info("Validate autogenerated id is created, instead of the one passed!");
        assertNotEquals(userResponse.getId(), user.getId());

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_CREATED);
    }

    @Test(description = "Bug, api accepts characters for phone")
    public void createNewUserWithCharactersForPhone() {

        User user = User.generateRandomUser();
        user.setPhone(randomAlphabetic(9));

        UserErrorModel userResponse = restWrapper.usingUsers().createItemWithFailure(user);

        log.info("Validate characters for phone not allowed!");
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getPhone(), "Path `phone` is invalid.");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @DataProvider(name = "invalidDateOfBirth")
    public Object[][] createInvalidData() {
        return new Object[][]{
                {"31/12/1899"},
                {"31/12/2050"},
                {"29/02/2022"},
                {"now"}
        };
    }

    @Test(dataProvider = "invalidDateOfBirth")
    public void createUserWithInvalidDateOfBirth(String invalidDate) {

        User user = User.generateRandomUser();
        user.setDateOfBirth(invalidDate);

        UserErrorModel userResponse = restWrapper.usingUsers().createItemWithFailure(user);

        log.info("Validate invalid date of birth not allowed!");
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getDateOfBirth(), "Cast to date failed for value \"" + invalidDate + "\" (type string) at path \"dateOfBirth\"");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithLocation() {

        User userWithLocation = User.generateUserWithLocation();

        User userResponse = restWrapper.usingUsers().createItem(userWithLocation);

        log.info("Validate location information created!");
        assertEquals(userResponse.getLocation().getStreet(), userWithLocation.getLocation().getStreet());
        assertEquals(userResponse.getLocation().getCity(), userWithLocation.getLocation().getCity());
        assertEquals(userResponse.getLocation().getState(), userWithLocation.getLocation().getState());
        assertEquals(userResponse.getLocation().getCountry(), userWithLocation.getLocation().getCountry());
        assertEquals(userResponse.getLocation().getTimezone(), userWithLocation.getLocation().getTimezone());

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_CREATED);
    }

    @Test(description = "Bug, api accepts characters for timezone")
    public void createNewUserWithWrongTimezone() {

        User userL = User.generateUserWithLocation();
        String timezone = randomAlphanumeric(4);
        UserLocation location = userL.getLocation();
        location.setTimezone(timezone);

        UserErrorModel userResponse = restWrapper.usingUsers().createItemWithFailure(userL);

        log.info("Validate invalid timezone format not allowed!");
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getLocation().getTimezone(), "Path `timezone` is invalid.");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithInvalidGender() {

        User user = User.generateRandomUser();
        String gender = randomAlphanumeric(4);
        user.setGender(gender);

        UserErrorModel userResponse = restWrapper.usingUsers().createItemWithFailure(user);

        log.info("Validate invalid gender format not allowed");
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getGender(), "`" + gender + "`" + " is not a valid enum value for path `gender`.");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }
}
