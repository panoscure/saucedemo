package canvas.page.objects.paypal;

import common.utils.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

public class PayPalLoginPage {
    WebDriver webDriver;

    public PayPalLoginPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }


    public PayPalLoginPage enterEmail(String email) {
        WebElement emailField = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//input[@id='email']"));

        if (!emailField.getAttribute("value").equalsIgnoreCase("")) {
            emailField.sendKeys(Keys.LEFT_SHIFT, Keys.END, Keys.BACK_SPACE);
            emailField.sendKeys(email);
        }else {
            emailField.sendKeys(email);
        }

        return this;
    }

    public PayPalLoginPage enterPassword(String password) {
        WebElement emailField = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//input[@id='password']"));
        emailField.click();
        //check this
        emailField.sendKeys(password);
        return this;
    }

    public PayPalLoginPage clickNextButton() {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//button[contains(@class, 'actionContinue') and not(contains(@class, 'submit'))]"));
        btn.click();
        return this;
    }

    public PayPalLoginPage clickLoginButton() {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//button[contains(@class, 'submit')]"));
        btn.click();
        return this;
    }
}
