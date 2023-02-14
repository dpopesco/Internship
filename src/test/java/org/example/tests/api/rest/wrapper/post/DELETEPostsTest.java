package org.example.tests.api.rest.wrapper.post;

import lombok.extern.slf4j.Slf4j;
import org.example.models.error.ErrorModel;
import org.example.models.post.PostGET;
import org.example.models.post.PostPOST;
import org.example.tests.api.rest.wrapper.ApiBaseClass;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

@Slf4j
public class DELETEPostsTest extends ApiBaseClass {
    private String createdId;

    @Test
    public void deletePost() {

        PostPOST post = PostPOST.generateRandomPost();
        post.setOwnerId("63e0d8d3c2fbb95b9f900a95");

        PostGET response = restWrapper.usingPosts().createItem(post);

        String createdId = response.getId();

        PostPOST responseDelete = restWrapper.usingPosts().deleteItem(createdId);

        log.info("Validate post is deleted successfully!");
        assertNull(responseDelete.getLink());

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
        this.createdId = createdId;
    }

    @Test
    public void deleteAlreadyDeletedPost() {

        deletePost();

        ErrorModel responseDelete = restWrapper.usingPosts().deleteItemWithFailure(createdId);

        log.info("Validate already deleted post is not found!");
        assertEquals(responseDelete.getError(), "RESOURCE_NOT_FOUND");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_NOT_FOUND);
    }

    @Test
    public void deleteAnInvalidPostId() {

        String postId = "63e1f94a";

        ErrorModel responseDelete = restWrapper.usingPosts().deleteItemWithFailure(postId);

        log.info("Validate post is not deleted because of invalid postId!");
        assertEquals(responseDelete.getError(), "PARAMS_NOT_VALID");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void deletePostWithoutAuthorization() {

        String postId = "63e1f94ab6c1a123d203959a";

        ErrorModel responseDelete = restWrapperWithoutAuth.usingPosts().deleteItemWithFailure(postId);

        log.info("Validate post is not deleted as app id is missing!");
        assertEquals(responseDelete.getError(), "APP_ID_MISSING");

        log.info("Validate status code!");
        int statusCode = restWrapperWithoutAuth.getStatusCode();
        assertEquals(statusCode, SC_FORBIDDEN);
    }
}
