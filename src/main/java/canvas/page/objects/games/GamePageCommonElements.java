package canvas.page.objects.games;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

public class GamePageCommonElements {

    WebDriver webDriver;

    public GamePageCommonElements(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public GamePageCommonElements selectGameEntryTypeTab(String tabName) {
        WebElement entryTypeTab = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//h5[text() = '"+tabName+"']//ancestor::button"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", entryTypeTab);
        return this;
    }

}
