package org.example.tests.api.rest.wrapper.user.user;

import org.example.models.UsersCollection;
import org.example.models.error.ErrorModel;
import org.example.requests.UsersRequests;
import org.example.tests.api.rest.wrapper.user.ApiBaseClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

public class GETUsersPaginationTest extends ApiBaseClass {


    @Test
    public void checkPageNumberAndLimitParameters() {


        UsersRequests request = new UsersRequests(restWrapper);
        UsersCollection user = request.usingParams("page=0", "limit=10").getUsersWithParams();

        //Validate provided limit with response list limit
        assertEquals(user.getData().size(), 10);

        // Validate status code and response time
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void checkUsersLimit() {

        UsersRequests request = new UsersRequests(restWrapper);
        UsersCollection user = request.getUsers();

        //Validate provided limit with response list limit
        assertEquals(user.getData().size(), 20);

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @DataProvider(name = "invalidNumbers")
    public Object[][] createInvalidData() {
        return new Object[][]{
                {1000},
                {-1},
                {"Word"},
                {"%%%%%"},
                {1.2}
        };
    }

    @Test(dataProvider = "invalidNumbers", description = "bug, api accepts invalid page parameter")
    public void checkInvalidPageNumber(Object pageNumber) {
        UsersRequests request = new UsersRequests(restWrapper);
        ErrorModel user = request.usingParams("page=" + String.valueOf(pageNumber)).getUsersWithParamsAndExpectError();

        //Validate params not valid
        assertEquals(user.getError(), "PARAMS_NOT_VALID");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test(dataProvider = "invalidNumbers", description = "bug, api accepts invalid page parameter")
    public void checkInvalidLimitNumber(Object limitNumber) {
        UsersRequests request = new UsersRequests(restWrapper);
        ErrorModel user = request.usingParams("limit=" + String.valueOf(limitNumber)).getUsersWithParamsAndExpectError();

        //Validate params not valid
        assertEquals(user.getError(), "PARAMS_NOT_VALID");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }
}
