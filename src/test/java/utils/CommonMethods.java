package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CommonMethods {
    public static WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void openBrowser() {

        ConfigReader.readProperties(Constants.CONFIGURATION_FILEPATH);

        switch (ConfigReader.getPropertyValue("browser")) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver=new FirefoxDriver();
                break;
            default:
                throw new RuntimeException("Invalid browser name");
        }

        driver.get(ConfigReader.getPropertyValue("url"));
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Constants.IMPLICIT_WAIT, TimeUnit.SECONDS);
    }

    public static void sendText (WebElement element, String textToSend){
        element.clear();
        element.sendKeys(textToSend);
    }
    public static WebDriverWait getWait(){
        WebDriverWait wait=new WebDriverWait(driver, Constants.EXPLICIT_WAIT);
        return wait;
    }
    public static void waitFotClickability(WebElement element){
        getWait().until(ExpectedConditions.elementToBeClickable(element));
    }
    public static void click (WebElement element){
        waitFotClickability(element);
        element.click();
    }
    public static JavascriptExecutor getJSWExecutor(){
        JavascriptExecutor js=(JavascriptExecutor) driver;
        return js;
    }

    public static void jsClick(WebElement element){
        getJSWExecutor().executeScript("arguments[0].click()",element);
    }
    //to take screenshot
    public static void takeScreenshot(String fileName) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File sourceFile = ts.getScreenshotAs(OutputType.FILE);

        try {
            FileUtils.copyFile(sourceFile, new File(Constants.SCREENSHOT_FILEPATH + fileName + " " +getTimeStamp("YYYY-MM-DD-HH-SS")+ ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getTimeStamp(String pattern){
        Date date=new Date();
        //pattern yyyy-mm-dd-hh-mm-ss-ms
        //to format the date according to out choice we have a function:
        SimpleDateFormat sdf=new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(){
        driver.quit();
    }
}
