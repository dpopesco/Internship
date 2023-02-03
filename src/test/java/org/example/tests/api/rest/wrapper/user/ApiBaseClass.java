package org.example.tests.api.rest.wrapper.user;

import io.restassured.response.Response;
import org.example.utils.Properties;
import org.example.utils.TestContext;
import org.example.wrappers.RestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;

@ContextConfiguration(classes = TestContext.class)
public abstract class ApiBaseClass extends AbstractTestNGSpringContextTests {

    @Autowired
    protected RestWrapper restWrapper;

    @Autowired
    protected RestWrapper restWrapperWithoutAuth;

    @Autowired
    protected Properties properties;

    @BeforeClass(alwaysRun = true)
    public void addHeader() {
        restWrapper.addRequestHeader("app-id", properties.getAppId());
    }

    public void logResponse(Response response) {
        response.getBody().prettyPrint();
        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Header: " + response.getHeader("content-type"));
        System.out.println("Response time: " + response.getTime());
    }
}
