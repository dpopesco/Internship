package org.example.requests;

import org.example.exceptions.ConversionJsonToModelException;
import org.example.models.error.ErrorModel;
import org.example.models.error.UserErrorModel;
import org.example.models.post.Post;
import org.example.models.post.PostGET;
import org.example.models.post.PostPOST;
import org.example.models.post.PostsCollection;
import org.example.wrappers.RestRequest;
import org.example.wrappers.RestWrapper;
import org.springframework.http.HttpMethod;

public class PostsRequests extends ModelRequest<PostsRequests> implements APIContract<Post, PostsCollection, ErrorModel> {

    private final String path = "/post/{id}";
    private final String pathCreate = "/post/create";

    public PostsRequests(RestWrapper restWrapper) {
        super(restWrapper);
    }

    @Override
    public PostGET getItem(String itemId) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, path, itemId);
        return restWrapper.processModel(PostGET.class, request);
    }

    @Override
    public ErrorModel getItemWithFailure(String itemId) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, path, itemId);
        return restWrapper.processModel(ErrorModel.class, request);
    }

    @Override
    public PostsCollection getItems() throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/post?{parameters}", this.getParameters());
        return restWrapper.processModel(PostsCollection.class, request);
    }

    @Override
    public ErrorModel getItemsWithFailure() throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/post?{parameters}", this.getParameters());
        return restWrapper.processModel(ErrorModel.class, request);
    }

    @Override
    public PostGET createItem(Post item) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, item, pathCreate);
        return restWrapper.processModel(PostGET.class, request);
    }

    @Override
    public ErrorModel createItemWithFailure(Post item) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, item, pathCreate);
        return restWrapper.processModel(ErrorModel.class, request);
    }

    @Override
    public ErrorModel createItemWithoutBody() throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.POST, pathCreate);
        return restWrapper.processModel(UserErrorModel.class, request);
    }

    @Override
    public PostGET updateItem(String itemId, Post updatedItem) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.PUT, updatedItem, path, itemId);
        return restWrapper.processModel(PostGET.class, request);
    }

    @Override
    public ErrorModel updateItemWithFailure(String itemId, Post updatedItem) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.PUT, updatedItem, path, itemId);
        return restWrapper.processModel(ErrorModel.class, request);
    }

    @Override
    public PostPOST deleteItem(String itemId) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.DELETE, path, itemId);
        return restWrapper.processModel(PostPOST.class, request);
    }

    @Override
    public ErrorModel deleteItemWithFailure(String itemId) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.DELETE, path, itemId);
        return restWrapper.processModel(ErrorModel.class, request);
    }

    public PostsCollection getInfoByUserId(String userId) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/user/{id}/post", userId);
        return restWrapper.processModel(PostsCollection.class, request);
    }

    public PostsCollection getInfoByTag(String tag) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/tag/{tag}/post", tag);
        return restWrapper.processModel(PostsCollection.class, request);
    }

    public ErrorModel createPostWithFailure(String json) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, json, pathCreate);
        return restWrapper.processModel(ErrorModel.class, request);
    }

    public UserErrorModel updatePostWithFailure(String json, String postId) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.PUT, json, path, postId);
        return restWrapper.processModel(UserErrorModel.class, request);
    }
}
