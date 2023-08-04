package canvas.page.objects.account.drawer.account.details;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

import java.util.concurrent.TimeUnit;

public class ChangeAddress {

    WebDriver webDriver;

    public ChangeAddress(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    //streetName, streetNumber, postCode, password
    public ChangeAddress enterValueToInputField(String id, String value) {
        WebElement inputField = SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//input[@id='" + id + "']"));
        inputField.sendKeys(Keys.LEFT_CONTROL, Keys.LEFT_SHIFT, Keys.HOME);
        inputField.sendKeys(Keys.BACK_SPACE);
        inputField.sendKeys(value);
        return this;
    }

    //CITY, COUNTRY
    public ChangeAddress selectComboboxOption(String labelName) {
        WebElement combobox = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='" + labelName + "']//following-sibling::div//input[@role='combobox']"));
        if (combobox.getAttribute("value").equals("Wyoming")) {
            combobox.click();
            combobox.sendKeys(Keys.ARROW_UP, Keys.ENTER);
        } else {
            combobox.click();
            combobox.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
        }
        return this;
    }

    //CITY, COUNTRY
    public String getComboboxOption(String labelName) {
        Awaitility
                .await()
                .pollInterval(2, TimeUnit.SECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .until(() ->
                {
                    WebElement combobox = SeleniumWaits.visibilityOfElementLocated(webDriver,
                            By.xpath("//label[text()='" + labelName + "']//following-sibling::div//input[@role='combobox']"));
                    return combobox.isDisplayed();
                });
        WebElement combobox = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//label[text()='" + labelName + "']//following-sibling::div//input[@role='combobox']"));
        return combobox.getAttribute("value");
    }

    public Boolean isCountryFieldDisabled() {
        WebElement countryInputField = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//input[@id='country']"));
        return countryInputField.getAttribute("class").contains("disabled");
    }

    public String getSubmitButtonName() {
        WebElement submitBtn = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[@class='drawer-content']//button[@type='submit']"));
        return submitBtn.getText();
    }

    public ChangeAddress clickSubmitButton() {
        WebElement submitBtn = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[@class='drawer-content']//button[@type='submit']"));
        submitBtn.click();
        return this;
    }
}
