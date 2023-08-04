package canvas.page.objects.modals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

import java.util.List;
import java.util.stream.Collectors;

public class ConfirmationModal {

    WebDriver webDriver;

    public ConfirmationModal(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public ConfirmationModal clickButtonInModal(String btnText) {

        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='modal-content confirm']//button[text()='"+btnText+"']"));
        btn.click();
        return this;
    }

    public Boolean isGamePageCorrect(String gameName) {
        WebElement addToCartButton = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'game-card__media')]/img[contains(@src, '"+gameName+"')]"));

        return addToCartButton.isDisplayed();
    }


}
