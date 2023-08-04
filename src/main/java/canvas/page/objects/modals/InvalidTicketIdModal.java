package canvas.page.objects.modals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import selenium.SeleniumWaits;

public class InvalidTicketIdModal {

    WebDriver webDriver;

    public InvalidTicketIdModal(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    //Code is not valid
    public String getModalTitle() {
        WebElement title = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[@id='invalidTicketCode_modal' and not(@aria-hidden='true')]//div[contains(@class, 'ticketDetailsInvalid')]/h5"));
        return title.getText();
    }

    //Please enter a valid barcode.
    public String getModalMessage() {
        WebElement message = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[@id='invalidTicketCode_modal' and not(@aria-hidden='true')]//div[contains(@class, 'ticketDetailsInvalid')]/p"));
        return message.getText();
    }

    //Try Again, Cancel
    public InvalidTicketIdModal clickButtonInModal(String btnName) {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@id='invalidTicketCode_modal' and not(@aria-hidden='true')]//button[text()='"+btnName+"']"));
        btn.click();
        return this;
    }

    public InvalidTicketIdModal closeModal() {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@id='invalidTicketCode_modal' and not(@aria-hidden='true')]//button[@aria-label='Close Dialog']"));
        btn.click();
        return this;
    }
}
