package org.example.tests.api.rest.wrapper.user;

import lombok.extern.slf4j.Slf4j;
import org.example.models.error.ErrorModel;
import org.example.models.user.User;
import org.example.models.user.UsersCollection;
import org.example.tests.api.rest.wrapper.ApiBaseClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

@Slf4j
public class GETUsersTest extends ApiBaseClass {


    @Test
    public void checkItemsCreatedInCurrentEnvironment() {
        UsersCollection user = restWrapper.usingUsers().usingParams("created=1").getItems();

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void checkUserInfoById() {

        String id = "60d0fe4f5311236168a109dd";

        User user = restWrapper.usingUsers().getItem(id);

        log.info("Validate response id as per request");
        assertEquals(user.getId(), id);

        log.info("Validate status code!");
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

        ErrorModel user = restWrapper.usingUsers().getItemWithFailure(String.valueOf(id));

        log.info("Validate params not valid");
        assertEquals(user.getError(), "PARAMS_NOT_VALID");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }
}
