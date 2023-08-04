package canvas.page.objects.modals;

import org.openqa.selenium.*;
import selenium.SeleniumWaits;

public class AccountLoginModal {

    WebDriver webDriver;

    public AccountLoginModal(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public String getAccountLoginModalTitle() {
        WebElement title = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[@class='modal-content page']//span[text()='ACCOUNT LOGIN']"));
        return title.getText();
    }

    public AccountLoginModal enterValueInInputField(String id, String value) {
        WebElement inputField = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@id='loginDialog']//form//input[@id='"+id+"']"));
        inputField.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        inputField.sendKeys(value);
        return this;
    }

    public AccountLoginModal clickLoginButton() {
        WebElement loginBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@id='loginDialog']//form//button[text()='Login']"));
        //loginBtn.click();
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", loginBtn);
        return this;
    }

    public AccountLoginModal clickForgotPasswordLink() {
        WebElement forgotPasswordLink = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//span[text()='Forgot Password']/ancestor::a"));
        forgotPasswordLink.click();
        return this;
    }

    public String getInvalidCredentialsErrorMessage() {
        WebElement errorMessage = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//form//div[contains(@class, 'ResponseMessageAutoclose')]//div[contains(@class, 'responseMessage')]"));
        return errorMessage.getText();
    }

    public AccountLoginModal clickRegisterButton() {
        WebElement registerBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//button[text()='Register']"));
        registerBtn.click();
        return this;
    }
}
