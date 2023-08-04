package canvas.page.objects.account.drawer.wallet.gaminghistory;

import canvas.page.objects.account.drawer.wallet.FinancialHistorySection;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GamingHistory {

    WebDriver webDriver;

    public GamingHistory(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public GamingHistory clickTheLatestFinancialTransaction() {
        List<WebElement> financialTransactions = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'table-row')]"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", financialTransactions.get(0));
        return this;
    }

    public GamingHistory clickTheSecondFinancialTransaction() {
        List<WebElement> financialTransactions = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'table-row')]"));
        //financialTransactions.get(1).click();
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", financialTransactions.get(1));
        return this;
    }

    public Boolean isGameCorrect(String gameName) {
        WebElement gameIcon = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'logo')]//img[contains(@src, '"+gameName+"')]"));
        return gameIcon.isDisplayed();
    }

    public GamingHistory clickLoadMoreButton() {
        WebElement numberOfTicketsShown = SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//div[contains(@class, 'gamingHistory')]"));
        Awaitility
                .await()
                .pollInterval(2, TimeUnit.SECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .until(() ->
                {
                    WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                            By.xpath("//div[@class='drawer-content']//div[@class='paginator']//button[contains(text(), 'more')]"));
                    JavascriptExecutor js = (JavascriptExecutor) webDriver;
                    js.executeScript("arguments[0].click();", btn);
                    return numberOfTicketsShown.getText().contains("Showing 1 to 20 of ");
                });
        return this;
    }

    public GamingHistory isLostTicket() {
        List<WebElement> gamesInGamingHistory = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'table-row')]"));
        for (int i = 0; i < gamesInGamingHistory.size(); i++) {

            if (gamesInGamingHistory.get(i).findElement( By.xpath(".//div[text()='NOT-WINNING']")).isDisplayed()) {
                System.out.println(gamesInGamingHistory.get(i).findElement( By.xpath(".//div[text()='NOT-WINNING']")).isDisplayed());
                break;

            }
        }
        return this;
    }

    public String getDrawingDate(Integer ticketNum) {
        List<WebElement> date = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'table-row')]//div[@class='info']"));

        return date.get(ticketNum).toString();
    }

    public GamingHistory clickShowButton(Integer ticketNum) {
        Awaitility
                .await()
                .pollInterval(10, TimeUnit.SECONDS)
                .atMost(5, TimeUnit.MINUTES)
                .until(() ->
                {
                    WebElement showBtn = SeleniumWaits.elementToBeClickable(webDriver,
                            By.xpath("//button[text()='Show']"));
                    JavascriptExecutor js = (JavascriptExecutor) webDriver;
                    js.executeScript("arguments[0].click();", showBtn);
                    Date date = new Date();
                    SimpleDateFormat formatTime = new SimpleDateFormat("MM/dd/yyyy");
                    String today = formatTime.format(date);

                    return getTicketInfo(ticketNum).get(0).equalsIgnoreCase("WIN");
                });

        return this;
    }

    public List<String> getTicketInfo(Integer ticketNum) {


        List<WebElement> info = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'table-row')]//div[contains(@class, 'info') and not(@class='info')]/div"));
        return Arrays.asList(info.get(ticketNum).getText().split("\n"));
    }

    public GamingHistory clickTicketInGamingHistory(Integer ticketIndex) {
        List<WebElement> financialTransactions = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'table-row')]"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", financialTransactions.get(ticketIndex));
        return this;
    }


}
