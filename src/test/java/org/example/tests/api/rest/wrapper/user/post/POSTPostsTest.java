package org.example.tests.api.rest.wrapper.user.post;

import io.restassured.response.Response;
import org.example.models.PostGET;
import org.example.models.PostPOST;
import org.example.models.User;
import org.example.models.error.ErrorModel;
import org.example.tests.api.rest.wrapper.user.ApiBaseClass;
import org.springframework.http.HttpMethod;
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
        Response responseUser = restWrapper.sendRequest(HttpMethod.POST, "/user/create", user, "");
        User userResponseM = restWrapper.convertResponseToModel(responseUser, User.class);

        PostPOST post = PostPOST.generateRandomPost();
        post.setOwnerId(userResponseM.getId());

        Response response = restWrapper.sendRequest(HttpMethod.POST, "/post/create", post, "");

        logResponse(response);

        //Validate post is created successfully
        PostGET postM = restWrapper.convertResponseToModel(response, PostGET.class);
        assertEquals(postM.getText(), post.getText());
        assertEquals(postM.getImage(), post.getImage());
        assertEquals(postM.getLikes(), post.getLikes());
        assertEquals(postM.getTags(), post.getTags());
        assertEquals(postM.getUser().getId(), post.getOwnerId());


        // Validate status code
        int statusCode = response.getStatusCode();
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
        Response response = restWrapper.sendRequest(HttpMethod.POST, "/post/create", post, "");

        logResponse(response);

        //Validate post is created successfully
        PostGET postM = restWrapper.convertResponseToModel(response, PostGET.class);
        assertEquals(postM.getText(), post.getText());
        assertEquals(postM.getImage(), post.getImage());
        assertEquals(postM.getLikes(), post.getLikes());
        assertEquals(postM.getTags(), post.getTags());
        assertEquals(postM.getUser().getId(), post.getOwnerId());

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_CREATED);
    }

    @Test(description = "bug, api accepts text bigger than 1000")
    public void createPostWithTextBiggerThan1000Characters() {
        PostPOST post = PostPOST.generateRandomPost();
        post.setOwnerId("63e0d8d3c2fbb95b9f900a95");
        post.setText(randomAlphanumeric(1001));

        Response response = restWrapper.sendRequest(HttpMethod.POST, "/post/create", post, "");

        logResponse(response);

        //Validate post is created successfully
        ErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, ErrorModel.class);
        assertEquals(errorResponseModel.getError(), "BODY_NOT_VALID");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }

    @Test
    public void createPostWithoutOwnerId() {
        PostPOST post = new PostPOST();
        post.setText("hgjf");
        Response response = restWrapper.sendRequest(HttpMethod.POST, "/post/create", post, "");

        logResponse(response);
        //Validate post is created successfully
        ErrorModel errorResponseModel = restWrapper.convertResponseToModel(response, ErrorModel.class);
        assertEquals(errorResponseModel.getError(), "BODY_NOT_VALID");

        // Validate status code
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_BAD_REQUEST);
    }
}
