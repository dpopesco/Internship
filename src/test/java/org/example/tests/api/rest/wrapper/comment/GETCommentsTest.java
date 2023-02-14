package org.example.tests.api.rest.wrapper.comment;

import lombok.extern.slf4j.Slf4j;
import org.example.models.comment.CommentsCollection;
import org.example.models.error.ErrorModel;
import org.example.tests.api.rest.wrapper.ApiBaseClass;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Slf4j
public class GETCommentsTest extends ApiBaseClass {
    @Test
    public void getCommentsList() {

        CommentsCollection response = restWrapper.usingComments().getItems();

        log.info("Validate default response limit!");
        assertEquals(response.getLimit(), 20);

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void getCommentsByPostId() {

        String postId = "60d21bb667d0d8992e610dcc";
        CommentsCollection response = restWrapper.usingComments().getCommentsByPostId(postId);

        log.info("Validate response post id as per request!");
        assertTrue(response.getData().stream().allMatch(x -> x.getPostId().equals(postId)));

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void getCommentsWithInvalidPostId() {

        String postId = "60d21bb667d0d8";
        ErrorModel response = restWrapper.usingComments().getCommentsByPostIdWithFailure(postId);

        log.info("Validate comments aren't shown for invalid post Id!");
        assertEquals(response.getError(), "PARAMS_NOT_VALID");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void getCommentsByUserId() {

        String userId = "60d0fe4f5311236168a10a1e";
        CommentsCollection response = restWrapper.usingComments().getCommentsByUserId(userId);

        log.info("Validate response user id as per request!");
        response.getData().stream().allMatch(x -> x.getOwner().getId().equals(userId));

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void getCommentsByInvalidUserId() {

        String userId = "60d0fe4f53";
        ErrorModel response = restWrapper.usingComments().getCommentsByUserIdWithFailure(userId);

        log.info("Validate comments aren't shown for invalid user Id!");
        assertEquals(response.getError(), "PARAMS_NOT_VALID");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }
}
