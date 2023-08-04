package canvas.page.objects.account.drawer.preferences.myfavorites;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyFavorites {

    WebDriver webDriver;

    public MyFavorites(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public Integer numberOfFavoriteGames() {
        List<WebElement> financialTransactions = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'mobile-row')]"));
        return financialTransactions.size();
    }

    public MyFavorites clickTheLatestFavorite() {
        Awaitility
                .await()
                .pollInterval(1, TimeUnit.SECONDS)
                .atMost(5, TimeUnit.SECONDS)
                .until(() ->
                {
                    List<WebElement> favoriteGames = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                            By.xpath("//div[contains(@class, 'mobile-row')]"));
                    return !(favoriteGames.isEmpty());
                });
        List<WebElement> favoriteGames = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'mobile-row')]"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", favoriteGames.get(0));
        return this;
    }

    public Boolean isGameCorrect(String gameName) {
        WebElement gameIcon = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'logo')]//img[contains(@src, '"+gameName+"')]"));
        return gameIcon.isDisplayed();
    }

    public MyFavorites clickMyFavoriteCouponsButton() {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@id='tabs']//button[text() = 'My Favorite Tickets']"));
        btn.click();
        return this;
    }
    public MyFavorites clickMyFavoriteNumbersButton() {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@id='tabs']//button[text() = 'My Favorite Numbers']"));
        btn.click();
        return this;
    }

    public Boolean areGamesSortedByDescendingDate() {
        WebElement dropdownValue = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//label[text() = 'Sort by']/following-sibling::div/input"));
        return dropdownValue.getAttribute("value").equalsIgnoreCase("Date Newest");
    }

    public String getNoFavoritesMessage() {
        WebElement noFavorites = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'no-data-table')]"));
        return noFavorites.getText();
    }

    public String getNumOfFavoriteGames() {
        WebElement numOfFavoriteGames = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'pagination-info')]"));
        return numOfFavoriteGames.getText();
    }

    public MyFavorites clickLoadMoreButton() {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//div[@class = 'paginator']/button"));
       // btn.click();
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", btn);
        return this;
    }

    public Boolean areFavoritesEmpty() {
        WebElement noDataText = SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//div[contains(@class, 'no-data')]"));
        return noDataText.isDisplayed();
    }

}
