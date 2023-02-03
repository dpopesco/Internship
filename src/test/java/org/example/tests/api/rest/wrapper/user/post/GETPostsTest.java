package org.example.tests.api.rest.wrapper.user.post;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.tests.api.rest.wrapper.user.ApiBaseClass;
import org.springframework.http.HttpMethod;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;


public class GETPostsTest extends ApiBaseClass {
    @Test
    public void getPostList() {
        Response response = restWrapper.sendRequest(HttpMethod.GET, "/post{}", "", "");

        logResponse(response);

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void getUserPostsById() {

        String id = "60d0fe4f5311236168a109ca";

        Response response = restWrapper.sendRequest(HttpMethod.GET, "/user/" + id + "/post{}", "", "");

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

        Response response = restWrapper.sendRequest(HttpMethod.GET, "/tag/" + tag + "/post{}", "", "");

        logResponse(response);

        //Validate response tag as per request

        JsonPath path = response.body().jsonPath();
        assertEquals(path.get("data[0].tags[1]"), tag);

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

}
