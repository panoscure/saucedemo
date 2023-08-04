package canvas.page.objects.account.drawer.preferences.myfavorites;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MyFavoriteNumbers {

    WebDriver webDriver;

    public MyFavoriteNumbers(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public MyFavoriteNumbers enterFavoriteNumber(String favoriteNumber) {
        Awaitility
                .await()
                .pollInterval(1, TimeUnit.SECONDS)
                .atMost(7, TimeUnit.SECONDS)
                .until(() ->
                {
                    WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                            By.xpath("//h5[text()='Add favorite number']//following::button[text()='Add Number']"));
                    return btn.isDisplayed();
                });
        WebElement input = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//h5[text()='Add favorite number']//following::input[@id='number-input']"));
        input.sendKeys(favoriteNumber);
        return this;
    }

    public MyFavoriteNumbers clickAddNumberButton() {
        Awaitility
                .await()
                .pollInterval(1, TimeUnit.SECONDS)
                .atMost(7, TimeUnit.SECONDS)
                .until(() ->
                {
                    WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                            By.xpath("//h5[text()='Add favorite number']//following::button[text()='Add Number']"));
                    return btn.isEnabled();
                });
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//h5[text()='Add favorite number']//following::button[text()='Add Number']"));
        btn.click();
        return this;
    }

    public String getFavoriteNumber() {
        List<WebElement> favoriteNumber = SeleniumWaits.elementsToBeClickable(webDriver,
                By.xpath("//div[@class= 'number']//span"));
        return favoriteNumber.get(0).getText();
    }

    public List<String> getAllFavoriteNumbers() {
        List<WebElement> favoriteNumber = SeleniumWaits.elementsToBeClickable(webDriver,
                By.xpath("//div[@class= 'number']//span"));
        return favoriteNumber.stream().map(x->x.getText()).collect(Collectors.toList());
    }
    public MyFavoriteNumbers deleteFavoriteNumber() {
        WebElement xIcon = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class= 'number']//button"));
        xIcon.click();
        return this;
    }

    public Integer getFavoriteNumbersCount() {
        List<WebElement> favoriteNumbers = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[@class= 'number']"));

        return favoriteNumbers.size();
    }

    public MyFavoriteNumbers clickCloseModalButton() {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='close-button-container']/button"));
        btn.click();
        return this;
    }
}
