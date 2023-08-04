package canvas.page.objects.modals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteNumbersModal {

    WebDriver webDriver;

    public FavoriteNumbersModal(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public String getFavoriteNumbers() {
        List<WebElement> favoriteNumbers = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'favorite-numbers')]//div[contains(@class, 'number-picker')]//li"));
        return favoriteNumbers.get(0).getText();
    }

    public FavoriteNumbersModal deleteFavoriteNumber() {
        List<WebElement> favoriteNumbersDeleteBtn = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'favorite-numbers')]//button"));
        favoriteNumbersDeleteBtn.get(0).click();
        return this;
    }
}
