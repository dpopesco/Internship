package org.example.requests;

import lombok.extern.slf4j.Slf4j;
import org.example.models.error.ErrorModel;
import org.example.models.error.UserErrorModel;
import org.example.models.user.User;
import org.example.models.user.UsersCollection;
import org.example.wrappers.RestRequest;
import org.example.wrappers.RestWrapper;
import org.springframework.http.HttpMethod;

@Slf4j
public class UsersRequests extends ModelRequest<UsersRequests> {
    public UsersRequests(RestWrapper restWrapper) {
        super(restWrapper);
    }

    private final String path = "user/{id}";
    private final String userCreate = "/user/create";

    public User getUser(String userId) {

        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, path, userId);
        return restWrapper.processModel(User.class, request);
    }

    public ErrorModel getUserAndExpectError(String invalidId) {

        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, path, invalidId);
        return restWrapper.processModel(ErrorModel.class, request);
    }

    public ErrorModel getUsersWithParamsAndExpectError() {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/user?{parameters}", this.getParameters());
        return restWrapper.processModel(ErrorModel.class, request);
    }

    public UsersCollection getUsers() {

        log.info("Entering getUsers method where request is built and response is converted to model!");
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/user?{parameters}", this.getParameters());
        return restWrapper.processModel(UsersCollection.class, request);
    }

    public User createUser(User user) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, user, userCreate);
        return restWrapper.processModel(User.class, request);
    }

    public UserErrorModel createUserWithFailure(User user) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, user, userCreate);
        return restWrapper.processModel(UserErrorModel.class, request);
    }

    public UserErrorModel createUserWithoutBody() {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.POST, userCreate);
        return restWrapper.processModel(UserErrorModel.class, request);
    }

    public User updateUser(User user, String userId) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.PUT, user, path, userId);
        return restWrapper.processModel(User.class, request);
    }

    public UserErrorModel updateUserWithFailure(User user, String userId) {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.PUT, user, path, userId);
        return restWrapper.processModel(UserErrorModel.class, request);
    }

    public User deleteUser(String userId) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.DELETE, path, userId);
        return restWrapper.processModel(User.class, request);
    }

    public ErrorModel deleteUserWithFailure(String userId) {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.DELETE, path, userId);
        return restWrapper.processModel(ErrorModel.class, request);
    }
}
