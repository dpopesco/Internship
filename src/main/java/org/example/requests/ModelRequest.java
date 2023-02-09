package org.example.requests;

import org.example.wrappers.RestWrapper;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class ModelRequest<Request> {
    protected RestWrapper restWrapper;
    private String parameters = "";
    public ModelRequest(RestWrapper restWrapper) {
        this.restWrapper = restWrapper;
    }
    public Request usingParams(String... parameters) {
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
        setParameters("");
    }
}
