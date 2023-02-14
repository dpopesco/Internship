package org.example.requests;

import org.example.exceptions.ConversionJsonToModelException;
import org.example.models.comment.Comment;
import org.example.models.comment.CommentGET;
import org.example.models.comment.CommentsCollection;
import org.example.models.error.ErrorModel;
import org.example.wrappers.RestRequest;
import org.example.wrappers.RestWrapper;
import org.springframework.http.HttpMethod;

public class CommentsRequests extends ModelRequest<CommentsRequests> implements APIContract<Comment, CommentsCollection, ErrorModel> {

    private final String pathPost = "/post/{id}/comment";
    private final String pathUser = "/user/{id}/comment";
    private final String pathCreate = "/comment/create";
    private final String pathDelete = "/comment/{id}";

    public CommentsRequests(RestWrapper restWrapper) {
        super(restWrapper);
    }

    @Override
    public CommentsCollection getItems() throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/comment?{parameters}", this.getParameters());
        return restWrapper.processModel(CommentsCollection.class, request);
    }

    @Override
    public ErrorModel getItemsWithFailure() throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/comment?{parameters}", this.getParameters());
        return restWrapper.processModel(ErrorModel.class, request);
    }

    @Override
    public CommentGET createItem(Comment item) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, item, pathCreate);
        return restWrapper.processModel(CommentGET.class, request);
    }

    @Override
    public ErrorModel createItemWithFailure(Comment item) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, item, pathCreate);
        return restWrapper.processModel(ErrorModel.class, request);
    }

    @Override
    public ErrorModel createItemWithoutBody() throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.POST, pathCreate);
        return restWrapper.processModel(ErrorModel.class, request);
    }

    @Override
    public CommentGET deleteItem(String itemId) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.DELETE, pathDelete, itemId);
        return restWrapper.processModel(CommentGET.class, request);
    }

    @Override
    public ErrorModel deleteItemWithFailure(String itemId) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.DELETE, pathDelete, itemId);
        return restWrapper.processModel(ErrorModel.class, request);
    }

    //For comment endpoint api doesn't have get by commentId and update commentId requests, therefore methods are not implemented
    @Override
    public Comment getItem(String itemId) throws ConversionJsonToModelException {
        return null;
    }

    @Override
    public ErrorModel getItemWithFailure(String itemId) throws ConversionJsonToModelException {
        return null;
    }

    @Override
    public Comment updateItem(String itemId, Comment updatedItem) throws ConversionJsonToModelException {
        return null;
    }

    @Override
    public ErrorModel updateItemWithFailure(String itemId, Comment updatedItem) throws ConversionJsonToModelException {
        return null;
    }

    public CommentsCollection getCommentsByPostId(String postId) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, pathPost, postId);
        return restWrapper.processModel(CommentsCollection.class, request);
    }

    public ErrorModel getCommentsByPostIdWithFailure(String postId) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, pathPost, postId);
        return restWrapper.processModel(ErrorModel.class, request);
    }

    public CommentsCollection getCommentsByUserId(String userId) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, pathUser, userId);
        return restWrapper.processModel(CommentsCollection.class, request);
    }

    public ErrorModel getCommentsByUserIdWithFailure(String userId) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, pathUser, userId);
        return restWrapper.processModel(ErrorModel.class, request);
    }
}
