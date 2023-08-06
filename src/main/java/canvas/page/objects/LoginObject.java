package canvas.page.objects;

//import canvas.page.objects.account.drawer.AccountDrawer;
//import canvas.page.objects.modals.*;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import selenium.SeleniumWaits;

import java.util.concurrent.TimeUnit;

public class LoginObject {

    WebDriver webDriver;

    public LoginObject(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public LoginObject fillUsername(WebDriver webDriver,String username) {
        String path = "//input[@id='user-name']";
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath(path)));
        WebElement userName = webDriver.findElement(By.xpath(path));
        userName.sendKeys(username);
        return this;
    }

    public LoginObject fillPassword(WebDriver webDriver,String password) {
        String path = "//input[@id='password']";
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath(path)));
        WebElement pass = webDriver.findElement(By.xpath(path));
        pass.sendKeys(password);
        return this;
    }

    public HomePage clickLoginButton(WebDriver webDriver) {
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.
                elementToBeClickable(By.xpath("//input[@id='login-button']")));
        webDriver.findElement((By.xpath("//input[@id='login-button']"))).click();
        return new HomePage(webDriver);
    }
    public String getLoginErrorMessage(WebDriver webDriver) {

        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//*[@id='login_button_container']/div/form/div[3]/h3")));
        WebElement titleText = webDriver.findElement(By.xpath("//*[@id='login_button_container']/div/form/div[3]/h3"));

        String titleTextAsString = titleText.getText();
        return titleTextAsString;
    }

    public String verifyLoginScreen() {

        String path="//*[@id='root']/div/div[1]";
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath(path)));
        WebElement titleText = webDriver.findElement(By.xpath(path));

        String titleTextAsString = titleText.getText();
        return titleTextAsString;
    }



}
