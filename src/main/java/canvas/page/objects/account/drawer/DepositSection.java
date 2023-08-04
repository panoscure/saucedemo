package canvas.page.objects.account.drawer;

import org.awaitility.Awaitility;
import org.openqa.selenium.*;
import selenium.SeleniumWaits;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DepositSection {
    WebDriver webDriver;

    public DepositSection(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public DepositSection clickToSelectPaymentMethodForDeposit(String paymentMethodName) {
        WebElement paymentMethod = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='deposit']//img[contains(@src, '"+paymentMethodName+"')]"));
        paymentMethod.click();
        return this;
    }

    public DepositSection clickRadioButton() {
        WebElement emailRadioBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//input[@type='radio']/ancestor::span"));
        emailRadioBtn.click();
        return this;
    }

    public DepositSection clickContinueButton() {
        WebElement continueBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//button[text()='Continue']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", continueBtn);
        return this;
    }

    public DepositSection clickButtonWithAmountToDeposit(String amount) {
        WebElement amountBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//button/span[text()='$"+amount+"']"));
        amountBtn.click();
        return this;
    }

    public DepositSection clickDepositButton() {
        WebElement depositBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//button[text()='Deposit']"));
        depositBtn.click();
        return this;
    }

    public DepositSection clickConfirmButton() {

        Awaitility
                .await()
                .pollInterval(2, TimeUnit.SECONDS)
                .atMost(20, TimeUnit.SECONDS)
                .until(() ->
                {
                    List<WebElement> sectionButtons = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver, By.xpath("//div[@class='deposit']//button"));
                    WebElement confirmButton = sectionButtons.get(0);
                    return confirmButton.isDisplayed();
                });
        List<WebElement> sectionButtons = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver, By.xpath("//div[@class='deposit']//button"));
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

    public BigDecimal getDepositAmountAsBigDecimal(String depositAmount) {

        BigDecimal depositAmountAsBigDecimal = new BigDecimal(depositAmount);
        return depositAmountAsBigDecimal;
    }

    public BigDecimal getAvailableBalanceInDepositSectionAsBigDecimal() {
        WebElement balance = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//p[contains(text(), 'Available Balance')]//following-sibling::p"));
        String balanceAsString = balance.getText();
        String balanceWithoutSymbols = balanceAsString.substring(1).replace(",", "");
        BigDecimal balanceAsBigDecimal = new BigDecimal(balanceWithoutSymbols);
        return balanceAsBigDecimal;
    }

    public DepositSection clickDoneButton() {
        WebElement doneBtn = SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//button[text()= 'Done']"));
        doneBtn.click();
        return this;
    }

    public DepositSection enterDepositAmount(String amount) {
        WebElement depositInputField = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//label[text()='Enter amount']/following-sibling::div/input[@value='$0']"));
        depositInputField.sendKeys(amount, Keys.ENTER);
        return this;
    }

    public DepositSection clickTransactionHistoryButton() {
        Awaitility
                .await()
                .pollInterval(3, TimeUnit.SECONDS)
                .atMost(30, TimeUnit.SECONDS)
                .until(() ->
                {
                    List<WebElement> sectionButtons = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                            By.xpath("//div[contains(@class, 'withdrawAmount')]//button"));
                    WebElement btnTransactionHistory = sectionButtons.get(1);
                    JavascriptExecutor js = (JavascriptExecutor) webDriver;
                    js.executeScript("arguments[0].click();", btnTransactionHistory);
                    AccountDrawer accountDrawer = new AccountDrawer(webDriver);
                    return accountDrawer.isMyAccountButtonDisplayed();
                });

        return this;

    }

}
