package org.example.tests.api.rest.wrapper.post;

import lombok.extern.slf4j.Slf4j;
import org.example.models.error.ErrorModel;
import org.example.models.post.PostGET;
import org.example.models.post.PostPOST;
import org.example.models.user.User;
import org.example.tests.api.rest.wrapper.ApiBaseClass;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.testng.Assert.assertEquals;

@Slf4j
public class POSTPostsTest extends ApiBaseClass {
    @Test
    public void createNewPost() {

        User user = User.generateRandomUser();
        User responseCreate = restWrapper.usingUsers().createUser(user);

        PostPOST post = PostPOST.generateRandomPost();
        post.setOwnerId(responseCreate.getId());

        PostGET response = restWrapper.usingPosts().createPost(post);

        log.info("Validate post is created successfully!");
        assertEquals(response.getText(), post.getText());
        assertEquals(response.getImage(), post.getImage());
        assertEquals(response.getLikes(), post.getLikes());
        assertEquals(response.getTags(), post.getTags());
        assertEquals(response.getUser().getId(), post.getOwnerId());


        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_CREATED);
    }

    @DataProvider(name = "postMandatoryFields")
    public Object[][] createData() {
        return new Object[][]{
                {PostPOST.generateRandomPost()},
                {PostPOST.generateRandomPost()},
                {PostPOST.generateRandomPost()}
        };
    }

    @Test(dataProvider = "postMandatoryFields")
    public void createPost(PostPOST post) {

        post.setOwnerId("63e0d8d3c2fbb95b9f900a95");

        PostGET response = restWrapper.usingPosts().createPost(post);

        log.info("Validate post is created successfully!");
        assertEquals(response.getText(), post.getText());
        assertEquals(response.getImage(), post.getImage());
        assertEquals(response.getLikes(), post.getLikes());
        assertEquals(response.getTags(), post.getTags());
        assertEquals(response.getUser().getId(), post.getOwnerId());

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_CREATED);
    }

    @Test(description = "bug, api accepts text bigger than 1000")
    public void createPostWithTextBiggerThan1000Characters() {
        PostPOST post = PostPOST.generateRandomPost();
        post.setOwnerId("63e0d8d3c2fbb95b9f900a95");
        post.setText(randomAlphanumeric(1001));

        log.info("Validate post is not created as text has more than 1000 characters!");
        log.error("Bug, api accepts text bigger than 1000!");

        ErrorModel response = restWrapper.usingPosts().createPostWithFailure(post);

        assertEquals(response.getError(), "BODY_NOT_VALID");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createPostWithoutOwnerId() {
        PostPOST post = new PostPOST();
        post.setText("hgjf");

        ErrorModel response = restWrapper.usingPosts().createPostWithFailure(post);

        log.info("Validate post cannot be created without ownerId!");
        assertEquals(response.getError(), "BODY_NOT_VALID");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createPostWithCharactersForLikes() {
        PostPOST post = new PostPOST();
        JSONObject postS = new JSONObject(post);
        postS.put("likes", "rfr");

        ErrorModel response = restWrapper.usingPosts().createPostWithFailure(postS.toString());

        log.info("Validate post cannot be created when sent characters for number of likes!");
        assertEquals(response.getError(), "BODY_NOT_VALID");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @DataProvider(name = "invalidData")
    public Object[][] createInvalidData() {
        return new Object[][]{
                {10.5},
                {-1},
                {-10.5},
        };
    }

    @Test(dataProvider = "invalidData")
    public void createPostWithInvalidDataForLikes(Object likes) {

        PostPOST post = new PostPOST();
        JSONObject postS = new JSONObject(post);
        postS.put("likes", likes);

        ErrorModel response = restWrapper.usingPosts().createPostWithFailure(postS.toString());

        log.info("Validate post cannot be created when sent double type and negative figures for number of likes!");
        assertEquals(response.getError(), "BODY_NOT_VALID");

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }
}
