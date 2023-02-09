package org.example.requests;

import org.example.models.User;
import org.example.models.UsersCollection;
import org.example.models.error.ErrorModel;
import org.example.models.error.UserErrorModel;
import org.example.wrappers.RestRequest;
import org.example.wrappers.RestWrapper;
import org.springframework.http.HttpMethod;

public class UsersRequests extends ModelRequest<UsersRequests> {
    public UsersRequests(RestWrapper restWrapper) {
        super(restWrapper);
    }

    public User getUser(String userId) {

        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "user/{id}", userId);
        return restWrapper.processModel(User.class, request);
    }

    public ErrorModel getUserAndExpectError(String invalidId) {

        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "user/{id}", invalidId);
        return restWrapper.processModel(ErrorModel.class, request);
    }

    public UsersCollection getUsersWithParams() {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/user?{parameters}", this.getParameters());
        return restWrapper.processModel(UsersCollection.class, request);
    }

    public ErrorModel getUsersWithParamsAndExpectError() {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/user?{parameters}", this.getParameters());
        return restWrapper.processModel(ErrorModel.class, request);
    }

    public UsersCollection getUsers() {

        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "user");
        return restWrapper.processModel(UsersCollection.class, request);
    }

    public User createUser(User user) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, user, "/user/create");
        return restWrapper.processModel(User.class, request);
    }

    public UserErrorModel createUserWithFailure(User user) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, user, "/user/create");
        return restWrapper.processModel(UserErrorModel.class, request);
    }

    public UserErrorModel createUserWithoutBody() {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.POST, "/user/create");
        return restWrapper.processModel(UserErrorModel.class, request);
    }

    public User updateUser(User user, String userId) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.PUT, user, "/user/{id}", userId);
        return restWrapper.processModel(User.class, request);
    }

    public UserErrorModel updateUserWithFailure(User user, String userId) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.PUT, user, "/user/{id}", userId);
        return restWrapper.processModel(UserErrorModel.class, request);
    }

    public User deleteUser(String userId) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.DELETE, "/user/{id}", userId);
        return restWrapper.processModel(User.class, request);
    }

    public ErrorModel deleteUserWithFailure(String userId) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.DELETE, "/user/{id}", userId);
        return restWrapper.processModel(ErrorModel.class, request);
    }
}
