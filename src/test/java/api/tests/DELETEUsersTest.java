package api.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class DELETEUsersTest extends ApiBaseClass {
    @Test
    public void deleteUser() {

        Response response = RestAssured.given()
                .header("app-id", ApiBaseClass.APP_ID)
                .contentType(ContentType.JSON)
                .delete("/user/63d7914f10fee17a516c598d");
        response.getBody().prettyPrint();
        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Header: " + response.getHeader("content-type"));
        System.out.println("Response time: " + response.getTime());

        //Validate user is deleted successfully
        JsonPath path = response.body().jsonPath();
        assertNull(path.get("firstName"));

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void deleteAlreadyDeletedUser() {

        Response response = RestAssured.given()
                .header("app-id", ApiBaseClass.APP_ID)
                .contentType(ContentType.JSON)
                .delete("/user/60d0fe4f5311236168a109d3");
        response.getBody().prettyPrint();
        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Header: " + response.getHeader("content-type"));
        System.out.println("Response time: " + response.getTime());

        //Validate already deleted user is not found
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "RESOURCE_NOT_FOUND");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_NOT_FOUND);
    }

    @Test
    public void deleteUserWithInvalidId() {

        Response response = RestAssured.given()
                .header("app-id", ApiBaseClass.APP_ID)
                .contentType(ContentType.JSON)
                .delete("/user/9576445");
        response.getBody().prettyPrint();
        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Header: " + response.getHeader("content-type"));
        System.out.println("Response time: " + response.getTime());

        //Validate user with invalid id shows error params not valid
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "PARAMS_NOT_VALID");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void deleteWithoutAuthorization() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .delete("/user/60d0fe4f5311236168a109ca");
        response.getBody().prettyPrint();
        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Header: " + response.getHeader("content-type"));
        System.out.println("Response time: " + response.getTime());

        //Validate user not deleted without app-id
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("error"), "APP_ID_MISSING");


        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_FORBIDDEN);
    }

}
