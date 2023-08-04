package canvas.page.objects.games.drawer;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

public class GamesDrawer {

    WebDriver webDriver;

    public GamesDrawer(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public GamesDrawer clickBuyNowButtonInGamesDrawer(String gameName) {
        WebElement buyNowBtnInDrawer = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//li[@class='widgetContainerItem']//h5[text()='"+gameName+"']//ancestor::div[@class='games-list-overview-item']//child::button"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", buyNowBtnInDrawer);
        return this;
    }


}
