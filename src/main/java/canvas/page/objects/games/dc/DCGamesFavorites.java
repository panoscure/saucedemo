package canvas.page.objects.games.dc;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

public class DCGamesFavorites {

    WebDriver webDriver;

    public DCGamesFavorites(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public String getFavoriteGameName() {
        WebElement selectFavoriteDropdown = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//label[text()='Select Favorite']/ancestor::div[contains(@class, 'hasPopupIcon' )]//input[@readonly]"));
        return selectFavoriteDropdown.getAttribute("value");
    }

    public DCGamesFavorites clickUpdateFavoriteButton() {
        WebElement updateFavoriteBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='game-actions']//button[text()='Update Favorite']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", updateFavoriteBtn);
        //updateFavoriteBtn.click();
        return this;
    }

}
