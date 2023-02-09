package org.example.tests.api.rest.wrapper.user.post;

import org.example.models.PostGET;
import org.example.models.PostPOST;
import org.example.models.error.ErrorModel;
import org.example.requests.PostsRequests;
import org.example.tests.api.rest.wrapper.user.ApiBaseClass;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class DELETEPostsTest extends ApiBaseClass {
    @Test
    public void deletePost() {

        //create new user
        PostPOST post = PostPOST.generateRandomPost();
        post.setOwnerId("63e0d8d3c2fbb95b9f900a95");
        PostsRequests request = new PostsRequests(restWrapper);
        PostGET response = request.createPost(post);

        //save created id from response
        String createdId = response.getId();

        PostsRequests requestDelete = new PostsRequests(restWrapper);
        PostPOST responseDelete = requestDelete.deletePost(createdId);

        //Validate user is deleted successfully
        assertNull(responseDelete.getLink());

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void deleteAlreadyDeletedPost() {

        String postId = "63e1f94ab6c1a123d203959a";

        PostsRequests requestDelete = new PostsRequests(restWrapper);
        ErrorModel responseDelete = requestDelete.deletePostWithFailure(postId);

        //Validate user is deleted successfully
        assertEquals(responseDelete.getError(), "RESOURCE_NOT_FOUND");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_NOT_FOUND);
    }

    @Test
    public void deleteAnInvalidPostId() {

        String postId = "63e1f94a";

        PostsRequests requestDelete = new PostsRequests(restWrapper);
        ErrorModel responseDelete = requestDelete.deletePostWithFailure(postId);

        //Validate user is deleted successfully
        assertEquals(responseDelete.getError(), "PARAMS_NOT_VALID");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void deletePostWithoutAuthorization() {

        String postId = "63e1f94ab6c1a123d203959a";

        PostsRequests requestDelete = new PostsRequests(restWrapperWithoutAuth);
        ErrorModel responseDelete = requestDelete.deletePostWithFailure(postId);

        //Validate user is deleted successfully
        assertEquals(responseDelete.getError(), "APP_ID_MISSING");

        // Validate status code
        int statusCode = restWrapperWithoutAuth.getStatusCode();
        assertEquals(statusCode, SC_FORBIDDEN);
    }
}
