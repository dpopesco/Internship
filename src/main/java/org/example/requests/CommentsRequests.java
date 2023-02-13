package org.example.requests;

import org.example.models.comment.Comment;
import org.example.models.comment.CommentPOST;
import org.example.models.comment.CommentsCollection;
import org.example.models.error.ErrorModel;
import org.example.wrappers.RestRequest;
import org.example.wrappers.RestWrapper;
import org.springframework.http.HttpMethod;

public class CommentsRequests extends ModelRequest<CommentsRequests> {

    private final String pathPost = "/post/{id}/comment";
    private final String pathUser = "/user/{id}/comment";
    private final String pathCreate = "/comment/create";
    private final String pathDelete = "/comment/{id}";

    public CommentsRequests(RestWrapper restWrapper) {
        super(restWrapper);
    }

    public CommentsCollection getComments() {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/comment");
        return restWrapper.processModel(CommentsCollection.class, request);
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

    public Comment createComment(CommentPOST body) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, body, pathCreate);
        return restWrapper.processModel(Comment.class, request);
    }

    public ErrorModel createCommentWithFailure(CommentPOST body) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, body, pathCreate);
        return restWrapper.processModel(ErrorModel.class, request);
    }

    public Comment deleteComment(String commentId) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.DELETE, pathDelete, commentId);
        return restWrapper.processModel(Comment.class, request);
    }

    public ErrorModel deleteCommentWithFailure(String commentId) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.DELETE, pathDelete, commentId);
        return restWrapper.processModel(ErrorModel.class, request);
    }
}
