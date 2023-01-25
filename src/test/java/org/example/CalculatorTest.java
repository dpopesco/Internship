package org.example;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CalculatorTest {
    Calculator calculator;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        calculator = new Calculator();

    }

    @AfterMethod
    public void afterMethod() {
        System.out.println("This is invoked after each method, except for the skipped method. ");
    }

    @DataProvider(name = "sumData")
    public Object[][] createData() {
        return new Object[][]{
                {2, 3, 5},
                {15, 5, 20}
        };
    }

    @Test(description = "This test checks a valid sum.", groups = "validTests", priority = 0, dataProvider = "sumData")
    public void checkValidSum(int n1, int n2, int sum) {
        assertEquals(calculator.sum(n1, n2), sum);
    }

    @Test(groups = "validTests", dependsOnMethods = {"checkValidSum"}, priority = 0)
    public void checkValidSubstraction() {
        assertEquals(calculator.substraction(10, 5), 5);
    }

    @Test(groups = "validTests", priority = 0)
    public void checkValidMultiplication() {
        assertTrue(calculator.multiply(2, 3) == 6);
    }

    @Test(groups = "invalidTests", enabled = false)
    public void checkInvalidSum() {
        assertEquals(calculator.sum(3, 4), 0, "Suma nu a fost calculata corect.");
    }

    @Test(groups = "invalidTests")
    public void checkInvalidSubstraction() {
        assertFalse(calculator.substraction(5, 4) == 0, "Substraction wasn't correctly computed.");
    }

    @Test(groups = "invalidTests", priority = 1)
    public void checkInvalidMultiplication() {
        assertEquals(calculator.multiply(3, 6), 12, "Multiplication wasn't correctly computed.");
    }

    @Test
    public void skippedTest() {
        throw new SkipException("I want to skip this test");
    }
}
