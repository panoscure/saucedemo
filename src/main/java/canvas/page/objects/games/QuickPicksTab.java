package canvas.page.objects.games;

import canvas.page.objects.games.dc.DCGamesManualPlay;
import org.openqa.selenium.*;
import selenium.SeleniumWaits;

import java.util.List;

public class QuickPicksTab {

    WebDriver webDriver;

    public QuickPicksTab(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public List<WebElement> quickPicksButton(String betAmount) {
        List<WebElement> quickPicksButtons = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//p[contains(text(), '"+betAmount+"')]//ancestor::button"));
        //JavascriptExecutor js = (JavascriptExecutor) webDriver;
        //        js.executeScript("arguments[0].click();", btn);
        return quickPicksButtons;
    }

    public String get5NumbersPlayedPerPlay(Integer playNumber) {
        List<WebElement> numbersPlayedPerPlay = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//h5[text()='Your plays - Max 5']//following-sibling::div//h6[text()='PLAY "+playNumber+"']//ancestor::div[contains(@class, 'client-card client-card--desktop')]//span[not(text()='(Quick Pick)') and not(not(text()))]"));
        return numbersPlayedPerPlay.get(0).getText() + "\n" +
                numbersPlayedPerPlay.get(1).getText() + "\n" +
                numbersPlayedPerPlay.get(2).getText() + "\n" +
                numbersPlayedPerPlay.get(3).getText() + "\n" +
                numbersPlayedPerPlay.get(4).getText() ;
    }

    public QuickPicksTab setMultiDaysTo7() {
        WebElement daysRailInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//span[contains(@class, 'rail')]/ancestor::span//input"));
        daysRailInput.sendKeys(Keys.LEFT_CONTROL, Keys.ARROW_RIGHT, Keys.ARROW_RIGHT, Keys.ARROW_RIGHT, Keys.ARROW_RIGHT, Keys.ARROW_RIGHT, Keys.ARROW_RIGHT);
        return this;
    }

}
