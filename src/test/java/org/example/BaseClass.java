package org.example;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

public class BaseClass {

    @BeforeSuite
    public void beforeSuite() {
        System.out.println(" BeforeSuite: Suite execution begins: ");
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println(" AfterSuite: Execution finished! ");
    }

    @BeforeTest
    public void beforeTest() {
        System.out.println(" This method is invoked before every test tag from testng.xml! ");
    }
}
