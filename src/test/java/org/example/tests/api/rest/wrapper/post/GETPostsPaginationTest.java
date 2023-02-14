package org.example.tests.api.rest.wrapper.post;

import lombok.extern.slf4j.Slf4j;
import org.example.models.error.ErrorModel;
import org.example.models.post.PostsCollection;
import org.example.tests.api.rest.wrapper.ApiBaseClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

@Slf4j
public class GETPostsPaginationTest extends ApiBaseClass {
    @Test
    public void checkPageNumberAndLimitParameters() {

        PostsCollection post = restWrapper.usingPosts().usingParams("page=1", "limit=10").getItems();

        log.info("Validate provided limit with response list limit");
        assertEquals(post.getData().size(), 10);
        assertEquals(post.getPage(), 1);

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void checkPostsLimit() {

        PostsCollection user = restWrapper.usingPosts().getItems();

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

        ErrorModel post = restWrapper.usingPosts().usingParams("page=" + String.valueOf(pageNumber)).getItemsWithFailure();

        log.info("Validate params not valid");
        assertEquals(post.getError(), "PARAMS_NOT_VALID");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test(dataProvider = "invalidNumbers", description = "bug, api accepts invalid page parameter")
    public void checkInvalidLimitNumber(Object limitNumber) {

        ErrorModel post = restWrapper.usingPosts().usingParams("limit=" + String.valueOf(limitNumber)).getItemsWithFailure();

        log.info("Validate status code!");
        assertEquals(post.getError(), "PARAMS_NOT_VALID");

        log.error("BUG, api accepts invalid page parameter");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void checkLimitOfDisplayedPostsIs50() {

        PostsCollection user = restWrapper.usingPosts().usingParams("limit=50").getItems();

        log.info("Validate default response limit!");
        assertEquals(user.getData().size(), 50);

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }
}
