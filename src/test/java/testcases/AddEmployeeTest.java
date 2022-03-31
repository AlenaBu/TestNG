package testcases;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.AddEmployeePage;
import pages.DashboardPage;
import pages.EmployeeListPage;
import pages.LoginPage;
import utils.CommonMethods;
import utils.ConfigReader;
import utils.Constants;
import utils.ExelReading;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AddEmployeeTest extends CommonMethods {
    @Test(groups = "smoke1")
    public void addEmployee(){
        LoginPage loginPage=new LoginPage();
        loginPage.login(ConfigReader.getPropertyValue("username"),ConfigReader.getPropertyValue("password"));

        DashboardPage dashboardPage=new DashboardPage();
        click(dashboardPage.pimOption);
        click(dashboardPage.addEmployeeButton);

        AddEmployeePage addEmployeePage=new AddEmployeePage();
        sendText(addEmployeePage.firstName, "Johann202020201");
        sendText(addEmployeePage.middleName, "alejandro03030");
        sendText(addEmployeePage.lastName, "rojano9090");
        click(addEmployeePage.saveBtn);
    }
@Test
    public void addMultipleEmloyees(){
        //login
        LoginPage loginPage=new LoginPage();
        loginPage.login(ConfigReader.getPropertyValue("username"),ConfigReader.getPropertyValue("password"));
        //navigate to add employeepage
        DashboardPage dashboardPage=new DashboardPage();
        AddEmployeePage addEmployeePage=new AddEmployeePage();
        EmployeeListPage empList=new EmployeeListPage();
        SoftAssert softAssert=new SoftAssert();

        List<Map<String,String>> newEmployees= ExelReading.excelIntoListMap(Constants.TESTDATA_FILEPATH, "EmployeeData");
        Iterator<Map<String,String>> it=newEmployees.iterator();
        while(it.hasNext()){
            click(dashboardPage.pimOption);
            click(dashboardPage.addEmployeeButton);
            Map<String,String> mapNewEmployee= it.next();
            sendText(addEmployeePage.firstName, mapNewEmployee.get("FirstName"));//key has to match exel
            sendText(addEmployeePage.middleName, mapNewEmployee.get("MiddleName"));
            sendText(addEmployeePage.lastName, mapNewEmployee.get("LastName"));
            //capturing employee id value
            String employeeIdValue=addEmployeePage.employeeID.getAttribute("value");
            sendText(addEmployeePage.photograph, mapNewEmployee.get("Photograhp"));
            //select check box

            if(!addEmployeePage.createLoginCheckBox.isSelected()){
                click(addEmployeePage.createLoginCheckBox);
            }

            //provide credentials for users
            sendText(addEmployeePage.createUsername, mapNewEmployee.get("Username"));
            sendText(addEmployeePage.createPassword, mapNewEmployee.get("Password"));
            sendText(addEmployeePage.rePassword, mapNewEmployee.get("Password"));
            click(addEmployeePage.saveBtn);

            //navigate to emp list page
            click(dashboardPage.pimOption);
            click(dashboardPage.employeeListOption);

            sendText(empList.idEmployee, employeeIdValue);
            click(empList.searchButton);

            List<WebElement> rowData=driver.findElements(By.xpath("//*[@id='resultTable']/tbody/tr"));

            for (int i=0; i< rowData.size(); i++){
                System.out.println("I am inside the loop to get values for newly generated employee");
                String rowText=rowData.get(i).getText();
                System.out.println(rowText);
                String expectedData=employeeIdValue+" "+mapNewEmployee.get("FirstName")+" "+mapNewEmployee.get("MiddleName")+" "+mapNewEmployee.get("LastName");
                softAssert.assertEquals(rowText, expectedData);
            }
        }
        softAssert.assertAll();
    }
}
