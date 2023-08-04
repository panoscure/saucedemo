package canvas.page.objects;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TicketCheckerPage {

    WebDriver webDriver;

    public TicketCheckerPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    //CHECK MY NUMBERS
    public String getPageTitle() {
        WebElement title = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//h2"));
        return title.getText();
    }

    public TicketCheckerPage enterTicketId(String ticketId) {
        WebElement input = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//form//input[@id='ticketchecker']"));
        input.sendKeys(ticketId);
        return this;
    }

    public TicketCheckerPage clickCheckMyNumbersButton() {
        Awaitility
                .await()
                .pollInterval(2, TimeUnit.SECONDS)
                .atMost(5, TimeUnit.SECONDS)
                .until(() ->
                {
                    WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                            By.xpath("//form//button"));
                    return btn.isEnabled();
                });
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//form//button"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", btn);
        //btn.click();
        return this;
    }

    public List<Integer> getAllDrawsInCarousel(int numOfDraws) {
        List<Integer> participatingDrawsInCarousel = new ArrayList<>();
        for (int i = 0; i < numOfDraws; i++) {
            participatingDrawsInCarousel.add(Integer.parseInt(SeleniumWaits.visibilityOfElementLocated(webDriver,
                    By.xpath("(//div[./div[text()='Drawing Results for']]//span)[2]")).getText()));
            SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//div[@class='arrow-next']")).click();
        }
        return participatingDrawsInCarousel;
    }

    public boolean isArrowRightDisabled() {
        return webDriver.findElements(By.xpath("//div[@class='details-arrow disabled']//div[@class='arrow-next']")).size()>0;
    }

}
