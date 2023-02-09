package org.example.tests.api.rest.wrapper.user.post;

import org.example.models.PostGET;
import org.example.models.PostsCollection;
import org.example.requests.PostsRequests;
import org.example.tests.api.rest.wrapper.user.ApiBaseClass;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;


public class GETPostsTest extends ApiBaseClass {
    @Test
    public void getPostList() {

        PostsRequests request = new PostsRequests(restWrapper);
        PostsCollection response = request.getPosts();

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void getUserPostsById() {

        String id = "60d21b4967d0d8992e610c90";

        PostsRequests request = new PostsRequests(restWrapper);
        PostGET response = request.getInfoByPostId(id);

        //Validate response post id as per request
        assertEquals(response.getId(), id);

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void getUserPostsByUserId() {

        String id = "60d0fe4f5311236168a109ed";

        PostsRequests request = new PostsRequests(restWrapper);
        PostsCollection response = request.getInfoByUserId(id);

        //Validate response owner id as per request
        assertEquals(response.getData().get(0).getUser().getId(), id);

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void getPostsByTag() {

        String tag = "ice";
        PostsRequests request = new PostsRequests(restWrapper);
        PostsCollection response = request.getInfoByTag(tag);

        //Validate response tag as per request
        assertTrue(response.getData().get(0).getTags().contains(tag));

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }
}
