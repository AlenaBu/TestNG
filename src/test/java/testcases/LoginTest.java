package testcases;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.CommonMethods;
import utils.ConfigReader;

public class LoginTest extends CommonMethods {

    @Test(groups = "regression")
    public void adminLogin(){
        LoginPage login=new LoginPage();
        sendText(login.usernameBox, ConfigReader.getPropertyValue("username"));
        sendText(login.passwordBox, ConfigReader.getPropertyValue("password"));
        click(login.loginBtn);

        //assertion
        DashboardPage dashboardPage=new DashboardPage();
        Assert.assertTrue(dashboardPage.welcomeMessage.isDisplayed());
    }

    @DataProvider
    public Object[][] invalidData(){
        Object[][] data={
                {"james","123!","Invalid credentials"},
                {"Admin2","Hum@nhrm123", "Invalid credential"},
                {"Admin","","Password cannot be empty"},
                {"","Hum@nhrm123","Username cannot be empty"}
        };
        return data;
    }
    @Test(dataProvider = "invalidData", groups = "smoke")
    public void invalidLoginErrorMessageValidation(String username, String password, String message){
        LoginPage loginPage=new LoginPage();
        loginPage.login(username, password);

        String actualError=loginPage.errorMessage.getText();
        Assert.assertEquals(actualError, message, "Error message does not match");

    }

}
