package canvas.page.objects.modals;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

import java.util.List;
import java.util.stream.Collectors;

public class ThankYouForPlayingModal {

    WebDriver webDriver;

    public ThankYouForPlayingModal(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public String getModalTitle() {
        WebElement modalTitle = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'modal')]//h3"));
        return modalTitle.getText();
    }


    public String getTicketId() {
        WebElement gameId = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'modal')]//div[contains(@class, 'coupon-id')]//p[contains(@class, 'id')]"));
        return gameId.getText();
    }

    public String getPlayedBetType() {
        WebElement betType = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'pickx__ticket-details')]"));
        return betType.getText();
    }

    public String getDrawingDates() {
        WebElement drawingDates = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//p[contains(text(), 'Drawing Dates')]//ancestor::div[contains(@class, 'common__draw-date')]//div[contains(@class, 'noWrap') and not(contains(text(), '-'))]"));
        return drawingDates.getText().replace(",", "");
    }

    public List<String> getListOfNumbersPlayed() {
        List<WebElement> numbers = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'ticket-details')]//div[contains(@class, 'game-board__panels-primary')]//span"));
        return numbers.stream().map(x -> x.getText()).collect(Collectors.toList());
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

    public String getPlayTypeOfBoard(int boardId) {
        return SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[@id='ticket-details-board-"+boardId+"']//p[text()='Play Type']/following-sibling::p")).getText();
    }

    public String gameContainsMegaplier() {
        WebElement megaplier = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'ticket-options-details')]//p[contains(text(), 'Megaplier')]/following-sibling::p"));
        return megaplier.getText();
    }

    public String gameContainsPowerPlay() {
        WebElement megaplier = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'ticket-options-details')]//p[contains(text(), 'Power Play')]/following-sibling::p"));
        return megaplier.getText();
    }

    public String megamillionsContainsJustTheJackPot() {
        WebElement powerPlay = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'ticket-options-details')]//p[contains(text(), 'Just the Jackpot')]/following-sibling::p"));
        return powerPlay.getText();
    }

    public String getMultiDraws() {
        WebElement ticketMultiDraws = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//p[contains(text(), 'Multi')]/ancestor::div[contains(@class, 'multiple-draws')]//following-sibling::div[contains(@class, 'item')]"));
        return ticketMultiDraws.getText();
    }

    public String getDrawingDateFrom() {
        String drawingDateFullTextInModal = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("(//div[./p[contains(text(), 'Drawing')]]/following-sibling::div)[2]")).getText();
        String drawingDateTextFromInModal = drawingDateFullTextInModal.substring(0, drawingDateFullTextInModal.indexOf("-")-1);
        return drawingDateTextFromInModal;
    }

    public String getDrawingDateTo() {
        String drawingDateFullTextInModal = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("(//div[./p[contains(text(), 'Drawing')]]/following-sibling::div)[2]")).getText();
        String drawingDateTextToInModal = drawingDateFullTextInModal.substring(drawingDateFullTextInModal.indexOf("-")+2);
        return drawingDateTextToInModal;
    }

    public String getRaceDateFrom() {
        String drawingDateFullTextInModal = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("(//div[./p[contains(text(), 'Race Dates')]]/following-sibling::div)[1]")).getText();
        String drawingDateTextFromInModal = drawingDateFullTextInModal.substring(0, drawingDateFullTextInModal.indexOf("-")-1);
        return drawingDateTextFromInModal;
    }

    public String getRaceDateTo() {
        String drawingDateFullTextInModal = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("(//div[./p[contains(text(), 'Race Dates')]]/following-sibling::div)[1]")).getText();
        String drawingDateTextToInModal = drawingDateFullTextInModal.substring(drawingDateFullTextInModal.indexOf("-")+2);
        return drawingDateTextToInModal;
    }

    public ThankYouForPlayingModal clickOkButton() {
        WebElement okBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[contains(@class, 'modal')]//button"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", okBtn);
        return this;
    }


    public String getMultiRaces() {
        WebElement ticketMultiDraws = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//p[contains(text(), 'Multi')]/ancestor::div[contains(@class, 'multiple-draws')]//following-sibling::div[contains(@class, 'item')]"));
        return ticketMultiDraws.getText();
    }

    public Boolean isGameWithBonus() {
        WebElement bonus = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[contains(@class, 'common__ticket-options-details')]//p[text()='Bonus']//following-sibling::p"));
        return bonus.getText().equalsIgnoreCase("YES");
    }

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

    public String getMultiDrawsOnMultidrawTicket() {
        WebElement elementsContainingDrawing = SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//div[@class='modal-body']//p[contains(text(), 'Drawings')]/ancestor::div[1]//following-sibling::div"));
        return elementsContainingDrawing.getText();
    }

    public String getFromAndToDrawingDateOnMultidrawTicket() {
        WebElement toDrawingDateElmt = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//p[contains(text(), 'Drawing Dates')]//ancestor::div[contains(@class, 'common__draw-date')]//div[contains(@class, 'noWrap') and not(contains(text(), '-'))]"));
        String toDrawingDate = toDrawingDateElmt.getAttribute("innerText").replaceAll("\n", "");
        ;
        String[] datesSpliter = toDrawingDate.trim().split("-");
        String fromDate = datesSpliter[0];
        String toDate = datesSpliter[1];
        String finalDatesOutput = fromDate + " - " + toDate;
        return finalDatesOutput;
    }
}
