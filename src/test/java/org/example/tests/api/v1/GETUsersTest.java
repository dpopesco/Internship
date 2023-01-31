package org.example.tests.api.v1;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;


public class GETUsersTest extends ApiBaseClass {

    @Test
    public void checkItemsCreatedInCurrentEnvironment() {


        Response response = RestAssured.given()
                .header("app-id", ApiBaseClass.APP_ID)
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
                .header("app-id", ApiBaseClass.APP_ID)
                .get("/user/" + id);
        logResponse(response);

        //Validate response id as per request
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("id"), id);

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

}
