package canvas.page.objects.skrill;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

public class SkrillLoginPage {

    WebDriver webDriver;

    public SkrillLoginPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public SkrillLoginPage clickRejectAllCookiesButton() {
        WebElement rejectAllBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//button[contains(@id, 'reject-all')]"));
        rejectAllBtn.click();
        return this;
    }

    public SkrillLoginPage enterEmail(String email) {
        WebElement emailField = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//input[@formcontrolname='username']"));
        emailField.sendKeys(email);
        return this;
    }

    public SkrillLoginPage enterPassword(String password) {
        WebElement emailField = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//input[@formcontrolname='password']"));
        emailField.sendKeys(password);
        return this;
    }

    public SkrillLoginPage clickLoginButton() {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//button[@type='submit']"));
        btn.click();
        return this;
    }

}
