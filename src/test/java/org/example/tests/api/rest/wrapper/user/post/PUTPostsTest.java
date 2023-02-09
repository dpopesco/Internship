package org.example.tests.api.rest.wrapper.user.post;

import org.example.models.PostGET;
import org.example.models.PostPOST;
import org.example.models.error.ErrorModel;
import org.example.models.error.UserErrorModel;
import org.example.requests.PostsRequests;
import org.example.tests.api.rest.wrapper.user.ApiBaseClass;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class PUTPostsTest extends ApiBaseClass {
    @Test
    public void updatePost() {

        PostPOST post = PostPOST.generateRandomPost();
        String postId = "63e4ae16d970e64e3fc5bb5a";

        PostsRequests request = new PostsRequests(restWrapper);
        PostGET response = request.updatePost(post, postId);

        //Validate post is updated successfully
        assertEquals(response.getText(), post.getText());
        assertEquals(response.getImage(), post.getImage());
        assertEquals(response.getLikes(), post.getLikes());
        assertEquals(response.getTags(), post.getTags());

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void updateAnInvalidPostId() {

        PostPOST post = PostPOST.generateRandomPost();
        String postId = "63e4ae16d97";

        PostsRequests request = new PostsRequests(restWrapper);
        ErrorModel response = request.updatePostWithFailure(post, postId);

        //Validate postId is not valid
        assertEquals(response.getError(), "PARAMS_NOT_VALID");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void updateUserFromPost() {

        PostPOST post = PostPOST.generateRandomPost();
        String postId = "63e4ae16d970e64e3fc5bb5a";
        String ownerId = "63e4ae16d97";
        post.setOwnerId(ownerId);

        PostsRequests request = new PostsRequests(restWrapper);
        PostGET response = request.updatePost(post, postId);

        //Validate post is not updated with owner id
        assertNotEquals(response.getUser().getId(), "63e4ae16d97");
        assertEquals(response.getUser().getId(), "63e0d8d3c2fbb95b9f900a95");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test(description = "bug, status code is 0, instead of 403")
    public void updatePostWithoutAuthorization() {

        PostPOST post = PostPOST.generateRandomPost();
        String postId = "63e4ae16d970e64e3fc5bb5a";

        PostsRequests request = new PostsRequests(restWrapperWithoutAuth);
        ErrorModel response = request.updatePostWithFailure(post, postId);

        //Validate post is updated successfully
        assertEquals(response.getError(), "APP_ID_MISSING");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_FORBIDDEN);
    }

    @Test
    public void updatePostWithCharactersForLikes() {
        PostPOST post = new PostPOST();
        JSONObject postS = new JSONObject(post);
        String postId = "63e4ae16d970e64e3fc5bb5a";

        postS.put("likes", "rfr");

        PostsRequests request = new PostsRequests(restWrapper);
        UserErrorModel response = request.updatePostWithJson(postS.toString(), postId);
        //Validate post is created successfully
        assertEquals(response.getError(), "BODY_NOT_VALID");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }
}
