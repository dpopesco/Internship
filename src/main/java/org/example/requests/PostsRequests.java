package org.example.requests;

import org.example.models.PostGET;
import org.example.models.PostPOST;
import org.example.models.PostsCollection;
import org.example.models.error.ErrorModel;
import org.example.models.error.UserErrorModel;
import org.example.wrappers.RestRequest;
import org.example.wrappers.RestWrapper;
import org.springframework.http.HttpMethod;

public class PostsRequests extends ModelRequest<PostsRequests> {
    public PostsRequests(RestWrapper restWrapper) {
        super(restWrapper);
    }

    public PostsCollection getPosts() {

        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/post");
        return restWrapper.processModel(PostsCollection.class, request);
    }

    public PostGET getInfoByPostId(String postId) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/post/{id}", postId);
        return restWrapper.processModel(PostGET.class, request);
    }

    public PostsCollection getInfoByUserId(String userId) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/user/" + userId + "/post");
        return restWrapper.processModel(PostsCollection.class, request);
    }

    public PostsCollection getInfoByTag(String tag) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/tag/" + tag + "/post");
        return restWrapper.processModel(PostsCollection.class, request);
    }

    public PostGET createPost(PostPOST post) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, post, "/post/create");
        return restWrapper.processModel(PostGET.class, request);
    }

    public ErrorModel createPostWithFailure(PostPOST post) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, post, "/post/create");
        return restWrapper.processModel(ErrorModel.class, request);
    }

    public ErrorModel createPostWithJson(String json) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, json, "/post/create");
        return restWrapper.processModel(ErrorModel.class, request);
    }

    public PostGET updatePost(PostPOST post, String postId) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.PUT, post, "post/" + postId);
        return restWrapper.processModel(PostGET.class, request);
    }

    public ErrorModel updatePostWithFailure(PostPOST post, String postId) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.PUT, post, "post/" + postId);
        return restWrapper.processModel(ErrorModel.class, request);
    }

    public UserErrorModel updatePostWithJson(String json, String postId) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.PUT, json, "/post/" + postId);
        return restWrapper.processModel(UserErrorModel.class, request);
    }

    public PostPOST deletePost(String postId) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.DELETE, "/post/" + postId);
        return restWrapper.processModel(PostPOST.class, request);
    }

    public ErrorModel deletePostWithFailure(String postId) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.DELETE, "/post/" + postId);
        return restWrapper.processModel(ErrorModel.class, request);
    }
}
