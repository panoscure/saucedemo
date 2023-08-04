package canvas.page.objects.modals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

public class ForgotPasswordModal {

    WebDriver webDriver;

    public ForgotPasswordModal(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public ForgotPasswordModal enterUsername(String username) {
        WebElement inputUsername = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//input[@id='username']"));
        inputUsername.sendKeys(username);
        return this;
    }

    public ForgotPasswordModal clickResetPasswordButton() {
        WebElement resetPasswordBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//button[text()='Reset Password']"));
        resetPasswordBtn.click();
        return this;
    }

    public ForgotPasswordModal clickCloseButton() {
        WebElement closeBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//img[contains(@src, 'close')]"));
        closeBtn.click();
        return this;
    }
}
