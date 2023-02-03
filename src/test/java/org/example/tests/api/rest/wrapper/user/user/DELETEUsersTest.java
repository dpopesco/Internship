package org.example.tests.api.rest.wrapper.user.user;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.tests.api.rest.wrapper.user.ApiBaseClass;
import org.json.simple.JSONObject;
import org.springframework.http.HttpMethod;
import org.testng.annotations.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class DELETEUsersTest extends ApiBaseClass {

    @Test
    public void deleteUser() {

        //create new user
        String email = randomAlphanumeric(6) + "@mail.com";
        String firstName = randomAlphabetic(5);
        String lastName = randomAlphabetic(6);

        JSONObject request = new JSONObject();
        request.put("firstName", firstName);
        request.put("lastName", lastName);
        request.put("email", email);

        Response responseCreate = restWrapper.sendRequest(HttpMethod.POST, "/user/create{}", request, "");

        //save created user's id
        JsonPath pathCreate = responseCreate.body().jsonPath();
        String createdId = pathCreate.get("id");

        Response response = restWrapper.sendRequest(HttpMethod.DELETE, "/user/{id}", "", createdId);

        logResponse(response);

        //Validate user is deleted successfully
        JsonPath path = response.body().jsonPath();
        assertNull(path.get("firstName"));

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void deleteAlreadyDeletedUser() {

        Response response = restWrapper.sendRequest(HttpMethod.DELETE, "/user/{id}", "", "60d0fe4f5311236168a109ca");

        logResponse(response);

        //Validate already deleted user is not found
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "RESOURCE_NOT_FOUND");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_NOT_FOUND);
    }

    @Test
    public void deleteUserWithInvalidId() {

        Response response = restWrapper.sendRequest(HttpMethod.DELETE, "/user/{id}", "", "9576445");

        logResponse(response);

        //Validate user with invalid id shows error params not valid
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "PARAMS_NOT_VALID");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void deleteWithoutAuthorization() {

        Response response = restWrapperWithoutAuth.sendRequest(HttpMethod.DELETE, "/user/{id}", "", "60d0fe4f5311236168a109ca");

        logResponse(response);

        //Validate user not deleted without app-id
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "APP_ID_MISSING");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_FORBIDDEN);
    }

}
