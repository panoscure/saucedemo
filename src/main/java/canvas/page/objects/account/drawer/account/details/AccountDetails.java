package canvas.page.objects.account.drawer.account.details;

import org.awaitility.Awaitility;
import org.openqa.selenium.*;
import selenium.JavaScriptExecutors;
import selenium.SeleniumWaits;

import java.util.concurrent.TimeUnit;

public class AccountDetails {
    WebDriver webDriver;

    public AccountDetails(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public String  getTitle() {
        WebElement title = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'drawerTitle')]//h6"));
        return title.getText();
    }

    public String getEmail() {
        return SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("(//li[./span[text()='Email']]/span)[2]")).getText();
    }

    public String getUsername() {
        return SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("(//li[./span[text()='Username']]/span)[2]")).getText();
    }

    public AccountDetails clickChangeAccountDetail(String accountDetailName) {
        WebElement accountDetail = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[text()='CHANGE "+accountDetailName.toUpperCase()+"']"));
        JavaScriptExecutors.scrollToElement(webDriver, accountDetail);
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();",accountDetail);
        //accountDetail.click();
        return this;
    }

    public AccountDetails clickChangePassword() {
        Awaitility
                .await()
                .pollInterval(2, TimeUnit.SECONDS)
                .atMost(20, TimeUnit.SECONDS)
                .until(() ->
                {
                    WebElement accountDetailBtn = SeleniumWaits.elementToBeClickable(webDriver,
                            By.xpath("//div[text()='CHANGE PASSWORD']"));
                    JavascriptExecutor js = (JavascriptExecutor) webDriver;
                    js.executeScript("arguments[0].click();",accountDetailBtn);
                    return getTitle().equalsIgnoreCase("CHANGE PASSWORD");
                });
        return this;
    }

    public AccountDetails clickViewMoreButtonInPersonalDetails() {
        By viewMoreButtonXPath = By.xpath("//div[@class = 'drawer-content']//button[text()='View more']");
        Awaitility.await().until(()->
        {
            SeleniumWaits.visibilityOfElementLocated(webDriver, viewMoreButtonXPath).sendKeys(Keys.ARROW_DOWN);
            return webDriver.findElement(viewMoreButtonXPath).isDisplayed();
        });
        JavaScriptExecutors.clickElementBy(webDriver, viewMoreButtonXPath);
        return this;
    }

    public String getPersonalDetails(String personalDetailName) {
        WebElement addressDetail = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[@class = 'drawer-content']//h4[text()='Personal Details']//following-sibling::ul/li/span[text()='"+personalDetailName+"']/following-sibling::span"));
        return addressDetail.getText();
    }
    public String getAddressDetails(String addressDetailName) {
        WebElement addressDetail = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[@class = 'drawer-content']//h4[text()='My Address']//following-sibling::ul/li/span[text()='"+addressDetailName+"']/following-sibling::span"));
        return addressDetail.getText();
    }
}

