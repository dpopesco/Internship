package org.example.tests.api.spring.post;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.tests.api.spring.ApiBaseClass;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;


public class GETPostsTest extends ApiBaseClass {
    @Test
    public void getPostList() {
        Response response = RestAssured.given()
                .header("app-id", properties.getAppId())
                .get("/post");

        logResponse(response);

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void getUserPostsById() {

        String id = "60d0fe4f5311236168a109ca";

        Response response = RestAssured.given()
                .header("app-id", properties.getAppId())
                .get("/user/" + id + "/post");


        logResponse(response);

        //Validate response owner id as per request
        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("data[0].owner.id"), id);

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void getPostsByTag() {

        String tag = "dog";

        Response response = RestAssured.given()
                .header("app-id", properties.getAppId())
                .get("/tag/" + tag + "/post");


        logResponse(response);

        //Validate response tag as per request

        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("data[0].tags[1]"), tag);

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

}
