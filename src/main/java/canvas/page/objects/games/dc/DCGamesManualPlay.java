package canvas.page.objects.games.dc;

import org.awaitility.Awaitility;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import selenium.JavaScriptExecutors;
import selenium.SeleniumWaits;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static selenium.JavaScriptExecutors.scrollToElementBy;

public class DCGamesManualPlay {
    WebDriver webDriver;

    public DCGamesManualPlay(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public List<WebElement> getYourPlaysList() {
        List<WebElement> yourPlaysList = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//h5[text()='Your plays - Max 5']//following-sibling::div//h6[contains(text(), 'PLAY')]"));
        return yourPlaysList;
    }

    public List<String> getNumbersPlayed() {
        List<WebElement> numbers = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//h5[text()='Your plays - Max 5']//following-sibling::div//div[@spacing='1']//span"));
        return numbers.stream().map(x -> x.getText()).collect(Collectors.toList());
    }

    public String getDrawingDateFrom() {
        return SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[./p[contains(text(), 'From')]]/following-sibling::div")).getText();
    }

    public String getDrawingDateTo() {
        return SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[./p[contains(text(), 'To')]]/following-sibling::div")).getText();
    }

    public String get4NumbersPlayedPerPlay(Integer playNumber) {
        List<WebElement> numbersPlayedPerPlay = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//h5[text()='Your plays - Max 5']//following-sibling::div//h6[text()='PLAY " + playNumber + "']//ancestor::div[contains(@class, 'client-card client-card--desktop')]//span[not(text()='(Quick Pick)') and not(not(text()))]"));
        return numbersPlayedPerPlay.get(0).getText() + "\n" +
                numbersPlayedPerPlay.get(1).getText() + "\n" +
                numbersPlayedPerPlay.get(2).getText() + "\n" +
                numbersPlayedPerPlay.get(3).getText();
    }

    public String get5NumbersPlayedPerPlay(Integer playNumber) {
        List<WebElement> numbersPlayedPerPlay = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//h5[text()='Your plays - Max 5']//following-sibling::div//h6[text()='PLAY " + playNumber + "']//ancestor::div[contains(@class, 'client-card client-card--desktop')]//span[not(text()='(Quick Pick)') and not(not(text()))]"));
        return numbersPlayedPerPlay.get(0).getText() + "\n" +
                numbersPlayedPerPlay.get(1).getText() + "\n" +
                numbersPlayedPerPlay.get(2).getText() + "\n" +
                numbersPlayedPerPlay.get(3).getText() + "\n" +
                numbersPlayedPerPlay.get(4).getText();
    }

    public String get2NumbersPlayedPerPlay(Integer playNumber) {
        List<WebElement> numbersPlayedPerPlay = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//h5[text()='Your plays - Max 5']//following-sibling::div//h6[text()='PLAY " + playNumber + "']//ancestor::div[contains(@class, 'client-card client-card--desktop')]//span[not(text()='(Quick Pick)') and not(not(text()))]"));
        return
                numbersPlayedPerPlay.get(0).getText() + "\n" +
                        numbersPlayedPerPlay.get(1).getText();
    }

    public String getDC3BackPairNumbers(Integer playNumber) {
        List<WebElement> numbersPlayedPerPlay = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//h5[text()='Your plays - Max 5']//following-sibling::div//h6[text()='PLAY " + playNumber + "']//ancestor::div[contains(@class, 'client-card client-card--desktop')]//span[not(text()='(Quick Pick)') and not(not(text()))]"));
        return
                "-\n" +
                        numbersPlayedPerPlay.get(0).getText() + "\n" +
                        numbersPlayedPerPlay.get(1).getText();
    }

    public String get3NumbersPlayedPerPlay(Integer playNumber) {
        List<WebElement> numbersPlayedPerPlay = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//h5[text()='Your plays - Max 5']//following-sibling::div//h6[text()='PLAY " + playNumber + "']//ancestor::div[contains(@class, 'client-card client-card--desktop')]//span[not(text()='(Quick Pick)') and not(not(text()))]"));
        System.out.println(numbersPlayedPerPlay);
        return
                numbersPlayedPerPlay.get(0).getText() + "\n" + numbersPlayedPerPlay.get(1).getText() + "\n" + numbersPlayedPerPlay.get(2).getText();
    }


    public DCGamesManualPlay clickToExpandGameOptionDropdownList() {
        WebElement dropdownArrowBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Type']//following-sibling::div//button"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", dropdownArrowBtn);
        return this;
    }

    public DCGamesManualPlay clickToExpandMultiplierDropdownList() {
        WebElement dropdownArrowBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Amount']//following-sibling::div//button"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", dropdownArrowBtn);
        return this;
    }


    public String getSelectedOptionValue() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Type']//following-sibling::div//input"));
        return dropdownInput.getAttribute("value");
    }

