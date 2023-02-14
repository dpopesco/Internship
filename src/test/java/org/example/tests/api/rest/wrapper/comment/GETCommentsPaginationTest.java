package org.example.tests.api.rest.wrapper.comment;

import lombok.extern.slf4j.Slf4j;
import org.example.models.comment.CommentsCollection;
import org.example.models.error.ErrorModel;
import org.example.tests.api.rest.wrapper.ApiBaseClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

@Slf4j
public class GETCommentsPaginationTest extends ApiBaseClass {
    @Test
    public void checkPageNumberAndLimitParameters() {

        CommentsCollection comment = restWrapper.usingComments().usingParams("page=2", "limit=10").getItems();

        log.info("Validate provided limit with response list limit");
        assertEquals(comment.getData().size(), 10);
        assertEquals(comment.getPage(), 2);

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void checkCommentsLimit() {

        CommentsCollection user = restWrapper.usingComments().getItems();

        log.info("Validate default response limit!");
        assertEquals(user.getData().size(), 20);

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @DataProvider(name = "invalidNumbers")
    public Object[][] createInvalidData() {
        return new Object[][]{
                {1000},
                {-1},
                {"Word"},
                {"%%%%%"},
                {1.2}
        };
    }

    @Test(dataProvider = "invalidNumbers", description = "bug, api accepts invalid page parameter")
    public void checkInvalidPageNumber(Object pageNumber) {

        ErrorModel comment = restWrapper.usingComments().usingParams("page=" + String.valueOf(pageNumber)).getItemsWithFailure();

        log.info("Validate params not valid");
        assertEquals(comment.getError(), "PARAMS_NOT_VALID");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test(dataProvider = "invalidNumbers", description = "bug, api accepts invalid page parameter")
    public void checkInvalidLimitNumber(Object limitNumber) {

        ErrorModel comment = restWrapper.usingComments().usingParams("limit=" + String.valueOf(limitNumber)).getItemsWithFailure();

        log.info("Validate status code!");
        assertEquals(comment.getError(), "PARAMS_NOT_VALID");

        log.error("BUG, api accepts invalid page parameter");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void checkLimitOfDisplayedCommentsIs50() {

        CommentsCollection user = restWrapper.usingComments().usingParams("limit=50").getItems();

        log.info("Validate default response limit!");
        assertEquals(user.getData().size(), 50);

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }
}
