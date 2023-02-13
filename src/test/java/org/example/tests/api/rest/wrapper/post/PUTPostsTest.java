package org.example.tests.api.rest.wrapper.post;

import lombok.extern.slf4j.Slf4j;
import org.example.models.error.ErrorModel;
import org.example.models.error.UserErrorModel;
import org.example.models.post.PostGET;
import org.example.models.post.PostPOST;
import org.example.tests.api.rest.wrapper.ApiBaseClass;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

@Slf4j
public class PUTPostsTest extends ApiBaseClass {
    private final String postId = "63e4ae16d970e64e3fc5bb5a";

    @Test
    public void updatePost() {

        PostPOST post = PostPOST.generateRandomPost();

        PostGET response = restWrapper.usingPosts().updatePost(post, postId);

        log.info("Validate post is updated successfully!");
        assertEquals(response.getText(), post.getText());
        assertEquals(response.getImage(), post.getImage());
        assertEquals(response.getLikes(), post.getLikes());
        assertEquals(response.getTags(), post.getTags());

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void updateAnInvalidPostId() {

        PostPOST post = PostPOST.generateRandomPost();
        String postId = "63e4ae16d97";

        ErrorModel response = restWrapper.usingPosts().updatePostWithFailure(post, postId);

        log.info("Validate postId is not valid!");
        assertEquals(response.getError(), "PARAMS_NOT_VALID");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void updateUserFromPost() {

        PostPOST post = PostPOST.generateRandomPost();

        String ownerId = "63e4ae16d97";
        post.setOwnerId(ownerId);

        PostGET response = restWrapper.usingPosts().updatePost(post, postId);

        log.info("Validate post is not updated with owner id!");
        assertNotEquals(response.getUser().getId(), ownerId);
        assertEquals(response.getUser().getId(), "63e0d8d3c2fbb95b9f900a95");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test(description = "bug, status code is 0, instead of 403")
    public void updatePostWithoutAuthorization() {

        PostPOST post = PostPOST.generateRandomPost();

        ErrorModel response = restWrapperWithoutAuth.usingPosts().updatePostWithFailure(post, postId);

        log.info("Validate post cannot be updated without app id!");
        assertEquals(response.getError(), "APP_ID_MISSING");

        log.info("Validate status code!");
        log.error("Bug, status code is 0, instead of 403!");

        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_FORBIDDEN);
    }

    @Test
    public void updatePostWithCharactersForLikes() {
        PostPOST post = new PostPOST();
        JSONObject postS = new JSONObject(post);
        postS.put("likes", "rfr");

        UserErrorModel response = restWrapper.usingPosts().updatePostWithFailure(postS.toString(), postId);

        log.info("Validate post cannot be updated when sent characters for number of likes!");
        assertEquals(response.getError(), "BODY_NOT_VALID");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }
}
