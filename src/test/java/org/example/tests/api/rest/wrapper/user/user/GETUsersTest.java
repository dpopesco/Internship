package org.example.tests.api.rest.wrapper.user.user;

import io.restassured.response.Response;
import org.example.models.User;
import org.example.models.error.ErrorResponseModel;
import org.example.tests.api.rest.wrapper.user.ApiBaseClass;
import org.springframework.http.HttpMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

public class GETUsersTest extends ApiBaseClass {

    @Test
    public void checkItemsCreatedInCurrentEnvironment() {

        Response response = restWrapper.sendRequest(HttpMethod.GET, "/user?{parameters}", "", "created=1");

        logResponse(response);

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void checkUserInfoById() {

        String id = "60d0fe4f5311236168a109dd";

        Response response = restWrapper.sendRequest(HttpMethod.GET, "/user/{id}", "", id);


        logResponse(response);

        //Validate response id as per request
        User userM = restWrapper.convertResponseToModel(response, User.class);
        assertEquals(userM.getId(), id);

        // Validate status code
        int statusCode = response.getStatusCode();
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

        Response response = restWrapper.sendRequest(HttpMethod.GET, "/user/{id}", "", id);


        logResponse(response);

        //Validate params not valid
        ErrorResponseModel errorResponseModel = restWrapper.convertResponseToModel(response, ErrorResponseModel.class);
        assertEquals(errorResponseModel.getError(), "PARAMS_NOT_VALID");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }
}
