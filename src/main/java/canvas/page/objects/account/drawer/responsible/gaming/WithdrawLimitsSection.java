package canvas.page.objects.account.drawer.responsible.gaming;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

import java.math.BigDecimal;
import java.util.List;

public class WithdrawLimitsSection {
    WebDriver webDriver;

    public BigDecimal getDailyLimitLeftToWithdraw(){
        String dailyLimitFullText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                        By.xpath("(//div[contains(@class, 'limits--active')]//div[contains(@class, 'row')]/div[@class='col-sm-6'])[1]"))
                .getText();
        String dailyLimit = dailyLimitFullText.substring(1, dailyLimitFullText.indexOf("Left to withdraw")-1)
                .replace(",", "");
        return new BigDecimal(dailyLimit);
    }

    public BigDecimal getWeeklyLimitLeftToWithdraw(){
        String weeklyLimitFullText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                        By.xpath("(//div[contains(@class, 'limits--active')]//div[contains(@class, 'row')]/div[@class='col-sm-6'])[2]"))
                .getText();
        String weeklyLimit = weeklyLimitFullText.substring(1, weeklyLimitFullText.indexOf("Left to withdraw")-1)
                .replace(",", "");
        return new BigDecimal(weeklyLimit);
    }

    public BigDecimal getMonthlyLimitLeftToWithdraw(){
        String monthlyLimitFullText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                        By.xpath("(//div[contains(@class, 'limits--active')]//div[contains(@class, 'row')]/div[@class='col-sm-6'])[3]"))
                .getText();
        String monthlyLimit = monthlyLimitFullText.substring(1, monthlyLimitFullText.indexOf("Left to withdraw")-1)
                .replace(",", "");
        return new BigDecimal(monthlyLimit);
    }
    public WithdrawLimitsSection(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public List<WebElement> getAllDurationsWithdrawLimitsAmounts() {
        List<WebElement> allLimits = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[contains(@class, 'limits--active')]//div[contains(@class, 'row')]"));
        return allLimits;
    }

    public String getDailyWithdrawLimitAmount() {
        WebElement withdrawLimitDailyDuration = getAllDurationsWithdrawLimitsAmounts().get(0);
        List<WebElement> dailyWithdraw = withdrawLimitDailyDuration.findElements(By.xpath("./div"));
        String dailyWithdrawAmount = dailyWithdraw.get(1).getText().substring(3);
        return dailyWithdrawAmount.substring(0,dailyWithdrawAmount.length()-3);
    }

    public String getWeeklyWithdrawLimitAmount() {
        WebElement withdrawLimitWeeklyDuration = getAllDurationsWithdrawLimitsAmounts().get(1);
        List<WebElement> weeklyWithdraw = withdrawLimitWeeklyDuration.findElements(By.xpath("./div"));
        String weeklyWithdrawAmount = weeklyWithdraw.get(1).getText().substring(3);
        return weeklyWithdrawAmount.substring(0,weeklyWithdrawAmount.length()-3);
    }

    public String getMonthlyWithdrawLimitAmount() {
        WebElement withdrawLimitMonthlylyDuration = getAllDurationsWithdrawLimitsAmounts().get(2);
        List<WebElement> monthlyWithdraw = withdrawLimitMonthlylyDuration.findElements(By.xpath("./div"));
        String monthlyWithdrawAmount = monthlyWithdraw.get(1).getText().substring(3);
        return monthlyWithdrawAmount.substring(0,monthlyWithdrawAmount.length()-3);
    }
    
    public String getDailyWithdrawMaxLimitAmount() {
        WebElement withdrawLimitDailyDuration = getAllDurationsWithdrawLimitsAmounts().get(0);
        List<WebElement> dailyWithdraw = withdrawLimitDailyDuration.findElements(By.xpath("./div"));
        String dailyWithdrawAmount = dailyWithdraw.get(1).getText().substring(3);
        return dailyWithdrawAmount;
    }

    public WithdrawLimitsSection clickChangeWithdrawLimitsButton() {
        WebElement changeWithdrawLimitsBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//button[text()='Change Withdrawal limits']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", changeWithdrawLimitsBtn);
        return this;
    }

}
