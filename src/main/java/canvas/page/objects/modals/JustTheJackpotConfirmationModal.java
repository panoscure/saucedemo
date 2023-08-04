package canvas.page.objects.modals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

public class JustTheJackpotConfirmationModal {

    WebDriver webDriver;

    public JustTheJackpotConfirmationModal(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public JustTheJackpotConfirmationModal clickButtonInModal(String btnText) {

        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='modal-content-container']//button[text()='"+btnText+"']"));
        btn.click();
        return this;

    }
}
