package canvas.page.objects.modals;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

public class SaveFavoritesModal {

    WebDriver webDriver;

    public SaveFavoritesModal(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public SaveFavoritesModal clickButtonInModal(String buttonName) {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='modal-content dialog']//button[text()='"+buttonName+"']"));
        //btn.click();
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", btn);
        return this;
    }

    public SaveFavoritesModal enterNameForFavoritePlaySlip(String name) {
        WebElement input = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='modal-content dialog']//input"));
        input.sendKeys(name);
        return this;
    }

    public String getModalText() {
        WebElement text = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[@class='modal-content dialog']//h5"));
        return text.getText();
    }
}
