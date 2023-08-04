package canvas.page.objects.modals;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

public class NumberOfFavoriteGamesOverTheMaxErrorModal {

    WebDriver webDriver;

    public NumberOfFavoriteGamesOverTheMaxErrorModal(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public String getErrorMessage() {
        WebElement errorMessage = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[@class='modal-content error']//div[contains(@class, 'content')]//div[contains(text(), 'limit')]"));
        return errorMessage.getText();
    }

    public NumberOfFavoriteGamesOverTheMaxErrorModal clickCloseButton() {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='modal-content error']//button"));
        //btn.click();
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", btn);
        return this;
    }
}
