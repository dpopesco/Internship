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
    public String deleteComment() {

        Comment responseCreate = restWrapper.usingComments().createComment(CommentPOST.generateComment());
        String commentId = responseCreate.getId();

        Comment response = restWrapper.usingComments().deleteComment(commentId);

        log.info("Validate comment is deleted successfully!");
        assertNull(response.getMessage());

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
        return commentId;
    }

    @Test
    public void deleteAlreadyDeletedComment() {

        String deletedComment = deleteComment();
        ErrorModel response = restWrapper.usingComments().deleteCommentWithFailure(deletedComment);

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
