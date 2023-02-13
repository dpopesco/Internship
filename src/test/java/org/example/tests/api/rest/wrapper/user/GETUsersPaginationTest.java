package org.example.tests.api.rest.wrapper.user;

import lombok.extern.slf4j.Slf4j;
import org.example.models.error.ErrorModel;
import org.example.models.user.UsersCollection;
import org.example.tests.api.rest.wrapper.ApiBaseClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

@Slf4j
public class GETUsersPaginationTest extends ApiBaseClass {


    @Test
    public void checkPageNumberAndLimitParameters() {

        UsersCollection user = restWrapper.usingUsers().usingParams("page=0", "limit=10").getUsers();

        log.info("Validate provided limit with response list limit");
        assertEquals(user.getData().size(), 10);

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void checkUsersLimit() {

        UsersCollection user = restWrapper.usingUsers().getUsers();

        log.info("Validate provided limit with response list limit");
        assertEquals(user.getData().size(), 20);

        log.info("Validate status code!");
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

        ErrorModel user = restWrapper.usingUsers().usingParams("page=" + String.valueOf(pageNumber)).getUsersWithParamsAndExpectError();

        log.info("Validate params not valid");
        assertEquals(user.getError(), "PARAMS_NOT_VALID");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test(dataProvider = "invalidNumbers", description = "bug, api accepts invalid page parameter")
    public void checkInvalidLimitNumber(Object limitNumber) {

        ErrorModel user = restWrapper.usingUsers().usingParams("limit=" + String.valueOf(limitNumber)).getUsersWithParamsAndExpectError();

        log.info("Validate status code!");
        assertEquals(user.getError(), "PARAMS_NOT_VALID");

        log.error("BUG, api accepts invalid page parameter");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }
}
