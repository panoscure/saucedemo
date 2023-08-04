package canvas.page.objects;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CartDrawer {

    WebDriver webDriver;

    public CartDrawer(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public String getDrawerTitle() {
        WebElement drawerTitle = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[contains(@class, 'cart-header')]/h3"));
        return drawerTitle.getText();
    }

    public Boolean isGameCorrect(String gameName, Integer index) {
        List<WebElement> gameIcon = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'logo')]//img[contains(@src, '"+gameName+"')]"));
        return gameIcon.get(index).isDisplayed();
    }

    public CartDrawer clickExpandTicketDetailsButton(Integer index) {
        List<WebElement> expandTicketDetailsBtns = SeleniumWaits.elementsToBeClickable(webDriver,
                By.xpath("//button[text()='EXPAND TICKET DETAILS']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", expandTicketDetailsBtns.get(index));
        return this;
    }

    public CartDrawer clickCollapseTicketDetailsButton() {
        WebElement expandTicketDetailsBtns = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//button[text()='Collapse Ticket details']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", expandTicketDetailsBtns);
        return this;
    }

    public String getNumberOfPlaysInCart() {
        WebElement numberOfPlays = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//li[contains(@class, 'cart-ticket-data')]/span[contains(text(), 'Plays')]/following-sibling::span"));
        return numberOfPlays.getText();
    }

    public String getPlayType(Integer index) {
        List<WebElement> types = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//li[contains(@class, 'cart-ticket-data')]/span[contains(text(), 'Type')]/following-sibling::span"));
        return types.get(index).getText();
    }

    public String getPlayPrice() {
        WebElement price = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//span[contains(text(), 'Price')]/following-sibling::div/span"));
        return price.getText();
    }

    public String getPlayTotal() {
        WebElement total = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//span[contains(text(), 'Total')]/following-sibling::span"));
        return total.getText();
    }

    public String getGameInCart(Integer index) {
        Awaitility
                .await()
                .pollInterval(2, TimeUnit.SECONDS)
                .atMost(20, TimeUnit.SECONDS)
                .until(() ->
                {
                    List<WebElement> listOfGamesInTheTicket = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                            By.xpath("//div[@class='game-all-boards']//div[contains(@class, 'elevation')]"));
                    return listOfGamesInTheTicket.size()>=1;
                });
        List<WebElement> listOfGamesInTheTicket = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[@class='game-all-boards']//div[contains(@class, 'elevation')]"));
        return listOfGamesInTheTicket.get(index-1).getText();
    }

    public CartDrawer clickCartTicketButton(String buttonName) {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[contains(@class, 'cart-ticket-action')]//button[text()= '"+buttonName+"']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", btn);
        return this;
    }

    public CartDrawer clickCartButton(String buttonName) {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[contains(@class, 'cart-action')]//button[text()= '"+buttonName+"']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", btn);
        return this;
    }


    public String gameContainsPowerPlay() {
        WebElement powerPlay = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'ticket-options-details')]//p[contains(text(), 'Power Play')]/following-sibling::p"));
        return powerPlay.getText();
    }

    public String gameContainsMegaplier() {
        WebElement powerPlay = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'ticket-options-details')]//p[contains(text(), 'Megaplier')]/following-sibling::p"));
        return powerPlay.getText();
    }

    public String megamillionsContainsJustTheJackPot() {
        WebElement powerPlay = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'ticket-options-details')]//p[contains(text(), 'Just the Jackpot')]/following-sibling::p"));
        return powerPlay.getText();
    }

    public String getEmptyCartText() {
        WebElement text = SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//h3[text()= 'Your cart is empty']"));
        return text.getText();
    }

    public WebElement getTicketIdPlayedInCart(Integer index) {
        List<WebElement> ticketPlayedInCart = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'cart-item')]"));
        return ticketPlayedInCart.get(index);
    }

    public String getThankYouTextInCart() {
        WebElement thankYouText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'cart-item')]//preceding-sibling::div//h5"));
        return thankYouText.getText();
    }

    public String getTicketIdPlayedInCart(int index) {
        List<WebElement> ticketIds = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'cart-item')]//p[contains(text(), 'ID')]/following-sibling::p"));
        return ticketIds.get(index).getText();
    }

    //div[contains(@class, 'coupon-id')]//p[contains(@class, 'id')]

    public String getTicketPlayedMultiDrawsInCart(int index) {
        List<WebElement> ticketMultiDraws = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'cart-item')]//p[contains(text(), 'Multi Drawing')]/ancestor::div[contains(@class, 'multiple-draws')]//following-sibling::div[contains(@class, 'item')]"));
        return ticketMultiDraws.get(index).getText();
    }

    public String getGamedPlayedTotalCostInCart() {
        WebElement totalCost = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'cart-actions')]//h6[contains(text(), 'Cost')]/following-sibling::h6"));
        return totalCost.getText().substring(1);
    }

    public CartDrawer clickButtonInCheckoutCart(String buttonName) {
        WebElement btn = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'checkOut-buttons')]//button[text()='"+buttonName+"']"));
        btn.click();
        return this;
    }

}
