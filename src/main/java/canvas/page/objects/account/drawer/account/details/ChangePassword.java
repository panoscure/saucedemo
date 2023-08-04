package canvas.page.objects.account.drawer.account.details;

import org.openqa.selenium.*;
import selenium.SeleniumWaits;

import java.security.Key;

public class ChangePassword {

    WebDriver webDriver;

    public ChangePassword(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public ChangePassword enterPassword(String name, String password) {
        WebElement input = SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//input[@name='"+name+"']"));
        input.sendKeys(password);
        input.sendKeys(Keys.ENTER);
        return this;
    }

    public ChangePassword
    clickChangePasswordButton() {
        WebElement submitBtn = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//button[text()='CHANGE PASSWORD']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();",submitBtn);
        return this;
    }

    public String getPasswordUpdatedSuccessfullyMessage() {
        WebElement message = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'responseMessage')]//p"));

        return message.getText();
    }

    public ChangePassword clickBackToAccountDetailsButton() {
        WebElement backToAccountDetailsBtn = SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//button[text()='Back to Account details']"));
        backToAccountDetailsBtn.click();
        return this;
    }

}