    public DCGamesManualPlay selectOptionBox() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Type']//following-sibling::div//input"));
        dropdownInput.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }

    public DCGamesManualPlay selectOptionStraightBox() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Type']//following-sibling::div//input"));
        dropdownInput.sendKeys(Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }

    public DCGamesManualPlay selectOptionFrontPairDC3DC5FrontThreeDC4() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Type']//following-sibling::div//input"));
        dropdownInput.sendKeys(Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }

    public DCGamesManualPlay selectOptionBackPairDC3DC5BackThreeDC4() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Type']//following-sibling::div//input"));
        dropdownInput.sendKeys(Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }

    public DCGamesManualPlay selectOptionComboDC3FrontThreeDC5() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Type']//following-sibling::div//input"));
        dropdownInput.sendKeys(Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }

    public DCGamesManualPlay selectOptionBackThreeDC5() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Type']//following-sibling::div//input"));
        dropdownInput.sendKeys(Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }

    public DCGamesManualPlay selectOptionFrontFourDC5() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Type']//following-sibling::div//input"));
        dropdownInput.sendKeys(Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }

    public DCGamesManualPlay selectOptionBackFourDC5() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Type']//following-sibling::div//input"));
        dropdownInput.sendKeys(Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }

    public DCGamesManualPlay enterNumberToPlay(int index, String numberToPlay) {
        List<WebElement> numberInputFields = SeleniumWaits.elementsToBeClickable(webDriver,
                By.xpath("//div[contains(@class, 'pick-x-board')]//input"));
        WebElement input = numberInputFields.get(index);
        input.sendKeys(numberToPlay);
        return this;
    }

    public DCGamesManualPlay enterNumberToPlayCustom(int boardId, List<Integer> numbersToPlay) {
        for (int i = 1; i <= 5; i++) {
            SeleniumWaits.visibilityOfElementLocated(webDriver,
                            By.xpath("(//div[@class='pick-x-board board-" + boardId + " MuiBox-root css-0']//input)[" + i + "]"))
                    .sendKeys(numbersToPlay.get(i-1).toString());
        }
        return this;
    }

    public DCGamesManualPlay clickPlayNowButton() {
        WebElement buyNowBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='game-actions']//button[text()='PLAY NOW']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", buyNowBtn);
        return this;
    }

    public DCGamesManualPlay clickAddToCartButton() {
        WebElement addToCartButton = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='game-actions']//button[text()='Add to Cart']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", addToCartButton);
        return this;
    }

    public DCGamesManualPlay clickUpdateCartButton() {
        WebElement addToCartButton = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='game-actions']//button[text()='Update Cart']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", addToCartButton);
        return this;
    }

    public DCGamesManualPlay clickAddToFavoritesButton() {
        WebElement buyNowBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='game-actions']//button[text()='Add to Favorites']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", buyNowBtn);
        return this;
    }

    public DCGamesManualPlay clickToExpandMultiplierDropdown() {
        WebElement dropdownArrowBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Play Amount']//following-sibling::div//button"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", dropdownArrowBtn);
        return this;
    }

    public DCGamesManualPlay setMultiplierOptionTo$1() {
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

    public BigDecimal getSelectedOptionMultiplierValueAsBigDecimal() {
        WebElement multiplier = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//label[text()='Play Amount']//following-sibling::div/input"));
        return new BigDecimal(multiplier.getAttribute("value").replace("$", ""));
    }

    public DCGamesManualPlay deselectDrawTime(String drawingTime, String gameId) {
        SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//span[./input[@id='" + drawingTime + "-play-game-id-" + gameId + "']]")).click();
        return this;
    }


    public List<WebElement> wagerBuilderFromToDaysOnMultidrawTicket() {
        List<WebElement> fromToDatesContainer = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver, By.xpath("//h5[text()='Drawing(s)']//following-sibling::div/div"));
        return fromToDatesContainer;
    }

    public String getFromOrToDateForAMultidrawTicketOnWagerBuilder(WebElement containerElement) {
        WebElement dateElmt = containerElement.findElement(By.xpath(".//div//following-sibling::div"));
        String date = dateElmt.getText();
        return date;
    }


    public String getBetTotalPrice() {
        WebElement totalPrice = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//li[@class='game-total-list-item game-total-list-item-total']//span[@class = 'game-total-list-item__value']"));
        return totalPrice.getText().replace("$", "");
    }

    public String getNumberOfDays() {
        WebElement numOfDraws = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//p[contains(@class, 'number-of-draws__text')]//span[@class='game-total-list-item__value']"));
        return numOfDraws.getText();
    }

    public String getDCGamesPrice(String betType) {
        WebElement price = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//p[contains(text(), " + betType + ")]//span[@class='game-total-list-item__value']"));
        return price.getText().replace("$", "");
    }

    public DCGamesManualPlay waitForLoaderToClose() {
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

    public DCGamesManualPlay clickQuickPicksButtonUnderNumbersSection() {
        WebElement quickPicksBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//h5[text() = 'Numbers']//following::div//button[text()='Quick Pick']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", quickPicksBtn);
        // quickPicksBtn.click();
        return this;
    }

    public DCGamesManualPlay clickFavoriteNumbersButton() {
        WebElement quickPicksBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//h5[text() = 'Numbers']//following::div//button[text()='Favorite Numbers']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", quickPicksBtn);
        // quickPicksBtn.click();
        return this;
    }

    public DCGamesManualPlay clickAddQuickPlayButton() {
        WebElement addQuickPlay = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//h5[text() = 'Your plays - Max 5']//following::div[contains(@class, 'play-actions')]//button[text()='Quick Picks']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", addQuickPlay);
        //addQuickPlay.click();
        return this;
    }

    public DCGamesManualPlay clickAddPlayButton() {
        WebElement addPlay = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//h5[text() = 'Your plays - Max 5']//following::div[contains(@class, 'play-actions')]//button[text()='Add Play']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", addPlay);
        //addQuickPlay.click();
        return this;
    }

    public DCGamesManualPlay setMultiDaysTo2() {
        WebElement daysRailInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//span[contains(@class, 'rail')]/ancestor::span//input"));
        daysRailInput.sendKeys(Keys.LEFT_CONTROL, Keys.ARROW_RIGHT);
        return this;
    }

    public DCGamesManualPlay setMultiDaysTo3() {
        WebElement daysRailInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//span[contains(@class, 'rail')]/ancestor::span//input"));
        daysRailInput.sendKeys(Keys.LEFT_CONTROL, Keys.ARROW_RIGHT, Keys.ARROW_RIGHT);
        return this;
    }

    public DCGamesManualPlay setMultiDaysTo7() {
        WebElement daysRailInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//span[contains(@class, 'rail')]/ancestor::span//input"));
        daysRailInput.sendKeys(Keys.LEFT_CONTROL, Keys.ARROW_RIGHT, Keys.ARROW_RIGHT, Keys.ARROW_RIGHT, Keys.ARROW_RIGHT, Keys.ARROW_RIGHT, Keys.ARROW_RIGHT);
        return this;
    }

    public DCGamesManualPlay enableLuckySum() {
        WebElement powerPlayToggle = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//p[contains(text(), 'per play')]//preceding-sibling::span/span[contains(@class, 'switchBase')]"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", powerPlayToggle);
        //quickPicksBtn.click();
        return this;
    }


    /************** REFACTOR FUNCTIONS IMPLEMENTED AND USED FOR ONE TEST CASE  **************/

    public DCGamesManualPlay clickPlayStyleSelectBox() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath(" //label[text()='Play Type']//following-sibling::div[contains(@class,'MuiAutocomplete')]//button[contains(@class,'MuiAutocomplete-popupIndicator')]"));
        JavaScriptExecutors.clickElement(webDriver, dropdownInput);
        return this;
    }

    public DCGamesManualPlay selectPlayStyle(String playStyle) {
        SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//ul[contains(@class,'MuiAutocomplete-listbox')]"));
        WebElement selectionToClick = SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//ul[contains(@class,'MuiAutocomplete-listbox')]//li//p[text()='" + playStyle + "']"));
        JavaScriptExecutors.clickElement(webDriver, selectionToClick);
        return this;
    }

    public DCGamesManualPlay clickMultiplierSelectBox() {
        WebElement dropdownInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath(" //label[text()='Play Amount']//following-sibling::div[contains(@class,'MuiAutocomplete')]//button[contains(@class,'MuiAutocomplete-popupIndicator')]"));
        JavaScriptExecutors.clickElement(webDriver, dropdownInput);
        return this;
    }

    public DCGamesManualPlay selectMultiplier(String multiplier) {
        SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//ul[contains(@class,'MuiAutocomplete-listbox')]"));
        WebElement selectionToClick = SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//ul[contains(@class,'MuiAutocomplete-listbox')]//li//p[text()='" + multiplier + "']"));
        JavaScriptExecutors.clickElement(webDriver, selectionToClick);
        return this;
    }

    public DCGamesManualPlay selectAdvancePlay(String dayOfWeek) {
        scrollToElementBy(webDriver, By.xpath("//h5[text()='Advance Play']"));
        WebElement advancePlaySelection = SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//span[text()='" + dayOfWeek + "']"));
        advancePlaySelection.click();
        return this;
    }

    public WebElement multiDrawSwiperElement() {
        WebElement multiDrawSwiperElmt = SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//span[contains(@class, 'rail')]/ancestor::span//input"));
        return multiDrawSwiperElmt;
    }

    public String getCurrentMultiDrawsSelection() {
        String drawsSelected = SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//div[contains(@class, 'common-slider')]//input//following-sibling::span//span//span")).getText();
        return drawsSelected;
    }

    public DCGamesManualPlay swipeRightUntilMultiDrawsAre(String draws, int xOffset) {
        String multiDrawSelection = "0";
        multiDrawSelection = getCurrentMultiDrawsSelection();
        //get the Number Of Days element x coordinate to act as x.coordinate reference for our drag n drop
        Integer currentDraw = 1;
        do {
            //need a breaking condition
            if (Integer.valueOf(multiDrawSelection) == Integer.valueOf(draws)) {
                break;
            }
            WebElement swiperMultiDraw = multiDrawSwiperElement();

            Point point = swiperMultiDraw.getLocation();
            int xCoordinate = point.getX();
            int yCoordinate = point.getY();

            Actions action = new Actions(webDriver);
            action.dragAndDropBy(swiperMultiDraw, xCoordinate + xOffset, yCoordinate).release().perform();

            currentDraw = currentDraw + 1;
            //we need to add awaitility here to check for the draw change on the slider as it has been noticed that event if the slider reaches the desired draw, it continues to add one more slide.
            Integer finalCurrentDraw = currentDraw;
            Awaitility.await().pollInterval(1, TimeUnit.SECONDS).atMost(60, TimeUnit.SECONDS).until(() ->
            {
                Integer getSliderSelection = Integer.valueOf(getCurrentMultiDrawsSelection());
                return getSliderSelection.equals(finalCurrentDraw);
            });
            //we need to add awaitility here to check for the draw change on the slider as it has been noticed that event if the slider reaches the desired draw, it continues to add one more slide.

            multiDrawSelection = getCurrentMultiDrawsSelection();
        } while (!draws.equals(multiDrawSelection));
        return this;
    }

}
