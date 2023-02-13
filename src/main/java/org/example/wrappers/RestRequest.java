package org.example.wrappers;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;

@Getter
@Setter
@Slf4j
public class RestRequest {
    private Object body;
    private HttpMethod httpMethod;
    private String path;
    private String[] pathParams;

    private RestRequest(HttpMethod httpMethod, String path, String[] pathParams) {
        setHttpMethod(httpMethod);
        setPath(path);
        setPathParams(pathParams);
    }

    private RestRequest(HttpMethod httpMethod, Object body, String path, String[] pathParams) {
        setBody(body);
        setHttpMethod(httpMethod);
        setPath(path);
        setPathParams(pathParams);
    }

    public static RestRequest simpleRequest(HttpMethod httpMethod, String path, String... pathParams) {
        log.trace("Entering simple request!");
        return new RestRequest(httpMethod, path, pathParams);
    }

    public static RestRequest requestWithBody(HttpMethod httpMethod, Object body, String path, String... pathParams) {
        log.trace("Entering request with body!");
        return new RestRequest(httpMethod, body, path, pathParams);
    }
}
