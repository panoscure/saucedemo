package canvas.page.objects.account.drawer;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.JavaScriptExecutors;
import selenium.SeleniumWaits;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class WithdrawSection {

    WebDriver webDriver;

    public WithdrawSection(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WithdrawSection clickToSelectPaymentForTheWithdraw(String paymentMethodName) {
        WebElement paymentMethod = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='withdraw']//img[contains(@src, '"+paymentMethodName+"')]"));
        paymentMethod.click();
        return this;
    }

    public WithdrawSection clickRadioButton() {
        WebElement emailRadioBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//input[@type='radio']/ancestor::span"));
        emailRadioBtn.click();
        return this;
    }

    public WithdrawSection clickContinueButton() {
        WebElement continueBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//button[text()='Continue']"));
        JavaScriptExecutors.clickElement(webDriver, continueBtn);
        return this;
    }

    public WithdrawSection clickButtonWithAmountToWithdraw(String amount) {
        WebElement amountBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//button/span[text()='$"+amount+"']"));
        amountBtn.click();
        return this;
    }

    public WithdrawSection clickRequestWithdrawButton() {
        WebElement requestWithdrawBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//button[text()='Request withdrawal']"));
        requestWithdrawBtn.click();
        return this;
    }

    public WithdrawSection clickConfirmButton() {

        Awaitility
                .await()
                .pollInterval(2, TimeUnit.SECONDS)
                .atMost(20, TimeUnit.SECONDS)
                .until(() ->
                {
                    List<WebElement> sectionButtons = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver, By.xpath("//div[@class='withdraw']//button"));
                    WebElement confirmButton = sectionButtons.get(0);
                    return confirmButton.isDisplayed();
                });
        List<WebElement> sectionButtons = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver, By.xpath("//div[@class='withdraw']//button"));
        WebElement confirmButton = sectionButtons.get(0);
        confirmButton.click();
        return this;
    }

    public String getSuccessMessageTitle() {
        WebElement messageTitle = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'withdrawAmount')]/p"));
        return messageTitle.getText();
    }

    public String getSuccessMessage() {
        WebElement message = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'responseMessage')]//p"));
        return message.getText();
    }

    public Boolean isPaymentMethodCorrect(String paymentMethod) {
        WebElement paymentMethodImg = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'payment')]/img[contains(@src, '"+paymentMethod+"')]"));
        return paymentMethodImg.isDisplayed();

    }

    public BigDecimal getWithdrawAmountAsBigDecimal(String withdrawAmount) {

        BigDecimal withdrawAmountAsBigDecimal = new BigDecimal(withdrawAmount);
        return withdrawAmountAsBigDecimal;
    }

    public BigDecimal getAvailableBalanceInWithdrawSectionAsBigDecimal() {
        WebElement balance = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//p[contains(text(), 'Available Balance')]//following-sibling::p"));
        String balanceAsString = balance.getText();
        String balanceWithoutSymbols = balanceAsString.substring(1).replace(",", "");
        BigDecimal balanceAsBigDecimal = new BigDecimal(balanceWithoutSymbols);
        return balanceAsBigDecimal;
    }

    public WithdrawSection clickDoneButton() {
        WebElement doneBtn = SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//button[text()= 'Done']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", doneBtn);
        return this;
    }

    public WithdrawSection enterWithdrawAmount(String amount) {
        WebElement inputField = SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//label[text()='Enter amount']//following::input"));
        inputField.sendKeys(amount);
        return this;
    }

}
