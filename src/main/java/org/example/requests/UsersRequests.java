package org.example.requests;

import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.ConversionJsonToModelException;
import org.example.models.error.ErrorModel;
import org.example.models.error.UserErrorModel;
import org.example.models.user.User;
import org.example.models.user.UsersCollection;
import org.example.wrappers.RestRequest;
import org.example.wrappers.RestWrapper;
import org.springframework.http.HttpMethod;

@Slf4j
public class UsersRequests extends ModelRequest<UsersRequests> implements APIContract<User, UsersCollection, ErrorModel> {
    public UsersRequests(RestWrapper restWrapper) {
        super(restWrapper);
    }

    private final String path = "user/{id}";
    private final String userCreate = "/user/create";

    @Override
    public User getItem(String itemId) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, path, itemId);
        return restWrapper.processModel(User.class, request);
    }

    @Override
    public ErrorModel getItemWithFailure(String itemId) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, path, itemId);
        return restWrapper.processModel(ErrorModel.class, request);
    }

    @Override
    public UsersCollection getItems() throws ConversionJsonToModelException {
        log.info("Entering getUsers method where request is built and response is converted to model!");
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/user?{parameters}", this.getParameters());
        return restWrapper.processModel(UsersCollection.class, request);
    }

    @Override
    public ErrorModel getItemsWithFailure() throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.GET, "/user?{parameters}", this.getParameters());
        return restWrapper.processModel(ErrorModel.class, request);
    }

    @Override
    public User createItem(User item) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, item, userCreate);
        return restWrapper.processModel(User.class, request);
    }

    @Override
    public UserErrorModel createItemWithFailure(User item) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.POST, item, userCreate);
        return restWrapper.processModel(UserErrorModel.class, request);
    }

    @Override
    public UserErrorModel createItemWithoutBody() throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.POST, userCreate);
        return restWrapper.processModel(UserErrorModel.class, request);
    }

    @Override
    public User updateItem(String itemId, User updatedItem) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.PUT, updatedItem, path, itemId);
        return restWrapper.processModel(User.class, request);
    }

    @Override
    public UserErrorModel updateItemWithFailure(String itemId, User updatedItem) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.requestWithBody(HttpMethod.PUT, updatedItem, path, itemId);
        return restWrapper.processModel(UserErrorModel.class, request);
    }

    @Override
    public User deleteItem(String itemId) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.DELETE, path, itemId);
        return restWrapper.processModel(User.class, request);
    }

    @Override
    public ErrorModel deleteItemWithFailure(String itemId) throws ConversionJsonToModelException {
        RestRequest request = RestRequest.simpleRequest(HttpMethod.DELETE, path, itemId);
        return restWrapper.processModel(ErrorModel.class, request);
    }
}
