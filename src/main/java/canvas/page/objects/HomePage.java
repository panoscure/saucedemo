package canvas.page.objects;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class HomePage {

    WebDriver webDriver;

    public HomePage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public HomePage clickLoginButton() {
        WebElement loginBtn = SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//button[text()='Login']"));
        loginBtn.click();
        return this;
    }

    public HomePage clickHeaderLogo() {
        WebElement logo = SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//header//img[@title='Logo']"));
        logo.click();
        return this;
    }

    public HomePage clickUserIcon() {
        Awaitility
                .await()
                .pollInterval(2, TimeUnit.SECONDS)
                .atMost(20, TimeUnit.SECONDS)
                .until(() ->
                {
                    WebElement userIcon = SeleniumWaits.visibilityOfElementLocated(webDriver,
                            By.xpath("//div[contains(@class, 'QuickLottoMyAccountArea')]//button"));
                    JavascriptExecutor js = (JavascriptExecutor) webDriver;
                    js.executeScript("arguments[0].click();", userIcon);
                    WebElement myAccountBtn = SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//p[text()='MY ACCOUNT']"));
                    return myAccountBtn.isDisplayed();
                });
        return this;
    }

    public BigDecimal getBalance() {
        Awaitility
                .await()
                .pollInterval(3, TimeUnit.SECONDS)
                .atMost(8, TimeUnit.SECONDS)
                .until(() ->
                {
                    WebElement balance = SeleniumWaits.visibilityOfElementLocated(webDriver,
                            By.xpath("//p[contains(text(), 'BALANCE')]//following-sibling::p/span"));
                    return balance.isDisplayed();
                });
        WebElement balance = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//p[contains(text(), 'BALANCE')]//following-sibling::p/span"));
        String balanceAsString = balance.getText();
        String balanceWithoutSymbols = balanceAsString.substring(1).replace(",", "");
        BigDecimal balanceAsBigDecimal = new BigDecimal(balanceWithoutSymbols);
        return balanceAsBigDecimal;
    }

    public HomePage clickCloseButtonInErrorModal() {
        WebElement closeButton = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//button[text()='RETRY']"));

        //JavascriptExecutor js = (JavascriptExecutor) webDriver;
        //        js.executeScript("arguments[0].click();", closeButton);
        closeButton.click();
        return this;
    }

    public HomePage clickLinkInTopMenu(String linkName) {
        WebElement linkInTopMenu = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='treeLinkHorizontal']//span[text()='"+linkName+"']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", linkInTopMenu);

        return this;
    }

    public HomePage clickTicketCheckerInTopMenu() {
        WebElement link = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='treeLinkHorizontal']//span/a[text()='TICKET CHECKER']"));
        link.click();
        return this;
    }

    public HomePage clickCartButton(){
        WebElement cartBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='cart-indicator']/button"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", cartBtn);
        return this;
    }

    public String numberOfGameInCart() {
        Awaitility
                .await()
                .pollInterval(1, TimeUnit.SECONDS)
                .atMost(2, TimeUnit.SECONDS)
                .until(() ->
                {
                    WebElement gameInCart = SeleniumWaits.visibilityOfElementLocated(webDriver,
                            By.xpath("//div[@class='cart-indicator']//span[@class='cart-circle']"));
                    return gameInCart.isDisplayed();
                });
        WebElement gamesInCart = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[@class='cart-indicator']//span[@class='cart-circle']"));
        return gamesInCart.getText();
    }


}
