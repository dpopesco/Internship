package org.example.tests.api.spring.user;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.tests.api.spring.ApiBaseClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class GETUsersPaginationTest extends ApiBaseClass {


    @Test
    public void checkPageNumberAndLimitParameters() {


        Response response = RestAssured.given()
                .header("app-id", properties.getAppId())
                .get("/user?page=0&limit=10");

        logResponse(response);

        //Validate provided limit with response list limit
        JsonPath path = response.body().jsonPath();
        List<HashMap<String, Object>> jsonObjects = path.getList("data");
        assertEquals(jsonObjects.size(), 10);

        // Validate status code and response time
        int statusCode = response.getStatusCode();
        long responseTime = response.time();
        assertEquals(statusCode, SC_OK);
        assertTrue(responseTime < 1500);
    }

    @Test
    public void checkUsersLimit() {


        Response response = RestAssured.given()
                .header("app-id", properties.getAppId())
                .get("/user");

        logResponse(response);

        //Validate provided limit with response list limit
        JsonPath path = response.body().jsonPath();
        List<HashMap<String, Object>> jsonObjects = path.getList("data");
        assertEquals(jsonObjects.size(), 20);

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @DataProvider(name = "invalidPageNumber")
    public Object[][] createInvalidData() {
        return new Object[][]{
                {1000},
                {-1},
                {"Word"},
                {"%%%%%"},
                {1.2}
        };
    }
    @Test(dataProvider = "invalidPageNumber")
    public void checkInvalidPageNumber( Object pageNumber) {


        Response response = RestAssured.given()
                .header("app-id", properties.getAppId())
                .get("/user?page="+ pageNumber);

        logResponse(response);

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }
}
