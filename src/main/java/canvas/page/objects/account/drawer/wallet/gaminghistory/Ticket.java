package canvas.page.objects.account.drawer.wallet.gaminghistory;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Ticket {
    WebDriver webDriver;

    public Ticket(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public String getTicketId() {
        Awaitility
                .await()
                .pollInterval(2, TimeUnit.SECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .until(() ->
                {
                    WebElement ticketId = SeleniumWaits.visibilityOfElementLocated(webDriver,
                            By.xpath("//div[@class='ticket-id']//span"));
                    return !(ticketId.getText().isEmpty());
                });
        WebElement ticketId = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[@class='ticket-id']//span"));
        return ticketId.getText();
    }

    public List<String> getListOfDCGamesInTheTicket() {
        List<WebElement> listOfGamesInTheTicket = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'pickx__ticket-details')]//div[contains(@class, 'elevation')]"));
        return listOfGamesInTheTicket.stream().map(x->x.getText()).collect(Collectors.toList());
    }

    public List<String> getListOf5Plus1GamesInTheTicket() {
        List<WebElement> listOfGamesInTheTicket = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'lotto__ticket-details')]//div[contains(@class, 'elevation')]"));
        return listOfGamesInTheTicket.stream().map(x->x.getText()).collect(Collectors.toList());
    }

    public List<String> getListOfRaceToRichesGamesInTheTicket() {
        List<WebElement> listOfGamesInTheTicket = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'lotto__ticket-details')]"));
        return listOfGamesInTheTicket.stream().map(x->x.getText()).collect(Collectors.toList());
    }

    public String getTicketChannel() {
        List<WebElement> channel = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//p[contains(text(), 'Channel')]/ancestor::div/following-sibling::div"));
        return channel.get(0).getText();
    }

    public String getTicketType() {
        List<WebElement> type = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'gaming-history__ticket-details')]//p[contains(text(), 'Type')]/ancestor::div/following-sibling::div"));
        return type.get(0).getText();
    }

    public String getNumberOfBoardsInGame() {
        List<WebElement> boards = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//p[contains(text(), 'Plays')]/ancestor::div/following-sibling::div"));
        return boards.get(0).getText();
    }

    public String getDrawingId() {
        List<WebElement> drawings = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//p[contains(text(), 'DRAWING')]/ancestor::div/following-sibling::div"));
        return drawings.get(0).getText();
    }

    public String getWinnings() {
        List<WebElement> winnings = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//p[contains(text(), 'WINNINGS')]/ancestor::div/following-sibling::div"));
        return winnings.get(0).getText().substring(1);
    }

    public String getTicketStatus() {
        List<WebElement> channel = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//p[contains(text(), 'STATUS')]/ancestor::div/following-sibling::div"));
        return channel.get(0).getText();
    }

    public Boolean isMegaMillionsJustTheJackpotGame() {
        WebElement justTheJackpot = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'ticket-game-options')]//p[contains(text(), 'Just the Jackpot')]"));
        return justTheJackpot.getText().equalsIgnoreCase("Just the Jackpot");
    }

    public String getTicketCost() {
        WebElement ticketCost = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'cost-summary')]/div[text()='Cost']/following-sibling::div/span"));
        return ticketCost.getText();
    }

    public String getTicketWinnings() {
        WebElement ticketWinnings = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'win-summary')]/div[text()='WINNINGS']/following-sibling::div"));
        return ticketWinnings.getText().substring(1);
    }

    public Ticket clickReturnToListButton() {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//span[text()='Return to list']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", btn);
        //btn.click();
        return this;
    }

    public String getWinningsText(String gameId) {
        WebElement text = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'ticket-details__participation')]//div[contains(@class, 'details-history-date game-"+gameId+"')]"));
        return text.getText();
    }

    public String getWinningNumbers() {
        WebElement numbers = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'ticket-details__participation__winning-numbers')]"));
        return numbers.getText();
    }

    public String getResponseMessageForWinningTicket() {
        //CLAIM YOUR WINNINGS AT DC LOTTERY!
        WebElement message = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[(@class='responseMessage--body')]"));
        return message.getText();
    }
}
