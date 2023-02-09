package org.example.tests.api.rest.wrapper.user.user;

import org.example.enums.Gender;
import org.example.enums.Title;
import org.example.models.User;
import org.example.models.UserLocation;
import org.example.models.error.ErrorModel;
import org.example.models.error.UserErrorModel;
import org.example.requests.UsersRequests;
import org.example.tests.api.rest.wrapper.user.ApiBaseClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.apache.commons.lang3.RandomStringUtils.*;
import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

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
        UsersRequests request = new UsersRequests(restWrapper);
        User userResponse = request.createUser(user);

        //Validate user is created successfully

        assertEquals(userResponse.getFirstName(), user.getFirstName());
        assertEquals(userResponse.getLastName(), user.getLastName());
        assertEquals(userResponse.getEmail(), user.getEmail().toLowerCase());

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_CREATED);
    }

    @Test
    public void createNewUser() {

        UsersRequests request = new UsersRequests(restWrapper);
        User user = User.generateRandomUser();
        User userResponse = request.createUser(user);

        //Validate user is created successfully
        assertEquals(userResponse.getFirstName(), user.getFirstName());
        assertEquals(userResponse.getLastName(), user.getLastName());
        assertEquals(userResponse.getEmail(), user.getEmail().toLowerCase());


        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_CREATED);
    }

    @Test
    public void createNewUserUsingExistingEmail() {

        UsersRequests request = new UsersRequests(restWrapper);
        User user = User.generateAlreadyRegisteredUser();
        UserErrorModel userResponse = request.createUserWithFailure(user);

        //Validate email already used error
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getEmail(), "Email already used");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }


    @Test
    public void createNewUserUsingSpacesForMandatoryFields() {

        UsersRequests request = new UsersRequests(restWrapper);
        User emptyUser = new User(" ", " ", " ");
        emptyUser.setGender(Gender.FEMALE.getUserGender());
        emptyUser.setTitle(Title.MISS.getUserTitle());
        UserErrorModel userResponse = request.createUserWithFailure(emptyUser);

        //Validate spaces for mandatory fields not allowed
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getEmail(), "Path `email` is required.");
        assertEquals(userResponse.getData().getFirstName(), "Path `firstName` is required.");
        assertEquals(userResponse.getData().getLastName(), "Path `lastName` is required.");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithInvalidEmail() {

        UsersRequests request = new UsersRequests(restWrapper);
        User userInvalid = new User("Mariana", "Ricky", "mariana@mail");
        userInvalid.setGender(Gender.FEMALE.getUserGender());
        userInvalid.setTitle(Title.MISS.getUserTitle());
        UserErrorModel userResponse = request.createUserWithFailure(userInvalid);

        //Validate invalid email structure not allowed
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getEmail(), "Path `email` is invalid (mariana@mail).");
        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithMissingMandatoryFields() {
        UsersRequests request = new UsersRequests(restWrapper);
        UserErrorModel userResponse = request.createUserWithoutBody();

        //Validate user isn't created without mandatory fields entered
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getEmail(), "Path `email` is required.");
        assertEquals(userResponse.getData().getFirstName(), "Path `firstName` is required.");
        assertEquals(userResponse.getData().getLastName(), "Path `lastName` is required.");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test(description = "bug, user is created with xss injection")
    public void createNewUserUsingXSSForFirstName() {

        UsersRequests request = new UsersRequests(restWrapper);
        User user = User.generateRandomUser();
        user.setFirstName("<script>alert(\\'H\\')</script>");
        UserErrorModel userResponse = request.createUserWithFailure(user);

        //Validate user is not created when entering xss script for firstName field
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getFirstName(), "Path `firstName` is invalid (<script>alert('H')</script>).");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithEmptyMandatoryFields() {

        User user = new User("", "", "");

        //added fields because title and gender are mandatory
        user.setTitle(Title.MISS.getUserTitle());
        user.setGender(Gender.FEMALE.getUserGender());

        UsersRequests request = new UsersRequests(restWrapper);
        UserErrorModel userResponse = request.createUserWithFailure(user);

        //Validate empty mandatory fields not allowed
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getEmail(), "Path `email` is required.");
        assertEquals(userResponse.getData().getFirstName(), "Path `firstName` is required.");
        assertEquals(userResponse.getData().getLastName(), "Path `lastName` is required.");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithoutAuthorization() {

        User user = new User("Ella", "Miro", "ella@mail.com");
        UsersRequests request = new UsersRequests(restWrapperWithoutAuth);
        ErrorModel userResponse = request.createUserWithFailure(user);
        //Validate user not created without app-id
        assertEquals(userResponse.getError(), "APP_ID_MISSING");

        // Validate status code
        int statusCode = restWrapperWithoutAuth.getStatusCode();
        assertEquals(statusCode, SC_FORBIDDEN);
    }

    @Test
    public void createNewUserWithFirstNameLongerThan30Char() {
        User user = User.generateRandomUser();
        String firstName = randomAlphabetic(31);
        user.setFirstName(firstName);

        UsersRequests request = new UsersRequests(restWrapper);
        UserErrorModel userResponse = request.createUserWithFailure(user);

        //Validate firstName longer than 30
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getFirstName(), "Path `firstName` (`" + firstName + "`) is longer than the maximum allowed length (30).");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithLastNameLongerThan30Char() {
        User user = User.generateRandomUser();
        String lastName = randomAlphabetic(31);
        user.setLastName(lastName);

        UsersRequests request = new UsersRequests(restWrapper);
        UserErrorModel userResponse = request.createUserWithFailure(user);

        //Validate lastName longer than 30
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getLastName(), "Path `lastName` (`" + lastName + "`) is longer than the maximum allowed length (30).");


        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test(description = "server error 504")
    public void createNewUserWithEmailLongerThan50Char() {
        User user = User.generateRandomUser();
        String email = randomAlphabetic(51);
        user.setEmail(email);

        UsersRequests request = new UsersRequests(restWrapper);
        UserErrorModel userResponse = request.createUserWithFailure(user);

        //Validate email longer than 50
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getEmail(), "Path `email` (`" + email + "`) is longer than the maximum allowed length (50).");


        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithWrongTitleFormat() {

        User user = User.generateRandomUser();
        user.setTitle("unknown");

        UsersRequests request = new UsersRequests(restWrapper);
        UserErrorModel userResponse = request.createUserWithFailure(user);

        //Validate wrong title not allowed
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getTitle(), "`unknown` is not a valid enum value for path `title`.");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithId() {

        User user = User.generateRandomUser();
        user.setId(randomNumeric(6));

        UsersRequests request = new UsersRequests(restWrapper);
        User userResponse = request.createUser(user);

        //Validate autogenerated id is created, instead of the one passed
        assertNotEquals(userResponse.getId(), user.getId());

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_CREATED);
    }

    @Test(description = "Bug, api accepts characters for phone")
    public void createNewUserWithCharactersForPhone() {

        User user = User.generateRandomUser();
        user.setPhone(randomAlphabetic(9));

        UsersRequests request = new UsersRequests(restWrapper);
        UserErrorModel userResponse = request.createUserWithFailure(user);

        //Validate characters for phone not allowed
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getPhone(), "Path `phone` is invalid.");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    //to revisit
    @Test
    public void createUserWithInvalidDateOfBirth() {

        User user = User.generateRandomUser();
        user.setDateOfBirth("now");

        UsersRequests request = new UsersRequests(restWrapper);
        UserErrorModel userResponse = request.createUserWithFailure(user);

        //Validate date of birth now not allowed
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getDateOfBirth(), "Cast to date failed for value \"now\" (type string) at path \"dateOfBirth\"");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithLocation() {

        User userWithLocation = User.generateUserWithLocation();

        UsersRequests request = new UsersRequests(restWrapper);
        User userResponse = request.createUser(userWithLocation);

        //Validate location information created
        assertEquals(userResponse.getLocation().getStreet(), userWithLocation.getLocation().getStreet());
        assertEquals(userResponse.getLocation().getCity(), userWithLocation.getLocation().getCity());
        assertEquals(userResponse.getLocation().getState(), userWithLocation.getLocation().getState());
        assertEquals(userResponse.getLocation().getCountry(), userWithLocation.getLocation().getCountry());
        assertEquals(userResponse.getLocation().getTimezone(), userWithLocation.getLocation().getTimezone());

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_CREATED);
    }

    @Test(description = "Bug, api accepts characters for timezone")
    public void createNewUserWithWrongTimezone() {

        User userL = User.generateUserWithLocation();
        String timezone = randomAlphanumeric(4);
        UserLocation location = userL.getLocation();
        location.setTimezone(timezone);

        UsersRequests request = new UsersRequests(restWrapper);
        UserErrorModel userResponse = request.createUserWithFailure(userL);

        //Validate invalid timezone format not allowed
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getLocation().getTimezone(), "Path `timezone` is invalid.");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithInvalidGender() {

        User user = User.generateRandomUser();
        String gender = randomAlphanumeric(4);
        user.setGender(gender);

        UsersRequests request = new UsersRequests(restWrapper);
        UserErrorModel userResponse = request.createUserWithFailure(user);

        //Validate invalid gender format not allowed
        assertEquals(userResponse.getError(), "BODY_NOT_VALID");
        assertEquals(userResponse.getData().getGender(), "`" + gender + "`" + " is not a valid enum value for path `gender`.");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }
}
