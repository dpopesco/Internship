package org.example.tests.api.rest.wrapper.user;

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
}
