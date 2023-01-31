package org.example.tests.api.spring;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.utils.Properties;
import org.example.utils.TestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;

@ContextConfiguration(classes = TestContext.class)
public abstract class ApiBaseClass extends AbstractTestNGSpringContextTests {

    @Autowired
    protected Properties properties;

    @BeforeMethod(alwaysRun = true)
    protected void setup() {
        RestAssured.baseURI = properties.getAppUrl();
    }

    public void logResponse(Response response) {
        response.getBody().prettyPrint();
        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Header: " + response.getHeader("content-type"));
        System.out.println("Response time: " + response.getTime());
    }
}
