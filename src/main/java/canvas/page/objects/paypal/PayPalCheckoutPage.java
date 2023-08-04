package canvas.page.objects.paypal;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

public class PayPalCheckoutPage {

    WebDriver webDriver;

    public PayPalCheckoutPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public PayPalCheckoutPage clickSubmitPaymentButton() {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//button[@id='payment-submit-btn']"));
        btn.click();
        return this;
    }
}
