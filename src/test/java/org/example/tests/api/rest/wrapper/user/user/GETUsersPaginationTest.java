package org.example.tests.api.rest.wrapper.user.user;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.models.UsersCollection;
import org.example.tests.api.rest.wrapper.user.ApiBaseClass;
import org.springframework.http.HttpMethod;
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


        Response response = restWrapper.sendRequest(HttpMethod.GET, "/user?{parameters}", "", "page=0&limit=10");

        logResponse(response);

        //Validate provided limit with response list limit
        UsersCollection users = restWrapper.convertResponseToModel(response,UsersCollection.class);
        assertEquals(users.getData().size(),10);

        // Validate status code and response time
        int statusCode = response.getStatusCode();
        long responseTime = response.time();
        assertEquals(statusCode, SC_OK);
        assertTrue(responseTime < 1500);
    }

    @Test
    public void checkUsersLimit() {


        Response response = restWrapper.sendRequest(HttpMethod.GET, "/user{}", "", "");

        logResponse(response);

        //Validate provided limit with response list limit
        JsonPath path = response.body().jsonPath();
        List<HashMap<String, Object>> jsonObjects = path.getList("data");
        assertEquals(jsonObjects.size(), 20);

        // Validate status code
        int statusCode = response.getStatusCode();
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

    @Test(dataProvider = "invalidNumbers")
    public void checkInvalidPageNumber(Object pageNumber) {


        Response response = restWrapper.sendRequest(HttpMethod.GET, "/user?{page}", "", "page=" + pageNumber);

        logResponse(response);

        //Validate params not valid
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "PARAMS_NOT_VALID");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test(dataProvider = "invalidNumbers")
    public void checkInvalidLimitNumber(Object limitNumber) {


        Response response = restWrapper.sendRequest(HttpMethod.GET, "/user?{limit}", "", "limit=" + limitNumber);

        logResponse(response);

        //Validate params not valid
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "PARAMS_NOT_VALID");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }
}
