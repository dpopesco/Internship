package org.example.requests;

import org.example.models.error.ErrorModel;
import org.example.models.error.UserErrorModel;
import org.example.models.post.PostGET;
import org.example.models.post.PostPOST;
import org.example.models.post.PostsCollection;
import org.example.wrappers.RestRequest;
import org.example.wrappers.RestWrapper;
import org.springframework.http.HttpMethod;

public class PostsRequests extends ModelRequest<PostsRequests> {

    private final String path = "/post/{id}";
    private final String pathCreate = "/post/create";

    public PostsRequests(RestWrapper restWrapper) {
        super(restWrapper);
    }

    public PostsCollection getPosts() {

        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/post");
        return restWrapper.processModel(PostsCollection.class, request);
    }

    public PostGET getInfoByPostId(String postId) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, path, postId);
        return restWrapper.processModel(PostGET.class, request);
    }

    public PostsCollection getInfoByUserId(String userId) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/user/{id}/post", userId);
        return restWrapper.processModel(PostsCollection.class, request);
    }

    public PostsCollection getInfoByTag(String tag) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/tag/{tag}/post", tag);
        return restWrapper.processModel(PostsCollection.class, request);
    }

    public PostGET createPost(PostPOST post) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, post, pathCreate);
        return restWrapper.processModel(PostGET.class, request);
    }

    public ErrorModel createPostWithFailure(PostPOST post) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, post, pathCreate);
        return restWrapper.processModel(ErrorModel.class, request);
    }

    public ErrorModel createPostWithFailure(String json) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, json, pathCreate);
        return restWrapper.processModel(ErrorModel.class, request);
    }

    public PostGET updatePost(PostPOST post, String postId) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.PUT, post, path, postId);
        return restWrapper.processModel(PostGET.class, request);
    }

    public ErrorModel updatePostWithFailure(PostPOST post, String postId) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.PUT, post, path, postId);
        return restWrapper.processModel(ErrorModel.class, request);
    }

    public UserErrorModel updatePostWithFailure(String json, String postId) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.PUT, json, path, postId);
        return restWrapper.processModel(UserErrorModel.class, request);
    }

    public PostPOST deletePost(String postId) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.DELETE, path, postId);
        return restWrapper.processModel(PostPOST.class, request);
    }

    public ErrorModel deletePostWithFailure(String postId) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.DELETE, path, postId);
        return restWrapper.processModel(ErrorModel.class, request);
    }
}
