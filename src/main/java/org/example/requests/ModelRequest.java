package org.example.requests;

import lombok.extern.slf4j.Slf4j;
import org.example.wrappers.RestWrapper;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
public abstract class ModelRequest<Request> {
    protected RestWrapper restWrapper;
    private String parameters = "";

    public ModelRequest(RestWrapper restWrapper) {
        this.restWrapper = restWrapper;
    }

    public Request usingParams(String... parameters) {
        log.info("Entering method where we can use parameters for requests!");
        String paramsStr = Arrays.stream(parameters).collect(Collectors.joining("&"));
        setParameters(paramsStr);
        return (Request) this;
    }

    protected String getParameters() {
        String localParam = parameters;
        clearParameters();
        return localParam;
    }

    protected void setParameters(String parameters) {
        this.parameters = parameters;
    }

    private void clearParameters() {
        log.info("Clearing parameters!");
        setParameters("");
    }
}
