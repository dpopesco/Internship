package org.example.tests.api.rest.wrapper.comment;

import lombok.extern.slf4j.Slf4j;
import org.example.models.comment.CommentGET;
import org.example.models.comment.CommentPOST;
import org.example.models.error.ErrorModel;
import org.example.tests.api.rest.wrapper.ApiBaseClass;
import org.testng.annotations.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.assertEquals;

@Slf4j
public class POSTCommentsTest extends ApiBaseClass {
    @Test
    public void createComment() {

        CommentPOST body = CommentPOST.generateComment();
        CommentGET response = restWrapper.usingComments().createItem(body);

        log.info("Validate comment is created!");
        assertEquals(response.getMessage(), body.getMessage());
        assertEquals(response.getOwner().getId(), body.getOwnerId());
        assertEquals(response.getPostId(), body.getPostId());

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_CREATED);
    }

    @Test
    public void createCommentWithInvalidOwnerId() {

        CommentPOST body = new CommentPOST();
        body.setMessage(randomAlphanumeric(9));
        body.setOwnerId("jdjdjd");
        body.setPostId("60d21b7967d0d8992e610d1b");

        ErrorModel response = restWrapper.usingComments().createItemWithFailure(body);

        log.info("Validate comment isn't created!");
        assertEquals(response.getError(), "BODY_NOT_VALID");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createCommentWithInvalidPostId() {

        CommentPOST body = new CommentPOST();
        body.setMessage(randomAlphanumeric(10));
        body.setOwnerId("60d0fe4f5311236168a109d0");
        body.setPostId("dhhdhd");

        ErrorModel response = restWrapper.usingComments().createItemWithFailure(body);

        log.info("Validate comment isn't created!");
        assertEquals(response.getError(), "BODY_NOT_VALID");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createCommentWithEmptyMessage() {

        CommentPOST body = new CommentPOST();
        body.setOwnerId("60d0fe4f5311236168a109d0");
        body.setPostId("60d21b7967d0d8992e610d1b");

        CommentGET response = restWrapper.usingComments().createItem(body);

        log.info("Validate comment is created!");
        assertEquals(response.getMessage(), "");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_CREATED);
    }

    @Test
    public void createCommentWithInvalidOwnerIdAndPostId() {

        CommentPOST body = new CommentPOST();
        body.setMessage(randomAlphanumeric(10));
        body.setOwnerId("hhhh");
        body.setPostId("dhhdhd");

        ErrorModel response = restWrapper.usingComments().createItemWithFailure(body);

        log.info("Validate comment isn't created!");
        assertEquals(response.getError(), "BODY_NOT_VALID");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createCommentWithoutAuthorization() {

        CommentPOST body = CommentPOST.generateComment();

        ErrorModel response = restWrapperWithoutAuth.usingComments().createItemWithFailure(body);

        log.info("Validate comment isn't created without authorization!");
        assertEquals(response.getError(), "APP_ID_MISSING");

        log.info("Validate status code!");
        int statusCode = restWrapperWithoutAuth.getStatusCode();
        assertEquals(statusCode, SC_FORBIDDEN);
    }

    @Test
    public void createCommentWithoutBody() {

        ErrorModel response = restWrapper.usingComments().createItemWithoutBody();

        log.info("Validate comment isn't created without body!");
        assertEquals(response.getError(), "BODY_NOT_VALID");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }
}
