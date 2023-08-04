package canvas.page.objects.games;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

public class FavoritePlaysTab {

    WebDriver webDriver;

    public FavoritePlaysTab(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public FavoritePlaysTab expandFavoriteCouponsList() {
        WebElement savedFavoriteGames = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='SELECT FAVORITE']//following-sibling::div//input"));
        savedFavoriteGames.click();
        return this;

    }
}
