package org.example;

import org.testng.annotations.*;

import static org.testng.Assert.assertEquals;


@Test(groups = "validTests")
public class EmployeeDetailsTest {

    @BeforeSuite
    public void beforeSuite() {
        System.out.println(" BeforeSuite: Suite execution begins: ");
    }

    @BeforeClass
    public void beforeClass() {
        System.out.println(" BeforeClass: Tests from EmployeeDetailsTest are executed.");
    }

    EmpBusinessLogic empBusinessLogic;
    EmployeeDetails employee;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        empBusinessLogic = new EmpBusinessLogic();
        employee = new EmployeeDetails();
    }

    //test to check appraisal
    private void calculateAppraisalTest() {
        employee.setName("Lola");
        employee.setAge(15);
        employee.setMonthlySalary(9000);

        double appraisal = empBusinessLogic.calculateAppraisal(employee);
        assertEquals(appraisal, 500, 0.0, "500");
    }

    //test to check yearly salary
    public void calculateYearlySalary() {
        employee.setName("Lola");
        employee.setAge(15);
        employee.setMonthlySalary(9000);

        double salary = empBusinessLogic.calculateYearlySalary(employee);
        assertEquals(salary, 108000, 0.0, "9000");
    }

    @AfterClass
    public void afterClass() {
        System.out.println(" AfterClass: Tests from EmployeeDetailsTest have been executed.");
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println(" AfterSuite: Execution finished! ");
    }
}
