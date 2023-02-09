package org.example.tests.api.rest.wrapper.user.post;

import org.example.models.PostGET;
import org.example.models.PostPOST;
import org.example.models.User;
import org.example.models.error.ErrorModel;
import org.example.requests.PostsRequests;
import org.example.requests.UsersRequests;
import org.example.tests.api.rest.wrapper.user.ApiBaseClass;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.testng.Assert.assertEquals;

public class POSTPostsTest extends ApiBaseClass {
    @Test
    public void createNewPost() {

        User user = User.generateRandomUser();
        UsersRequests requestCreate = new UsersRequests(restWrapper);
        User responseCreate = requestCreate.createUser(user);

        PostPOST post = PostPOST.generateRandomPost();
        post.setOwnerId(responseCreate.getId());

        PostsRequests request = new PostsRequests(restWrapper);
        PostGET response = request.createPost(post);

        //Validate post is created successfully
        assertEquals(response.getText(), post.getText());
        assertEquals(response.getImage(), post.getImage());
        assertEquals(response.getLikes(), post.getLikes());
        assertEquals(response.getTags(), post.getTags());
        assertEquals(response.getUser().getId(), post.getOwnerId());


        // Validate status code
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

        PostsRequests request = new PostsRequests(restWrapper);
        PostGET response = request.createPost(post);

        //Validate post is created successfully
        assertEquals(response.getText(), post.getText());
        assertEquals(response.getImage(), post.getImage());
        assertEquals(response.getLikes(), post.getLikes());
        assertEquals(response.getTags(), post.getTags());
        assertEquals(response.getUser().getId(), post.getOwnerId());

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_CREATED);
    }

    @Test(description = "bug, api accepts text bigger than 1000")
    public void createPostWithTextBiggerThan1000Characters() {
        PostPOST post = PostPOST.generateRandomPost();
        post.setOwnerId("63e0d8d3c2fbb95b9f900a95");
        post.setText(randomAlphanumeric(1001));

        PostsRequests request = new PostsRequests(restWrapper);
        ErrorModel response = request.createPostWithFailure(post);

        //Validate post is created successfully
        assertEquals(response.getError(), "BODY_NOT_VALID");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createPostWithoutOwnerId() {
        PostPOST post = new PostPOST();
        post.setText("hgjf");

        PostsRequests request = new PostsRequests(restWrapper);
        ErrorModel response = request.createPostWithFailure(post);
        //Validate post is created successfully
        assertEquals(response.getError(), "BODY_NOT_VALID");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createPostWithCharactersForLikes() {
        PostPOST post = new PostPOST();
        JSONObject postS = new JSONObject(post);

        postS.put("likes", "rfr");

        PostsRequests request = new PostsRequests(restWrapper);
        ErrorModel response = request.createPostWithJson(postS.toString());
        //Validate post is created successfully
        assertEquals(response.getError(), "BODY_NOT_VALID");

        // Validate status code
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }
}
