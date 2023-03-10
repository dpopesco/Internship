package org.example.tests.api.spring.user;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.tests.api.spring.ApiBaseClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

public class GETUsersTest extends ApiBaseClass {

    @Test
    public void checkItemsCreatedInCurrentEnvironment() {


        Response response = RestAssured.given()
                .header("app-id", properties.getAppId())
                .get("/user?created=1");

        logResponse(response);

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void checkUserInfoById() {

        String id = "60d0fe4f5311236168a109dd";

        Response response = RestAssured.given()
                .header("app-id", properties.getAppId())
                .get("/user/" + id);


        logResponse(response);

        //Validate response id as per request
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("id"), id);

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

        Response response = RestAssured.given()
                .header("app-id", properties.getAppId())
                .get("/user/" + id);


        logResponse(response);

        //Validate params not valid
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "PARAMS_NOT_VALID");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }
}
