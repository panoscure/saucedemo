package canvas.page.objects.modals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

public class DeleteGameFromCartConfirmationModal {

    WebDriver webDriver;

    public DeleteGameFromCartConfirmationModal(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public DeleteGameFromCartConfirmationModal clickButtonInModal(String btnText) {

        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='modal-content-container']//button[text()='"+btnText+"']"));
        btn.click();
        return this;

    }
}
