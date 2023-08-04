package canvas.page.objects.games;

import org.awaitility.Awaitility;
import org.openqa.selenium.*;
import selenium.SeleniumWaits;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Race2RichesGamesManualPlay {
    WebDriver webDriver;

    public Race2RichesGamesManualPlay(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public List<WebElement> getYourPlaysList() {
        List<WebElement> yourPlaysList = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//h5[text()='Your plays - Max 1']//following-sibling::div//h6[contains(text(), 'Play')]"));
        return yourPlaysList;
    }

    public Race2RichesGamesManualPlay clickToExpandGameOptionDropdownList() {
        WebElement dropdownArrowBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Type']//following-sibling::div//button"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", dropdownArrowBtn);
        return this;
    }

    public String getSelectedOptionValue() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Type']//following-sibling::div//input"));
        return dropdownInput.getAttribute("value");
    }

    public Race2RichesGamesManualPlay selectOptionShow() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Type']//following-sibling::div//input"));
        dropdownInput.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }

    public Race2RichesGamesManualPlay selectOptionExactA() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Type']//following-sibling::div//input"));
        dropdownInput.sendKeys(Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }

    public Race2RichesGamesManualPlay selectOptionExactABox() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Type']//following-sibling::div//input"));
        dropdownInput.sendKeys(Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }

    public Race2RichesGamesManualPlay selectOptionQuinella() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Type']//following-sibling::div//input"));
        dropdownInput.sendKeys(Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }

    public Race2RichesGamesManualPlay selectOptionTrifecta() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Type']//following-sibling::div//input"));
        dropdownInput.sendKeys(Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }

    public Race2RichesGamesManualPlay selectOptionTrifectaBox() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Type']//following-sibling::div//input"));
        dropdownInput.sendKeys(Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }
    public Race2RichesGamesManualPlay selectOptionTrifectaWheel() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Type']//following-sibling::div//input"));
        dropdownInput.sendKeys(Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }
    public Race2RichesGamesManualPlay selectOptionDailyDouble() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Type']//following-sibling::div//input"));
        dropdownInput.sendKeys(Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }

    public Race2RichesGamesManualPlay selectOptionSuperfecta() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Type']//following-sibling::div//input"));
        dropdownInput.sendKeys(Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }

    public Race2RichesGamesManualPlay selectOptionSuperfectaBox() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Type']//following-sibling::div//input"));
        dropdownInput.sendKeys(Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }

    public String getDrawingDates() {
        WebElement drawingDateFrom = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[@class = 'game-play-date']//p[text() = 'From']//following::div[contains(text(), 'AM') or contains(text(), 'PM') ]"));
        WebElement drawingDateTo = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[@class = 'game-play-date']//p[text() = 'To']//following::div[contains(text(), 'AM') or contains(text(), 'PM') ]"));
        return drawingDateFrom.getText().replace(",", "") + "\n" + "-\n" + drawingDateTo.getText().replace(",", "");

    }

    public Race2RichesGamesManualPlay selectOptionSuperfectaWheel() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Type']//following-sibling::div//input"));
        dropdownInput.sendKeys(Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }


    public Race2RichesGamesManualPlay enterNumberToPlay(int index, String numberToPlay) {
        List<WebElement> numberInputFields = SeleniumWaits.elementsToBeClickable(webDriver,
                By.xpath("//div[contains(@class, 'pick-x-board')]//input"));
        WebElement input = numberInputFields.get(index);
        input.sendKeys(numberToPlay);
        return this;
    }

    public Race2RichesGamesManualPlay clickPlayNowButton() {
        WebElement buyNowBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='game-actions']//button[text()='PLAY NOW']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", buyNowBtn);
        return this;
    }

    public Race2RichesGamesManualPlay clickToExpandMultiplierDropdown() {
        WebElement dropdownArrowBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Amount']//following-sibling::div//button"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", dropdownArrowBtn);
        return this;
    }

    public Race2RichesGamesManualPlay selectMultiplierOption() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Amount']//following-sibling::div//input"));
        dropdownInput.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }

    public String getSelectedOptionMultiplierValue() {
        WebElement multiplier = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//label[text()='Play Amount']//following-sibling::div/input"));
        return multiplier.getAttribute("value").replace("$", "");
    }


    public String getBetTotalPrice() {
        WebElement totalPrice = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//h5[text()='Total:']//span[@class='game-total-list-item__value']"));
        return totalPrice.getText().replace("$", "");
    }

    public String getNumberOfRaces() {
        WebElement numOfDraws = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//p[contains(text(), 'No of')]//span[@class='game-total-list-item__value']"));
        return numOfDraws.getText();
    }

    public String getGamesPrice(String betType) {
        WebElement price = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//ul[@class='game-total-list']/li//p[contains(text(), '"+betType+"')]//span[@class='game-total-list-item__value']"));
        return price.getText().replace("$", "");
    }
    public Race2RichesGamesManualPlay waitForLoaderToClose() {
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

    public Race2RichesGamesManualPlay clickQuickPicksButtonUnderNumbersSection() {
        WebElement quickPicksBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//h5[text() = 'Numbers']//following::div//button[text()='Quick Pick']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", quickPicksBtn);
        //quickPicksBtn.click();
        return this;
    }

    public Race2RichesGamesManualPlay clickNumberToSelectHorse(int rowNumber, String numberToSelect) {
        WebElement horseToPlay = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[contains(@class, 'panel__"+rowNumber+"')]//button[text()='"+numberToSelect+"']"));
        //horseToPlay.click();
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", horseToPlay);
        return this;
    }

    public Race2RichesGamesManualPlay clickAddToFavoritesButton() {
        WebElement buyNowBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='game-actions']//button[text()='Add to Favorites']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", buyNowBtn);
        return this;
    }

    public Race2RichesGamesManualPlay setMultiDays(Integer num) {
        WebElement daysRailInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//span[contains(@class, 'rail')]/ancestor::span//input"));
        for (int i = 1; i < num; i++) {
            daysRailInput.sendKeys(Keys.LEFT_CONTROL, Keys.ARROW_RIGHT);
        }

        return this;
    }

    public List<String> getNumbersPlayed() {
        List<WebElement> numbers = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//h5[text()='Your plays - Max 1']//following-sibling::div//div[@spacing='1']//span"));
        return numbers.stream().map(x->x.getText()).collect(Collectors.toList());
    }

    public Race2RichesGamesManualPlay clickAddToCartButton() {
        WebElement addToCartButton = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='game-actions']//button[text()='Add to Cart']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", addToCartButton);
        return this;
    }

    public Race2RichesGamesManualPlay clickBonusToggle() {
        WebElement powerPlayToggle = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//p[contains(text(), 'Bonus')]//preceding-sibling::span/span[contains(@class, 'switchBase')]"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", powerPlayToggle);
        //quickPicksBtn.click();
        return this;
    }

    public String getBonusCost() {
        WebElement numOfDraws = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//p[text()='Bonus:']//span[@class='game-total-list-item__value']/span"));
        return numOfDraws.getText().substring(1);
    }

    public Integer getSelectedHorsesCount() {
        List<WebElement> selectedHorses = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'panel__')]//button[not(@disabled)]"));
        return selectedHorses.size();
    }

    public String getSelectedHorsesNumbers(int index) {
        List<WebElement> selectedHorses = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'panel__')]//button[not(@disabled)]"));
        return selectedHorses.get(index).getText();
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
