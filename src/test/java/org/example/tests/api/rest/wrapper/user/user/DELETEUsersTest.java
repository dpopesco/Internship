package org.example.tests.api.rest.wrapper.user.user;

import org.example.models.User;
import org.example.models.error.ErrorModel;
import org.example.requests.UsersRequests;
import org.example.tests.api.rest.wrapper.user.ApiBaseClass;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class DELETEUsersTest extends ApiBaseClass {

    @Test
    public void deleteUser() {

        //create new user
        User user = User.generateRandomUser();
        UsersRequests request = new UsersRequests(restWrapper);
        User response = request.createUser(user);

        //save created id from response
        String createdId = response.getId();

        UsersRequests requestDelete = new UsersRequests(restWrapper);
        User responseDelete = requestDelete.deleteUser(createdId);

        //Validate user is deleted successfully
        assertNull(responseDelete.getFirstName());

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void deleteAlreadyDeletedUser() {

        UsersRequests request = new UsersRequests(restWrapper);
        ErrorModel response = request.deleteUserWithFailure("60d0fe4f5311236168a109ca");

        //Validate already deleted user is not found
        assertEquals(response.getError(), "RESOURCE_NOT_FOUND");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_NOT_FOUND);
    }

    @Test
    public void deleteUserWithInvalidId() {

        UsersRequests request = new UsersRequests(restWrapper);
        ErrorModel response = request.deleteUserWithFailure("9576445");

        //Validate user with invalid id shows error params not valid
        assertEquals(response.getError(), "PARAMS_NOT_VALID");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void deleteWithoutAuthorization() {

        UsersRequests request = new UsersRequests(restWrapperWithoutAuth);
        ErrorModel response = request.deleteUserWithFailure("60d0fe4f5311236168a109ca");

        //Validate user not deleted without app-id
        assertEquals(response.getError(), "APP_ID_MISSING");

        // Validate status code
        int statusCode = restWrapperWithoutAuth.getStatusCode();
        assertEquals(statusCode, SC_FORBIDDEN);
    }

}
