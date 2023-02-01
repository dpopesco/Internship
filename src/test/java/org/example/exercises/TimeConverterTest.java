package org.example.exercises;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class TimeConverterTest extends BaseClass {

    TimeConverter timeConverter;


    @BeforeClass(alwaysRun = true)
    public void setup() {
        timeConverter = new TimeConverter();
    }

    @Test(groups = "validTests")
    public void checkValidConversion() {
        assertEquals(timeConverter.convertToMilliseconds(3, 44, 50), 13490000);
    }

    @Test(groups = "validTests")
    public void checkTrueConversion() {
        assertTrue(timeConverter.convertToMilliseconds(2, 3, 59) == 7439000);
    }

    @Test(groups = "invalidTests")
    public void checkInvalidConversion() {
        assertFalse(timeConverter.convertToMilliseconds(1, 1, 1) == 3600);
    }
}
