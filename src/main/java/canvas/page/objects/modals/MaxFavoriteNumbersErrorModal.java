package canvas.page.objects.modals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

public class MaxFavoriteNumbersErrorModal {

    WebDriver webDriver;

    public MaxFavoriteNumbersErrorModal(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public String getModalHeader() {
        WebElement header = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[@class='modal-content-container']//h5"));
        return header.getText();
    }

    public String getModalMessage() {
        WebElement header = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[@class='modal-content-container']//div[@class='modal-content-body']/div/div[contains(text(), '30 exceeded')]"));
        return header.getText();
    }
}
