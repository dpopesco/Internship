package org.example.tests.api.rest.wrapper;

import lombok.extern.slf4j.Slf4j;
import org.example.utils.PlainTextReporter;
import org.example.utils.Properties;
import org.example.utils.TestContext;
import org.example.wrappers.RestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.*;

@Slf4j
@ContextConfiguration(classes = TestContext.class)
@Listeners(PlainTextReporter.class)
public abstract class ApiBaseClass extends AbstractTestNGSpringContextTests {

    @Autowired
    protected RestWrapper restWrapper;

    @Autowired
    protected RestWrapper restWrapperWithoutAuth;

    @Autowired
    protected Properties properties;

    @BeforeClass(alwaysRun = true)
    public void addHeader() {
        log.info("Entering: " + this.getClass().toString());
        log.info("Setting in header app-id!");
        restWrapper.addRequestHeader("app-id", properties.getAppId());
    }

    @AfterClass(alwaysRun = true)
    public void addLogForExitingClassTests() {
        log.info("Exiting: " + this.getClass().toString());
    }

    @BeforeMethod
    public void addBeforeMethodLog() {
        log.info("Entering test!");
    }

    @AfterMethod
    public void addAfterMethodLog() {
        log.info("Exiting test!");
    }
}
