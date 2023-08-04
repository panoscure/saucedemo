package canvas.page.objects.account.drawer.account.details;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

import java.util.concurrent.TimeUnit;

public class ChangeAccountDetails {
    WebDriver webDriver;

    public ChangeAccountDetails(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public ChangeAccountDetails enterInputValue(String inputId, String text) {
        WebElement input = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//input[@id='" + inputId + "']"));
        input.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
//        input.sendKeys(text, Keys.TAB, Keys.ENTER);
        input.sendKeys(text, Keys.TAB);
        return this;
    }

    public String getUsernameUpdatedValue(String inputId, String usernameValue) {

        Awaitility
                .await()
                .pollInterval(2, TimeUnit.SECONDS)
                .atMost(20, TimeUnit.SECONDS)
                .until(() ->
                {
                    WebElement input = SeleniumWaits.visibilityOfElementLocated(webDriver,
                            By.xpath("//input[@id='" + inputId + "']"));
                    return input.getAttribute("value").equalsIgnoreCase(usernameValue);
                });
        WebElement input = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//input[@id='" + inputId + "']"));
        return input.getAttribute("value");
    }


    public String getInputFieldValidationMessage(String inputId) {
        WebElement inputErrorMessage = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//input[@id='" + inputId + "']/ancestor::div[contains(@class, 'Input')]/following-sibling::p//span[contains(@class, 'body')]"));
        return inputErrorMessage.getText();
    }

    public ChangeAccountDetails clickChangeAccountDetailButton() {
        Awaitility.await().pollInterval(1, TimeUnit.SECONDS).atMost(1, TimeUnit.MINUTES).until(() -> {
            SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//div[@class='drawer-content']//button[@type='submit']")).click();
            return SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                    By.xpath("//p[text()='Your username has been successfully changed!']")).size() > 0;
        });
        return this;
    }

    public String getSuccessAccountDetailUpdated() {
        return SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'responseMessage')]//p")).getText();
    }

    public String getSuccessVerificationCodeSentMessage() {
        return SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//h3[contains(text(),'Verification')]")).getText();
    }

    public ChangeAccountDetails clickBackToAccountDetailsButton() {
        WebElement backToAccountDetailsBtn = SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//button[text()='Back to Account details']"));
        backToAccountDetailsBtn.click();
        return this;
    }

    public ChangeAccountDetails clickChangeEmailButton() {
        Awaitility.await().until(()->
        {
            SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//button[text()='Change Email']")).click();
            return webDriver.findElements(
                    By.xpath("//h3[contains(text(),'Verification')]")).size()>0;
        });
        return this;
    }

    public ChangeAccountDetails clickContinueButton() {
        WebElement continueBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//button[text()='Continue']"));
        continueBtn.click();
        return this;
    }

    public ChangeAccountDetails clickChangeMobileButton() {
        SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//button[text()='Change Mobile']")).click();
        return this;
    }

    public ChangeAccountDetails clickChangeHomeNumberButton() {
        SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//button[text()='Change Home number']")).click();
        return this;
    }
}
