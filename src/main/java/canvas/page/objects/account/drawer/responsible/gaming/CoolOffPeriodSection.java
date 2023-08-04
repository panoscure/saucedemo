package canvas.page.objects.account.drawer.responsible.gaming;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import selenium.JavaScriptExecutors;
import selenium.SeleniumWaits;

public class CoolOffPeriodSection {
    WebDriver webDriver;
    By beginCoolOffButtonXPath = By.xpath("//button[text()='Begin Cool off']");

    public CoolOffPeriodSection(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public CoolOffPeriodSection setCoolOffDuration(String coolOffDurationValue) {
        Awaitility.await().until(()->
        {
            SeleniumWaits.visibilityOfElementLocated(webDriver, beginCoolOffButtonXPath).sendKeys(Keys.ARROW_DOWN);
            return webDriver.findElement(beginCoolOffButtonXPath).isDisplayed();
        });
        SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//p[text()='Cool off Duration']/following-sibling::div")).click();
        SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//li[./p[text()='" + coolOffDurationValue + "']]")).click();
        return this;
    }

    public CoolOffPeriodSection clickBeginCoolOffButton() {
        JavaScriptExecutors.clickElement(webDriver, webDriver.findElement(beginCoolOffButtonXPath));
        return this;
    }

    public CoolOffPeriodSection clickBeginCoolOffButtonPrompt() {
        SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//button[text()='Yes, Begin Cool off']")).click();
        return this;
    }

    public CoolOffPeriodSection clickCloseButtonSuccessPopUp() {
        SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//button[text()='Close']")).click();
        return this;
    }

    public String getSuccessModalHeader() {
        return SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//div[@class='modal-header']")).getText();
    }

    public String getSuccessModalBody() {
        return SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//div[@class='modal-body']")).getText();
    }
}
