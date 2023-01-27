package api.tests;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public class ApiBaseClass {

    protected static final String APP_ID = "63d230eb88cdfdb1b9a6359f";

    @BeforeClass(alwaysRun = true)
    protected static void setup() {
        RestAssured.baseURI = "https://dummyapi.io/data/v1";
    }
}
