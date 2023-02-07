package org.example.wrappers;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.example.exceptions.ConversionJsonToModelException;
import org.example.utils.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static io.restassured.RestAssured.given;

@Service
@Scope(value = "prototype")
public class RestWrapper {

    @Autowired
    private Properties properties;
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

    public Response sendRequest(HttpMethod httpMethod, String path, Object body, String params) {

        Response returnedResponse;

        switch (httpMethod) {
            case GET:
                if (StringUtils.isNotEmpty(params)) {
                    returnedResponse = onRequest().get(path, params).andReturn();
                } else {
                    returnedResponse = onRequest().get(path).andReturn();
                }
                break;
            case POST:
                if (StringUtils.isNotEmpty(params)) {
                    returnedResponse = onRequest().body(body).post(path, params).andReturn();
                } else {
                    returnedResponse = onRequest().body(body).post(path).andReturn();
                }
                break;
            case PUT:
                if (StringUtils.isNotEmpty(params)) {
                    returnedResponse = onRequest().body(body).put(path, params).andReturn();
                } else {
                    returnedResponse = onRequest().body(body).put(path).andReturn();
                }
                break;
            case DELETE:
                if (StringUtils.isNotEmpty(params)) {
                    returnedResponse = onRequest().delete(path, params).andReturn();
                } else {
                    returnedResponse = onRequest().delete(path).andReturn();
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

    public <T> T convertResponseToModel(Response response, Class<T> modelClass) {
        T model;

        try {
            model = response.getBody().as(modelClass);
        } catch (Exception error) {
            error.printStackTrace();
            throw new ConversionJsonToModelException(modelClass, error);
        }
        return model;
    }
}
