package canvas.page.objects.account.drawer.responsible.gaming;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

import java.math.BigDecimal;
import java.util.List;

public class DepositLimitsSection {
    WebDriver webDriver;

    public DepositLimitsSection(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public List<WebElement> getAllDurationsDepositLimitsAmounts() {
        List<WebElement> allLimits = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'limits--active')]//div[contains(@class, 'row')]"));
        return allLimits;
    }

    public String getDailyDepositLimitAmount() {
        WebElement depositLimitDailyDuration = getAllDurationsDepositLimitsAmounts().get(0);
        List<WebElement> dailyDeposit = depositLimitDailyDuration.findElements(By.xpath("./div"));
        String dailyDepositAmount = dailyDeposit.get(1).getText().substring(3);
        return dailyDepositAmount.substring(0,dailyDepositAmount.length()-3);
    }

    public BigDecimal getDailyLimitLeftToDeposit(){
        String dailyLimitFullText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("(//div[contains(@class, 'limits--active')]//div[contains(@class, 'row')]/div[@class='col-sm-6'])[1]"))
                .getText();
        String dailyLimit = dailyLimitFullText.substring(1, dailyLimitFullText.indexOf("Left to deposit")-1)
                .replace(",", "");
        return new BigDecimal(dailyLimit);
    }

    public BigDecimal getWeeklyLimitLeftToDeposit(){
        String weeklyLimitFullText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                        By.xpath("(//div[contains(@class, 'limits--active')]//div[contains(@class, 'row')]/div[@class='col-sm-6'])[2]"))
                .getText();
        String weeklyLimit = weeklyLimitFullText.substring(1, weeklyLimitFullText.indexOf("Left to deposit")-1)
                .replace(",", "");
        return new BigDecimal(weeklyLimit);
    }

    public BigDecimal getMonthlyLimitLeftToDeposit(){
        String monthlyLimitFullText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                        By.xpath("(//div[contains(@class, 'limits--active')]//div[contains(@class, 'row')]/div[@class='col-sm-6'])[3]"))
                .getText();
        String monthlyLimit = monthlyLimitFullText.substring(1, monthlyLimitFullText.indexOf("Left to deposit")-1)
                .replace(",", "");
        return new BigDecimal(monthlyLimit);
    }

    public BigDecimal getDailyLimitCurrentValue(){
        String currentDailyLimitFullText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                        By.xpath("(//div[contains(@class, 'limits--active')]//div[contains(@class, 'row')]/div[@class='col-sm-6 text-right'])[1]"))
                .getText();
        String currentDailyLimit = currentDailyLimitFullText.substring(currentDailyLimitFullText.indexOf("$")+1)
                .replace(",", "");
        return new BigDecimal(currentDailyLimit);
    }

    public BigDecimal getWeeklyLimitCurrentValue(){
        String currentWeeklyLimitFullText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                        By.xpath("(//div[contains(@class, 'limits--active')]//div[contains(@class, 'row')]/div[@class='col-sm-6 text-right'])[2]"))
                .getText();
        String currentWeeklyLimit = currentWeeklyLimitFullText.substring(currentWeeklyLimitFullText.indexOf("$")+1)
                .replace(",", "");
        return new BigDecimal(currentWeeklyLimit);
    }

    public BigDecimal getMonthlyLimitCurrentValue(){
        String currentMonthlyLimitFullText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                        By.xpath("(//div[contains(@class, 'limits--active')]//div[contains(@class, 'row')]/div[@class='col-sm-6 text-right'])[3]"))
                .getText();
        String currentMonthlyLimit = currentMonthlyLimitFullText.substring(currentMonthlyLimitFullText.indexOf("$")+1)
                .replace(",", "");
        return new BigDecimal(currentMonthlyLimit);
    }

    public String getDailyIncreasedDepositLimitAmount() {
        WebElement depositLimit = SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//span[text()='DAILY']//ancestor::div[@class='py-4']//span[text()='APPLICABLE BY']//following-sibling::div"));
        return depositLimit.getText().substring(0,depositLimit.getText().length()-3);
    }
    public BigDecimal getDailyDepositLimitAsBigDecimal(String depositLimit) {
        String depositLimitUpdated = depositLimit;
        BigDecimal depositLimitAsBigDecimal = null;
        if (depositLimit.contains("$")) {
            depositLimitUpdated = depositLimit.replace("$", "");
        }
        String depositLimitUpdatedAgain = depositLimitUpdated;
        if (depositLimit.contains(",")) {
            depositLimitUpdatedAgain = depositLimitUpdated.replace(",", "");
        }
        depositLimitAsBigDecimal = new BigDecimal(depositLimitUpdatedAgain);
        return depositLimitAsBigDecimal;
    }

    public DepositLimitsSection clickChangeDepositLimitsButton() {
        WebElement changeDepositLimitsBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//button[text()='Change Deposit limits']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", changeDepositLimitsBtn);
        //changeDepositLimitsBtn.click();
        return this;
    }

    public String getNewIncreasedDepositLimitOfYourAccount() {
        WebElement newDepositLimit = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//span[text()='NEW DEPOSIT LIMIT']//following-sibling::div/div"));
        String newDepositLimitAmount = newDepositLimit.getText();
        return newDepositLimitAmount.substring(1, newDepositLimitAmount.length()-3).replace("$", "");
    }

    public String getNewIncreasedDepositLimitDate() {
        WebElement newDepositLimit = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//span[text()='NEW DEPOSIT LIMIT']//following-sibling::div/span/span"));
        String newDepositLimitAmount = newDepositLimit.getText();
        return newDepositLimitAmount.substring(1, newDepositLimitAmount.length()-3).replace("$", "");
    }
}
