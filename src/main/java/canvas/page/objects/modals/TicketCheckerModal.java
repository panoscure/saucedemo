package canvas.page.objects.modals;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TicketCheckerModal {

    WebDriver webDriver;

    public TicketCheckerModal(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public Boolean isGameCorrect(String game) {
        WebElement gameLogo = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'ticket-checker-dialog-header__logo')]/img[contains(@src, '" + game + "')]"));
        return gameLogo.isDisplayed();

    }

    //TICKET ID, PLAYS, CHANNEL
    public String getTicketDetail(String text) {
        WebElement ticketDetail = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'modal-content-container')]//div[contains(@class, 'ticket-details')]//span[contains(text(), '" + text + "')]/following-sibling::span"));
        return ticketDetail.getText();
    }

    public List<String> getListOfNumbersPlayed() {
        List<WebElement> listOfBetNumbers = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'ticket-details')]//div[contains(@class, 'game-board__panels-primary')]//span"));
        return listOfBetNumbers.stream().map(x -> x.getText()).collect(Collectors.toList());
    }

    public Boolean isLuckySumEnabled() {
        WebElement ls = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'ticket-details')]//div[contains(@class, 'lucky-sum')]/h6"));
        return ls.getText().equalsIgnoreCase("LS");
    }

    public String getLuckySum() {
        WebElement lsCount = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'ticket-details')]//div[contains(@class, 'lucky-sum')]/button"));
        return lsCount.getText();
    }

    public String getFirstGameBetType() {
        List<WebElement> betType = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'ticket-details')]//div[contains(@class, 'selected')]"));
        List<WebElement> standardOrQuickPick = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'ticket-details')]//p[contains(text(), 'Standard') or contains(text(), 'Quick Pick')]"));
        return betType.get(0).getText() + "\n" + standardOrQuickPick.get(0).getText();
    }

    public String getSecondGameBetType() {
        List<WebElement> betType = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'ticket-details')]//div[contains(@class, 'selected')]"));
        List<WebElement> standardOrQuickPick = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'ticket-details')]//p[contains(text(), 'Standard') or contains(text(), 'Quick Pick')]"));
        return betType.get(1).getText() + "\n" + standardOrQuickPick.get(1).getText();
    }

    public Boolean isMegamillionsGamePlusMegaplier() {
        WebElement plusMegaplier = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'common__ticket-game-options')]//p"));
        return plusMegaplier.getText().contains("+Megaplier");
    }

    public Boolean isPowerBallGamePlusPowerPlay() {
        WebElement plusMegaplier = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'common__ticket-game-options')]//p"));
        return plusMegaplier.getText().contains("+Power Play");
    }

    public Boolean isRace2RichesGamePlusBonus() {
        WebElement plusMegaplier = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'common__ticket-game-options')]//p"));
        return plusMegaplier.getText().contains("+Bonus");
    }

    public TicketCheckerModal clickNextArrowToSeeFourthDrawDate() {
        WebElement nextArrow = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[contains(@class, 'ticket-details__participation')]//div[@class='arrow-next']"));
        nextArrow.click();
        return this;
    }

    public String getDrawingDateFrom() {
        WebElement drawDateFrom = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'date-label')]//following-sibling::div/span"));
        return drawDateFrom.getText();
    }

    public String getDrawingDateToFor4Multidraw() {
        for (int i = 0; i <= 2; i++) {
            clickNextArrowToSeeFourthDrawDate();
        }
        WebElement drawDateTo = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'date-label')]//following-sibling::div/span"));
        return drawDateTo.getText();
    }

    public String getDrawingDateToFor20Multidraws() {
        Awaitility
                .await()
                .pollInterval(2, TimeUnit.SECONDS)
                .atMost(20, TimeUnit.SECONDS)
                .until(() ->
                {
                    String drawFromDate = getDrawingDateFrom();
                    return !(drawFromDate.isEmpty());
                });
        for (int i = 0; i <= 19; i++) {
            clickNextArrowToSeeFourthDrawDate();
        }
        WebElement drawDateTo = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'date-label')]//following-sibling::div/span"));
        return drawDateTo.getText();
    }

    public String getDrawingDatesFor4Multidraw() {

        return getDrawingDateFrom() + "\n" + "-\n" + getDrawingDateToFor4Multidraw();
    }

    public String getDrawingDatesFor20Multidraw() {

        return getDrawingDateFrom() + "\n" + "-\n" + getDrawingDateToFor20Multidraws();
    }

    public String getGameCost() {
        WebElement gameDetail = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class,  'cost-summary')]/div[text() = 'Cost']/following-sibling::div/span"));
        return gameDetail.getText().substring(1);
    }

    //Add to Favorites, Add to Cart, BUY NOW
    public TicketCheckerModal clickButton(String buttonName) {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[contains(@class,  'pc-ticket-details__actions')]/button[text() = 'Add to Favorites']"));
        btn.click();
        return this;
    }

    public TicketCheckerModal clickCloseButton() {
        WebElement closeBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='close-button-container']//img[contains(@src, 'close')]"));
        closeBtn.click();
        return this;
    }

    /************** REFACTOR FUNCTIONS IMPLEMENTED AND USED FOR ONE TEST CASE  **************/

    public List<WebElement> getSelectedBoards() {
        List<WebElement> getPlayedBoards = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver, By.xpath("//div[contains(@id,'ticket-details-board')]"));
        return getPlayedBoards;
    }

    public List<String> getPlayedNumberAccordingToBoard(WebElement board) {
        List<WebElement> numbers = board.findElements(By.xpath(".//div[contains(@class, 'game-board__panels-primary')]//span"));
        return numbers.stream().map(x -> x.getText()).collect(Collectors.toList());
    }

    public Boolean isLuckySumEnabledAccordingToBoard(WebElement board) {
        Boolean luckSumEnabled = false;
        List<WebElement> ls = board.findElements(By.xpath(".//div[contains(@class, 'lucky-sum')]/h6"));
        if (ls.size() != 0) {
            luckSumEnabled = true;
        }
        return luckSumEnabled;
    }

    public String getLuckySumAccordingToBoard(WebElement board) {
        WebElement lsCount = board.findElement(By.xpath(".//div[contains(@class, 'lucky-sum')]/button"));
        return lsCount.getText();
    }

    public String getPlayedStyleAccordingToBoard(WebElement board) {
        WebElement playStyle = board.findElement(By.xpath(".//p[text()='Play Type']//following-sibling::p"));
        return playStyle.getText();
    }

}
