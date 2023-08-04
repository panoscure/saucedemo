package canvas.page.objects.modals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

public class PlayerDoesNotExistErrorModal {

    WebDriver webDriver;

    public PlayerDoesNotExistErrorModal(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public String getErrorMessage() {
        WebElement errorMessage = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[@class='modal-content error']//td[text()='Message']/following-sibling::td/pre"));
        return errorMessage.getText();
    }

    public PlayerDoesNotExistErrorModal clickCloseButton() {
        WebElement closeBtn = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[@class='modal-content error']//button[text()='Close']"));
        closeBtn.click();
        return this;
    }
}
