package org.example.wrappers;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.ConversionJsonToModelException;
import org.example.requests.CommentsRequests;
import org.example.requests.PostsRequests;
import org.example.requests.UsersRequests;
import org.example.utils.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static io.restassured.RestAssured.given;

@Service
@Scope(value = "prototype")
@Slf4j
public class RestWrapper {

    @Autowired
    private Properties properties;

    private int statusCode;
    private RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();

    @PostConstruct
    public void initializeRequestSpecBuilder() {
        String url = properties.getAppUrl();
        RestAssured.baseURI = url;
        configureRequestSpec().setBaseUri(url);
    }

    public RequestSpecBuilder configureRequestSpec() {
        return this.requestSpecBuilder;
    }

    public RestWrapper addRequestHeader(String headerKey, String headerValue) {
        configureRequestSpec().addHeader(headerKey, headerValue);
        return this;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Response sendRequest(RestRequest restRequest) {

        Response returnedResponse;

        switch (restRequest.getHttpMethod()) {
            case GET:
                if (restRequest.getPathParams().length != 0) {
                    returnedResponse = onRequest().get(restRequest.getPath(), restRequest.getPathParams()).andReturn();
                } else {
                    returnedResponse = onRequest().get(restRequest.getPath()).andReturn();
                }
                break;
            case POST:
                RequestSpecification requestSpecification = onRequest();
                if (restRequest.getBody() != null) {
                    requestSpecification.body(restRequest.getBody());
                }
                if (restRequest.getPathParams().length != 0) {
                    returnedResponse = requestSpecification.post(restRequest.getPath(), restRequest.getPathParams()).andReturn();
                } else {
                    returnedResponse = requestSpecification.post(restRequest.getPath()).andReturn();
                }
                break;
            case PUT:
                if (restRequest.getPathParams().length != 0) {
                    returnedResponse = onRequest().body(restRequest.getBody()).put(restRequest.getPath(), restRequest.getPathParams()).andReturn();
                } else {
                    returnedResponse = onRequest().body(restRequest.getBody()).put(restRequest.getPath()).andReturn();
                }
                break;
            case DELETE:
                if (restRequest.getPathParams().length != 0) {
                    returnedResponse = onRequest().delete(restRequest.getPath(), restRequest.getPathParams()).andReturn();
                } else {
                    returnedResponse = onRequest().delete(restRequest.getPath()).andReturn();
                }
                break;
            default:
                throw new RuntimeException("Please send a valid method");
        }
        return returnedResponse;
    }

    protected RequestSpecification onRequest() {
        return given().spec(configureRequestSpec().setContentType(ContentType.JSON).build());
    }

    public <T> T processModel(Class<T> modelClass, RestRequest restRequest) {
        return sendRequestAndCreateModel(modelClass, restRequest);
    }

    private <T> T sendRequestAndCreateModel(Class<T> modelClass, RestRequest restRequest) {
        Response returnedResponse = sendRequest(restRequest);
        logResponse(returnedResponse);
        setStatusCode(returnedResponse.getStatusCode());
        T model;

        try {
            model = returnedResponse.getBody().as(modelClass);
        } catch (Exception error) {
            error.printStackTrace();
            throw new ConversionJsonToModelException(modelClass, error);
        }
        return model;
    }

    public void logResponse(Response response) {
        response.getBody().prettyPrint();
        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Header: " + response.getHeader("content-type"));
        System.out.println("Response time: " + response.getTime());
    }

    public UsersRequests usingUsers() {
        log.info("Entering method where we are creating User Request object!");
        return new UsersRequests(this);
    }

    public PostsRequests usingPosts() {
        log.info("Entering method where we are creating Post Request object!");
        return new PostsRequests(this);
    }

    public CommentsRequests usingComments() {
        log.info("Entering method where we are creating Comment Request object!");
        return new CommentsRequests(this);
    }
}
