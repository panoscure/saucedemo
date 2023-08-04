package canvas.page.objects.account.drawer;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.JavaScriptExecutors;
import selenium.SeleniumWaits;

import java.util.concurrent.TimeUnit;

public class AccountDrawer {

    WebDriver webDriver;

    public AccountDrawer(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public AccountDrawer clickLogoutButton() {
        WebElement logoutButton = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//button[text()='Logout']"));
        JavaScriptExecutors.scrollToElement(webDriver, logoutButton);
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", logoutButton);
        //logoutButton.click();
        return this;
    }

    public AccountDrawer clickWithdrawButton() {
        WebElement withdrawBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//button[text()='Withdraw']"));
        withdrawBtn.click();
        return this;
    }

    public AccountDrawer clickDepositButton() {
        WebElement withdrawBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//button[text()='Deposit']"));
        withdrawBtn.click();
        return this;
    }

    public AccountDrawer clickTabInAccountDrawer(String tabName) {
        Awaitility
                .await()
                .pollInterval(2, TimeUnit.SECONDS)
                .atMost(20, TimeUnit.SECONDS)
                .until(() ->
                {
                    WebElement tab = SeleniumWaits.visibilityOfElementLocated(webDriver,
                            By.xpath("//p[text()='"+tabName+"']"));
                    return tab.isDisplayed();
                });
        WebElement tab = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//p[text()='"+tabName+"']//ancestor::div[@role='menuitem']"));
        tab.click();
        return this;
    }

    public AccountDrawer clickBackToMyAccountButton() {
        WebElement backBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//p[text()='MY ACCOUNT']"));
        backBtn.click();
        return this;
    }

    public Boolean isMyAccountButtonDisplayed() {
        WebElement backBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//p[text()='MY ACCOUNT']/ancestor::div[contains(@class, 'back')]"));
        return backBtn.isDisplayed();
    }

    public AccountDrawer clickNextArrowButtonToFindPreferencesTab() {
        WebElement nextArrowBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='treeLinkTabset--links']//span[contains(@class, 'swiper-arrow swiper-next')]/span"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", nextArrowBtn);
        js.executeScript("arguments[0].click();", nextArrowBtn);
        js.executeScript("arguments[0].click();", nextArrowBtn);
        //nextArrowBtn.click();
        return this;
    }

    public AccountDrawer clickPreviousArrowButtonToWalletTab() {
        WebElement nextArrowBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='treeLinkTabset--links']//span[contains(@class, 'swiper-arrow swiper-prev')]/span"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", nextArrowBtn);
        js.executeScript("arguments[0].click();", nextArrowBtn);
        js.executeScript("arguments[0].click();", nextArrowBtn);
        //nextArrowBtn.click();
        return this;
    }

    public AccountDrawer clickPreviousArrowButtonInAccountDrawer() {
        WebElement nextArrowBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='treeLinkTabset--links']//span[contains(@class, 'swiper-arrow swiper-prev')]/span"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", nextArrowBtn);
        return this;
    }

}
