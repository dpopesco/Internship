package api.tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

public class GETUsersTest extends ApiBaseClass {

    @Test
    public void checkPageNumberAndLimitParameters() {


        Response response = RestAssured.given()
                .header("app-id", ApiBaseClass.APP_ID)
                .get("/user?page=0&limit=10");
        response.getBody().prettyPrint();
        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Header: " + response.getHeader("content-type"));
        System.out.println("Response time: " + response.getTime());


        //Validate provided limit with response list limit
        JsonPath path = response.body().jsonPath();
        List<HashMap<String, Object>> jsonObjects = path.getList("data");
        assertEquals(jsonObjects.size(), 10);

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void checkItemsCreatedInCurrentEnvironment() {


        Response response = RestAssured.given()
                .header("app-id", ApiBaseClass.APP_ID)
                .get("/user?created=1");
        response.getBody().prettyPrint();
        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Header: " + response.getHeader("content-type"));
        System.out.println("Response time: " + response.getTime());

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void checkUsersLimit() {


        Response response = RestAssured.given()
                .header("app-id", ApiBaseClass.APP_ID)
                .get("/user");
        response.getBody().prettyPrint();
        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Header: " + response.getHeader("content-type"));
        System.out.println("Response time: " + response.getTime());

        //Validate provided limit with response list limit
        JsonPath path = response.body().jsonPath();
        List<HashMap<String, Object>> jsonObjects = path.getList("data");
        assertEquals(jsonObjects.size(), 20);

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
        response.getBody().prettyPrint();
        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Header: " + response.getHeader("content-type"));
        System.out.println("Response time: " + response.getTime());

        //Validate response id as per request
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("id"), id);

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void checkInvalidPageNumber() {


        Response response = RestAssured.given()
                .header("app-id", ApiBaseClass.APP_ID)
                .get("/user?page=1000");
        response.getBody().prettyPrint();
        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Header: " + response.getHeader("content-type"));
        System.out.println("Response time: " + response.getTime());

        //Validate response id as per request
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("total").toString(), "100");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }


}
