package org.example.tests.api.rest.wrapper.user.post;

import io.restassured.response.Response;
import org.example.models.PostGET;
import org.example.models.PostsCollection;
import org.example.tests.api.rest.wrapper.user.ApiBaseClass;
import org.springframework.http.HttpMethod;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;


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

        String id = "60d21b4967d0d8992e610c90";

        Response response = restWrapper.sendRequest(HttpMethod.GET, "/post/{id}", "", id);

        logResponse(response);

        //Validate response post id as per request
        PostGET postModel = restWrapper.convertResponseToModel(response, PostGET.class);
        assertEquals(postModel.getId(), id);

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void getUserPostsByUserId() {

        String id = "60d0fe4f5311236168a109ed";

        Response response = restWrapper.sendRequest(HttpMethod.GET, "/user/" + id + "/post{}", "", "");

        logResponse(response);

        //Validate response owner id as per request
        PostsCollection postsCollectionModel = restWrapper.convertResponseToModel(response, PostsCollection.class);
        assertEquals(postsCollectionModel.getData().get(0).getUser().getId(), id);

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void getPostsByTag() {

        String tag = "ice";

        Response response = restWrapper.sendRequest(HttpMethod.GET, "/tag/" + tag + "/post{}", "", "");

        logResponse(response);

        //Validate response tag as per request

        PostsCollection postsCollectionModel = restWrapper.convertResponseToModel(response, PostsCollection.class);
        assertTrue(postsCollectionModel.getData().get(0).getTags().contains(tag));

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }
}
