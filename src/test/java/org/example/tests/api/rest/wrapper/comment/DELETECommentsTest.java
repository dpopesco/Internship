package org.example.tests.api.rest.wrapper.comment;

import lombok.extern.slf4j.Slf4j;
import org.example.models.comment.Comment;
import org.example.models.comment.CommentPOST;
import org.example.models.error.ErrorModel;
import org.example.tests.api.rest.wrapper.ApiBaseClass;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

@Slf4j
public class DELETECommentsTest extends ApiBaseClass {
    @Test
    public void deleteComment() {

        log.info("New comment is created!");
        Comment responseCreate = restWrapper.usingComments().createComment(CommentPOST.generateComment());
        log.info("Created id from response is saved!");
        String commentId = responseCreate.getId();

        Comment response = restWrapper.usingComments().deleteComment(commentId);

        log.info("Validate comment is deleted successfully!");
        assertNull(response.getMessage());

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void deleteAlreadyDeletedComment() {

        ErrorModel response = restWrapper.usingComments().deleteCommentWithFailure("63e8ed79afe0a1bac5899c46");

        log.info("Validate comment is already deleted!");
        assertEquals(response.getError(), "RESOURCE_NOT_FOUND");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_NOT_FOUND);
    }

    @Test
    public void deleteCommentWithoutAuthorization() {

        ErrorModel response = restWrapperWithoutAuth.usingComments().deleteCommentWithFailure("63e8ed79afe");

        log.info("Validate comment cannot be deleted without authorization!");
        assertEquals(response.getError(), "APP_ID_MISSING");

        log.info("Validate status code!");
        int statusCode = restWrapperWithoutAuth.getStatusCode();
        assertEquals(statusCode, SC_FORBIDDEN);
    }
}
