package canvas.page.objects.skrill;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

public class SkrillDashboardPage {

    WebDriver webDriver;

    public SkrillDashboardPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public String getDepositAmountFromSkrillWalletDashboard() {
        WebElement amountToDeposit = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[contains(@class, 'amount-info')]//ps-balance//span"));
        return amountToDeposit.getText();
    }


    public SkrillDashboardPage clickPayNowButton() {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//p[contains(@class, 'subtitle')]"));
        btn.click();
        return this;
    }

    public SkrillDashboardPage clickConfirmButton() {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//span[text()='CONFIRM']/ancestor::button"));
        btn.click();
        return this;
    }

}
