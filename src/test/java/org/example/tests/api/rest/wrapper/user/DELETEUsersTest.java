package org.example.tests.api.rest.wrapper.user;

import lombok.extern.slf4j.Slf4j;
import org.example.models.error.ErrorModel;
import org.example.models.user.User;
import org.example.tests.api.rest.wrapper.ApiBaseClass;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

@Slf4j
public class DELETEUsersTest extends ApiBaseClass {

    @Test
    public String deleteUser() {

        User user = User.generateRandomUser();
        User response = restWrapper.usingUsers().createUser(user);

        String createdId = response.getId();

        User responseDelete = restWrapper.usingUsers().deleteUser(createdId);

        log.info("Validate user is deleted successfully!");
        assertNull(responseDelete.getFirstName());

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
        return createdId;
    }

    @Test
    public void deleteAlreadyDeletedUser() {

        String deletedUser = deleteUser();

        ErrorModel response = restWrapper.usingUsers().deleteUserWithFailure(deletedUser);

        log.info("Validate already deleted user is not found!");
        assertEquals(response.getError(), "RESOURCE_NOT_FOUND");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_NOT_FOUND);
    }

    @Test
    public void deleteUserWithInvalidId() {

        ErrorModel response = restWrapper.usingUsers().deleteUserWithFailure("9576445");

        log.info("Validate user with invalid id shows error params not valid!");
        assertEquals(response.getError(), "PARAMS_NOT_VALID");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void deleteWithoutAuthorization() {

        ErrorModel response = restWrapperWithoutAuth.usingUsers().deleteUserWithFailure("60d0fe4f5311236168a109ca");

        log.info("Validate user not deleted without app-id!");
        assertEquals(response.getError(), "APP_ID_MISSING");

        log.info("Validate status code!");
        int statusCode = restWrapperWithoutAuth.getStatusCode();
        assertEquals(statusCode, SC_FORBIDDEN);
    }

}
