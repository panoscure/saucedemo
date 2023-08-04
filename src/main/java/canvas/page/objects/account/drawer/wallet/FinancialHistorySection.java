package canvas.page.objects.account.drawer.wallet;

import org.awaitility.Awaitility;
import org.openqa.selenium.*;
import selenium.SeleniumWaits;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class FinancialHistorySection {

    WebDriver webDriver;

    public FinancialHistorySection(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebElement getTheLatestFinancialTransaction() {
        List<WebElement> financialTransactions = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'table-row')]"));
        return financialTransactions.get(0);
    }

    public String getLastFinancialTransactionTime() {
        WebElement latestFinancialTransaction = getTheLatestFinancialTransaction();
        WebElement transactionDate = latestFinancialTransaction.findElement(By.xpath("//div[contains(@class, 'table-item-value')]/span"));
        return transactionDate.getText();
    }

    public String getLastFinancialTransactionAmount() {
        WebElement latestFinancialTransaction = getTheLatestFinancialTransaction();
        WebElement transactionAmount = latestFinancialTransaction.findElement(By.xpath("//div[contains(@class, 'amount')]/span"));
        return transactionAmount.getText().substring(1);
    }


    public String getLastFinancialTransactionStatus() {
        WebElement latestFinancialTransaction = getTheLatestFinancialTransaction();
        WebElement transactionStatus = latestFinancialTransaction.findElement(By.xpath("//div[contains(text(), 'Status')]"));
        return transactionStatus.getText();
    }

    public String getLastFinancialTransactionPaymentInstrument() {
        WebElement latestFinancialTransaction = getTheLatestFinancialTransaction();
        WebElement transactionPaymentInstrument = latestFinancialTransaction.findElement(By.xpath("//div[contains(text(), 'Payment')]//following-sibling::span"));
        return transactionPaymentInstrument.getText();
    }

    public FinancialHistorySection clickShowButton(String amount, String paymentInstrument) {
        Awaitility
                .await()
                .pollInterval(4, TimeUnit.SECONDS)
                .atMost(30, TimeUnit.SECONDS)
                .until(() ->
                {
                    WebElement showBtn = SeleniumWaits.elementToBeClickable(webDriver,
                            By.xpath("//button[text()='Show']"));
                    JavascriptExecutor js = (JavascriptExecutor) webDriver;
                    js.executeScript("arguments[0].click();", showBtn);
                    return getTheLatestFinancialTransaction().getText().contains(amount) && getLastFinancialTransactionPaymentInstrument().equalsIgnoreCase(paymentInstrument);
                });

        return this;
    }


    public FinancialHistorySection selectFinancialActivitiesSort() {
        WebElement selectedSortInput = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Sort by']//following-sibling::div/input"));
        String inputValue = selectedSortInput.getAttribute("value");
        WebElement sortByDropdown = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='Sort by']//following-sibling::div"));
        if (!(inputValue.equalsIgnoreCase("Newest"))) {

            do {
                sortByDropdown.click();
                selectedSortInput.sendKeys(Keys.ARROW_UP, Keys.ENTER);
                selectedSortInput = SeleniumWaits.elementToBeClickable(webDriver,
                        By.xpath("//label[text()='Sort by']//following-sibling::div/input"));
                inputValue = selectedSortInput.getAttribute("value");
            } while (!(inputValue.equalsIgnoreCase("Newest")));
        }
        return this;
    }

    public FinancialHistorySection selectActivity(String activity) {
        SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//div[./label[text()='Activity']]//input")).click();
        SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//li[./p[text()='"+activity+"']]")).click();
        return this;
    }

    public String getSectionTitle() {
        WebElement title = SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//h6"));
        return title.getText();
    }

}
