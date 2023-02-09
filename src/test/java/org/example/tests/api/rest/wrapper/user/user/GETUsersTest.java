package org.example.tests.api.rest.wrapper.user.user;

import org.example.models.User;
import org.example.models.UsersCollection;
import org.example.models.error.ErrorModel;
import org.example.requests.UsersRequests;
import org.example.tests.api.rest.wrapper.user.ApiBaseClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

public class GETUsersTest extends ApiBaseClass {

    @Test
    public void checkItemsCreatedInCurrentEnvironment() {
        UsersRequests usersRequest = new UsersRequests(restWrapper);
        UsersCollection user = usersRequest.usingParams("created=1").getUsersWithParams();

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void checkUserInfoById() {
        UsersRequests usersRequest = new UsersRequests(restWrapper);

        String id = "60d0fe4f5311236168a109dd";

        User user = usersRequest.getUser(id);

        //Validate response id as per request
        assertEquals(user.getId(), id);

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @DataProvider(name = "invalidId")
    public Object[][] createInvalidData() {
        return new Object[][]{
                {1000},
                {-1},
                {"Word"},
                {"%%%%%"},
                {1.2},
                {randomAlphanumeric(13)}
        };
    }

    @Test(dataProvider = "invalidId")
    public void checkUserInfoProvidingInvalidId(Object id) {

        UsersRequests usersRequest = new UsersRequests(restWrapper);
        ErrorModel user = usersRequest.getUserAndExpectError(String.valueOf(id));

        //Validate params not valid
        assertEquals(user.getError(), "PARAMS_NOT_VALID");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }
}
