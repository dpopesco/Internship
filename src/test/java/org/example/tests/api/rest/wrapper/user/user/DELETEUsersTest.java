package org.example.tests.api.rest.wrapper.user.user;

import io.restassured.response.Response;
import org.example.models.User;
import org.example.models.error.ErrorModel;
import org.example.tests.api.rest.wrapper.user.ApiBaseClass;
import org.springframework.http.HttpMethod;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class DELETEUsersTest extends ApiBaseClass {

    @Test
    public void deleteUser() {

        //create new user
        User user = User.generateRandomUser();

        Response responseCreate = restWrapper.sendRequest(HttpMethod.POST, "/user/create", user, "");

        //save created id from response
        User userMC = restWrapper.convertResponseToModel(responseCreate, User.class);
        String createdId = userMC.getId();
        Response response = restWrapper.sendRequest(HttpMethod.DELETE, "/user/{id}", "", createdId);

        logResponse(response);

        //Validate user is deleted successfully
        User userM = restWrapper.convertResponseToModel(response, User.class);
        assertNull(userM.getFirstName());

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void deleteAlreadyDeletedUser() {

        Response response = restWrapper.sendRequest(HttpMethod.DELETE, "/user/{id}", "", "60d0fe4f5311236168a109ca");

        logResponse(response);

        //Validate already deleted user is not found
        ErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, ErrorModel.class);
        assertEquals(errorResponseModel.getError(), "RESOURCE_NOT_FOUND");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_NOT_FOUND);
    }

    @Test
    public void deleteUserWithInvalidId() {

        Response response = restWrapper.sendRequest(HttpMethod.DELETE, "/user/{id}", "", "9576445");

        logResponse(response);

        //Validate user with invalid id shows error params not valid
        ErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, ErrorModel.class);
        assertEquals(errorResponseModel.getError(), "PARAMS_NOT_VALID");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void deleteWithoutAuthorization() {

        Response response = restWrapperWithoutAuth.sendRequest(HttpMethod.DELETE, "/user/{id}", "", "60d0fe4f5311236168a109ca");

        logResponse(response);

        //Validate user not deleted without app-id
        ErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, ErrorModel.class);
        assertEquals(errorResponseModel.getError(), "APP_ID_MISSING");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_FORBIDDEN);
    }

}
