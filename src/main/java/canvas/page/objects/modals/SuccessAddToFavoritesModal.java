package canvas.page.objects.modals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

public class SuccessAddToFavoritesModal {
    WebDriver webDriver;

    public SuccessAddToFavoritesModal(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public String getSuccessTitle() {
        WebElement success = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[@class='modal-content success']//h5"));
        return success.getText();
    }

    public SuccessAddToFavoritesModal clickCloseButton() {
        WebElement closeBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='modal-content success']//button"));
        closeBtn.click();
        return this;
    }

    public String getSuccessMessage() {
        WebElement successMessage = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[@class='modal-content success']//div[contains(@class, 'custom-content')]"));
        return successMessage.getText();
    }
}
