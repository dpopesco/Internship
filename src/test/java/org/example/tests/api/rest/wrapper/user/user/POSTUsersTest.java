package org.example.tests.api.rest.wrapper.user.user;

import io.restassured.response.Response;
import org.example.enums.Gender;
import org.example.enums.Title;
import org.example.models.User;
import org.example.models.UserLocation;
import org.example.models.error.ErrorModel;
import org.example.models.error.UserErrorModel;
import org.example.tests.api.rest.wrapper.user.ApiBaseClass;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
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

        Response response = restWrapper.sendRequest(HttpMethod.POST, "/user/create", user, "");

        logResponse(response);

        //Validate user is created successfully
        User userM = restWrapper.convertResponseToModel(response, User.class);

        assertEquals(userM.getFirstName(), user.getFirstName());
        assertEquals(userM.getLastName(), user.getLastName());
        assertEquals(userM.getEmail(), user.getEmail().toLowerCase());

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_CREATED);
    }

    @Test
    public void createNewUser() {

        User user = User.generateRandomUser();
        Response response = restWrapper.sendRequest(HttpMethod.POST, "/user/create", user, "");

        logResponse(response);

        //Validate user is created successfully
        User userM = restWrapper.convertResponseToModel(response, User.class);
        assertEquals(userM.getFirstName(), user.getFirstName());
        assertEquals(userM.getLastName(), user.getLastName());
        assertEquals(userM.getEmail(), user.getEmail().toLowerCase());


        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_CREATED);
    }

    @Test
    public void createNewUserUsingExistingEmail() {

        User user = User.generateAlreadyRegisteredUser();
        Response response = restWrapper.sendRequest(HttpMethod.POST, "/user/create", user, "");

        logResponse(response);

        //Validate email already used error
        UserErrorModel responseE = restWrapper.convertResponseToModel(response, UserErrorModel.class);
        assertEquals(responseE.getError(), "BODY_NOT_VALID");
        assertEquals(responseE.getData().getEmail(), "Email already used");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }


    @Test
    public void createNewUserUsingSpacesForMandatoryFields() {

        User emptyUser = new User(" ", " ", " ");
        emptyUser.setGender(Gender.FEMALE.getUserGender());
        emptyUser.setTitle(Title.MISS.getUserTitle());

        Response response = restWrapper.sendRequest(HttpMethod.POST, "/user/create", emptyUser, "");

        logResponse(response);

        //Validate spaces for mandatory fields not allowed
        UserErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, UserErrorModel.class);
        assertEquals(errorResponseModel.getError(), "BODY_NOT_VALID");
        assertEquals(errorResponseModel.getData().getEmail(), "Path `email` is required.");
        assertEquals(errorResponseModel.getData().getFirstName(), "Path `firstName` is required.");
        assertEquals(errorResponseModel.getData().getLastName(), "Path `lastName` is required.");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithInvalidEmail() {

        User userInvalid = new User("Mariana", "Ricky", "mariana@mail");
        userInvalid.setGender(Gender.FEMALE.getUserGender());
        userInvalid.setTitle(Title.MISS.getUserTitle());

        Response response = restWrapper.sendRequest(HttpMethod.POST, "/user/create", userInvalid, "");

        logResponse(response);

        //Validate invalid email structure not allowed
        UserErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, UserErrorModel.class);
        assertEquals(errorResponseModel.getError(), "BODY_NOT_VALID");
        assertEquals(errorResponseModel.getData().getEmail(), "Path `email` is invalid (mariana@mail).");
        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithMissingMandatoryFields() {

        Response response = restWrapper.sendRequest(HttpMethod.POST, "/user/create", "", "");

        logResponse(response);

        //Validate user isn't created without mandatory fields entered
        UserErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, UserErrorModel.class);
        assertEquals(errorResponseModel.getError(), "BODY_NOT_VALID");
        assertEquals(errorResponseModel.getData().getEmail(), "Path `email` is required.");
        assertEquals(errorResponseModel.getData().getFirstName(), "Path `firstName` is required.");
        assertEquals(errorResponseModel.getData().getLastName(), "Path `lastName` is required.");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test(description = "bug, user is created with xss injection")
    public void createNewUserUsingXSSForFirstName() {

        User user = User.generateRandomUser();
        user.setFirstName("<script>alert(\\'H\\')</script>");

        Response response = restWrapper.sendRequest(HttpMethod.POST, "/user/create", user, "");

        logResponse(response);

        //Validate user is not created when entering xss script for firstName field
        UserErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, UserErrorModel.class);
        assertEquals(errorResponseModel.getError(), "BODY_NOT_VALID");
        assertEquals(errorResponseModel.getData().getFirstName(), "Path `firstName` is invalid (<script>alert('H')</script>).");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithEmptyMandatoryFields() {

        User user = new User("", "", "");

        //added fields because title and gender are mandatory
        user.setTitle(Title.MISS.getUserTitle());
        user.setGender(Gender.FEMALE.getUserGender());

        Response response = restWrapper.sendRequest(HttpMethod.POST, "/user/create", user, "");

        logResponse(response);

        //Validate empty mandatory fields not allowed
        UserErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, UserErrorModel.class);
        assertEquals(errorResponseModel.getError(), "BODY_NOT_VALID");
        assertEquals(errorResponseModel.getData().getEmail(), "Path `email` is required.");
        assertEquals(errorResponseModel.getData().getFirstName(), "Path `firstName` is required.");
        assertEquals(errorResponseModel.getData().getLastName(), "Path `lastName` is required.");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithoutAuthorization() {

        User user = new User("Ella", "Miro", "ella@mail.com");

        Response response = restWrapperWithoutAuth.sendRequest(HttpMethod.POST, "/user/create", user, "");

        logResponse(response);

        //Validate user not created without app-id
        ErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, ErrorModel.class);
        assertEquals(errorResponseModel.getError(), "APP_ID_MISSING");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_FORBIDDEN);
    }

    @Test
    public void createNewUserWithFirstNameLongerThan30Char() {
        User user = User.generateRandomUser();

        String firstName = randomAlphabetic(31);
        user.setFirstName(firstName);

        Response response = restWrapper.sendRequest(HttpMethod.POST, "/user/create", user, "");

        logResponse(response);

        //Validate firstName longer than 30
        UserErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, UserErrorModel.class);
        assertEquals(errorResponseModel.getError(), "BODY_NOT_VALID");
        assertEquals(errorResponseModel.getData().getFirstName(), "Path `firstName` (`" + firstName + "`) is longer than the maximum allowed length (30).");


        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithLastNameLongerThan30Char() {
        User user = User.generateRandomUser();

        String lastName = randomAlphabetic(31);
        user.setLastName(lastName);

        Response response = restWrapper.sendRequest(HttpMethod.POST, "/user/create", user, "");

        logResponse(response);

        //Validate lastName longer than 30
        UserErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, UserErrorModel.class);
        assertEquals(errorResponseModel.getError(), "BODY_NOT_VALID");
        assertEquals(errorResponseModel.getData().getLastName(), "Path `lastName` (`" + lastName + "`) is longer than the maximum allowed length (30).");


        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test(description = "server error 502")
    public void createNewUserWithEmailLongerThan50Char() {
        User user = User.generateRandomUser();

        String email = randomAlphabetic(51);
        user.setEmail(email);

        Response response = restWrapper.sendRequest(HttpMethod.POST, "/user/create", user, "");

        logResponse(response);

        //Validate email longer than 50
        UserErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, UserErrorModel.class);
        assertEquals(errorResponseModel.getError(), "BODY_NOT_VALID");
        assertEquals(errorResponseModel.getData().getEmail(), "Path `email` (`" + email + "`) is longer than the maximum allowed length (50).");


        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    //to revisit
    @Test
    public void createNewUserWithWrongTitleFormat() {

        User user = User.generateRandomUser();

        user.setTitle("unknown");

        Response response = restWrapper.sendRequest(HttpMethod.POST, "/user/create", user, "");

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
    public void createNewUserWithId() {

        User user = User.generateRandomUser();

        user.setEmail(randomNumeric(6));

        Response response = restWrapper.sendRequest(HttpMethod.POST, "/user/create", user, "");

        logResponse(response);

        //Validate autogenerated id is created, instead of the one passed
        User userM = restWrapper.convertResponseToModel(response, User.class);
        assertNotEquals(userM.getId(), user.getId());


        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_CREATED);
    }

    @Test(description = "Bug, api accepts characters for phone")
    public void createNewUserWithCharactersForPhone() {

        User user = User.generateRandomUser();

        user.setPhone(randomAlphabetic(9));

        Response response = restWrapper.sendRequest(HttpMethod.POST, "/user/create", user, "");

        logResponse(response);

        //Validate characters for phone not allowed
        UserErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, UserErrorModel.class);
        assertEquals(errorResponseModel.getError(), "BODY_NOT_VALID");
        assertEquals(errorResponseModel.getData().getPhone(), "Path `phone` is invalid.");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createUserWithInvalidDateOfBirth() {

        User user = User.generateRandomUser();
        JSONObject userS = new JSONObject(user);

        userS.put("dateOfBirth", "now");

        Response response = restWrapper.sendRequest(HttpMethod.POST, "/user/create", userS.toString(), "");

        logResponse(response);

        //Validate user's email is not updated
        UserErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, UserErrorModel.class);
        assertEquals(errorResponseModel.getError(), "BODY_NOT_VALID");
        assertEquals(errorResponseModel.getData().getDateOfBirth(), "Cast to date failed for value \"now\" (type string) at path \"dateOfBirth\"");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithLocation() {

        User userWithLocation = User.generateUserWithLocation();

        Response response = restWrapper.sendRequest(HttpMethod.POST, "/user/create", userWithLocation, "");

        logResponse(response);

        //Validate location information created
        User userM = restWrapper.convertResponseToModel(response, User.class);
        assertEquals(userM.getLocation().getStreet(), userWithLocation.getLocation().getStreet());
        assertEquals(userM.getLocation().getCity(), userWithLocation.getLocation().getCity());
        assertEquals(userM.getLocation().getState(), userWithLocation.getLocation().getState());
        assertEquals(userM.getLocation().getCountry(), userWithLocation.getLocation().getCountry());
        assertEquals(userM.getLocation().getTimezone(), userWithLocation.getLocation().getTimezone());

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_CREATED);
    }

    @Test
    public void createNewUserWithWrongTimezone() {

        User userL = User.generateUserWithLocation();

        String timezone = randomAlphanumeric(4);

        UserLocation location = userL.getLocation();
        location.setTimezone(timezone);

        Response response = restWrapper.sendRequest(HttpMethod.POST, "/user/create", userL, "");

        logResponse(response);

        //Validate invalid timezone format not allowed
        UserErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, UserErrorModel.class);
        assertEquals(errorResponseModel.getError(), "BODY_NOT_VALID");
        assertEquals(errorResponseModel.getData().getLocation().getTimezone(), "Path `timezone` is invalid.");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createNewUserWithInvalidGender() {

        User user = User.generateRandomUser();

        String gender = randomAlphanumeric(4);
        user.setGender(gender);

        Response response = restWrapper.sendRequest(HttpMethod.POST, "/user/create", user, "");

        logResponse(response);

        //Validate invalid gender format not allowed
        UserErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, UserErrorModel.class);
        assertEquals(errorResponseModel.getError(), "BODY_NOT_VALID");
        assertEquals(errorResponseModel.getData().getGender(), "`" + gender + "`" + " is not a valid enum value for path `gender`.");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }
}
