package canvas.page.objects.games;

import canvas.page.objects.games.dc.DCGamesManualPlay;
import org.awaitility.Awaitility;
import org.openqa.selenium.*;
import selenium.SeleniumWaits;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class LuckyForLifeGamesManualPlay {
    WebDriver webDriver;

    public LuckyForLifeGamesManualPlay(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public Boolean isGamePageCorrect(String gameName) {
        WebElement addToCartButton = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'game-card__media')]/img[contains(@src, '"+gameName+"')]"));

        return addToCartButton.isDisplayed();
    }

    public LuckyForLifeGamesManualPlay selectFiveNumbersToPlay(String firstNumberToPlay) {
        WebElement number = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[contains(@class, 'panel__0')]//button[text()='"+firstNumberToPlay+"']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", number);
        return this;
    }

    public LuckyForLifeGamesManualPlay selectOneNumberToPlay(String numberToPlay) {
        WebElement number = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[contains(@class, 'panel__1')]//button[text()='"+numberToPlay+"']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", number);
        return this;
    }

    public List<WebElement> getYourPlaysList() {
        List<WebElement> yourPlaysList = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//h5[text()='Your plays - Max 5']//following-sibling::div//h6[contains(text(), 'PLAY')]"));
        return yourPlaysList;
    }

    public LuckyForLifeGamesManualPlay setMultiDaysTo20() {
        WebElement daysRailInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//span[contains(@class, 'rail')]/ancestor::span//input"));
        for (int i = 0; i <= 10; i++) {
            daysRailInput.sendKeys(Keys.LEFT_CONTROL, Keys.ARROW_RIGHT);

        }
        return this;
    }

    public String getDrawingDates() {
        WebElement drawingDateFrom = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[@class = 'game-play-date']//p[text() = 'From']//following::div[contains(text(), 'AM') or contains(text(), 'PM') ]"));
        WebElement drawingDateTo = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[@class = 'game-play-date']//p[text() = 'To']//following::div[contains(text(), 'AM') or contains(text(), 'PM') ]"));
        return drawingDateFrom.getText().replace(",", "") + "\n" + "-\n" + drawingDateTo.getText().replace(",", "");

    }


    public LuckyForLifeGamesManualPlay clickPlayNowButton() {
        WebElement buyNowBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='game-actions']//button[text()='PLAY NOW']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", buyNowBtn);
        return this;
    }



    public String getBetTotalPrice() {
        WebElement totalPrice = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//h5[text()='Total:']//span[@class='game-total-list-item__value']"));
        return totalPrice.getText().replace("$", "");
    }

    public String getNumberOfDraws() {
        WebElement numOfDraws = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//p[contains(text(), 'No of')]/span[@class='game-total-list-item__value']"));
        return numOfDraws.getText();
    }

    public LuckyForLifeGamesManualPlay clickAddPlayButton() {
        WebElement addPlay = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//h5[text() = 'Your plays - Max 5']//following::div[contains(@class, 'play-actions')]//button[text()='Add Play']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", addPlay);
        //addQuickPlay.click();
        return this;
    }

    public String getGamesPrice(String betType) {
        WebElement price = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//p[text()='"+betType+"']//span[@class='game-total-list-item__value']"));
        return price.getText().replace("$", "");
    }

    public LuckyForLifeGamesManualPlay waitForLoaderToClose() {
        Awaitility
                .await()
                .pollInterval(2, TimeUnit.SECONDS)
                .atMost(20, TimeUnit.SECONDS)
                .until(() ->
                {
                    Boolean loader = SeleniumWaits.invisibilityOfElementLocated(webDriver,
                            By.xpath("//div[@class='loader']"));
                    return loader;
                });
        return this;
    }

    public LuckyForLifeGamesManualPlay clickQuickPicksButtonUnderNumbersSection() {
        WebElement quickPicksBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//h5[text() = 'Numbers']//following::div//button[text()='Quick Pick']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", quickPicksBtn);
        //quickPicksBtn.click();
        return this;
    }

    public LuckyForLifeGamesManualPlay clickAddToFavoritesButton() {
        WebElement buyNowBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='game-actions']//button[text()='Add to Favorites']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", buyNowBtn);
        return this;
    }

    public LuckyForLifeGamesManualPlay setMultiDaysTo2() {
        WebElement daysRailInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//span[contains(@class, 'rail')]/ancestor::span//input"));
        daysRailInput.sendKeys(Keys.LEFT_CONTROL, Keys.ARROW_RIGHT);
        return this;
    }

    public LuckyForLifeGamesManualPlay clickAddToCartButton() {
        WebElement addToCartButton = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='game-actions']//button[text()='Add to Cart']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", addToCartButton);
        return this;
    }

    public String getDrawingDateFrom() {
        return SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[./p[contains(text(), 'From')]]/following-sibling::div")).getText();
    }

    public String getDrawingDateTo() {
        return SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[./p[contains(text(), 'To')]]/following-sibling::div")).getText();
    }

}
