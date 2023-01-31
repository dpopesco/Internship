package org.example.tests.api.v1;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeSuite;

public abstract class ApiBaseClass {

    protected static final String APP_ID = "63d230eb88cdfdb1b9a6359f";

    @BeforeSuite(alwaysRun = true)
    protected static void setup() {
        RestAssured.baseURI = "https://dummyapi.io/data/v1";
    }

    protected void logResponse(Response response) {
        response.getBody().prettyPrint();
        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Header: " + response.getHeader("content-type"));
        System.out.println("Response time: " + response.getTime());
    }
}
