package canvas.page.objects.account.drawer.responsible.gaming;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

import java.math.BigDecimal;

public class ChangePlayLimitsSection {

    WebDriver webDriver;

    public ChangePlayLimitsSection(WebDriver webDriver) {
        this.webDriver = webDriver;
    }


    public ChangePlayLimitsSection clickDropdownArrow() {
        WebElement arrow = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='DAILY LIMIT']/ancestor::div[contains(@class, 'hasPopupIcon')]//button"));
        arrow.click();
        return this;
    }

    public ChangePlayLimitsSection selectOptionFromDailyLimitDropdown(String dailyLimitSelectDropdownOptionToBeSelected) {
        SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//div[./label[text()='Daily Limit']]//input")).click();
        SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//p[text()='"+dailyLimitSelectDropdownOptionToBeSelected+"']")).click();
        return this;
    }

    public ChangePlayLimitsSection selectOptionFromWeeklyLimitDropdown(String weeklyLimitSelectDropdownOptionToBeSelected) {
        SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//div[./label[text()='Weekly Limit']]//input")).click();
        SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//p[text()='"+weeklyLimitSelectDropdownOptionToBeSelected+"']")).click();
        return this;
    }

    public ChangePlayLimitsSection selectOptionFromMonthlyLimitDropdown(String monthlyLimitSelectDropdownOptionToBeSelected) {
        Awaitility.await().until(()->
        {
            SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//div[./label[text()='Monthly Limit']]//input")).sendKeys(Keys.ARROW_DOWN);
            return webDriver.findElement( By.xpath("//div[./label[text()='Monthly Limit']]//input")).isDisplayed();
        });
        SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//div[./label[text()='Monthly Limit']]//input")).click();
        SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//p[text()='"+monthlyLimitSelectDropdownOptionToBeSelected+"']")).click();
        return this;
    }

    public ChangePlayLimitsSection setDailyLimit(String dailyLimitToBeSet) {
        SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("(//div[.//label[text()='Daily Limit']]/div/div/input)[2]")).click();
        SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("(//div[.//label[text()='Daily Limit']]/div/div/input)[2]")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
        SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("(//div[.//label[text()='Daily Limit']]/div/div/input)[2]")).sendKeys(Keys.BACK_SPACE);
        SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("(//div[.//label[text()='Daily Limit']]/div/div/input)[2]")).sendKeys(dailyLimitToBeSet);
        return this;
    }

    public ChangePlayLimitsSection setWeeklyLimit(String weeklyLimitTobeSet) {
        SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("(//div[.//label[text()='Weekly Limit']]/div/div/input)[2]")).click();
        SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("(//div[.//label[text()='Weekly Limit']]/div/div/input)[2]")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
        SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("(//div[.//label[text()='Weekly Limit']]/div/div/input)[2]")).sendKeys(Keys.BACK_SPACE);
        SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("(//div[.//label[text()='Weekly Limit']]/div/div/input)[2]")).sendKeys(weeklyLimitTobeSet);
        return this;
    }

    public ChangePlayLimitsSection setMonthlyLimit(String monthlyLimitTobeSet) {
        SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("(//div[.//label[text()='Monthly Limit']]/div/div/input)[2]")).click();
        SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("(//div[.//label[text()='Monthly Limit']]/div/div/input)[2]")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
        SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("(//div[.//label[text()='Monthly Limit']]/div/div/input)[2]")).sendKeys(Keys.BACK_SPACE);
        SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("(//div[.//label[text()='Monthly Limit']]/div/div/input)[2]")).sendKeys(monthlyLimitTobeSet);
        return this;
    }
    public ChangePlayLimitsSection clickSetLimitsButton() {
        SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//button[text()='Set limits']")).click();
        return this;
    }

    public String getNewlySetDepositLimit() {
        WebElement inputFieldText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//label[text()='Daily Limit']/ancestor::div[contains(@class, 'hasPopupIcon')]//input"));
        if (inputFieldText.getAttribute("value").equalsIgnoreCase("SET CUSTOM LIMIT")) {
            inputFieldText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                    By.xpath("//label[text()='Daily Limit']/ancestor::div[@class='my-4']/following-sibling::div//input"));
        }
        return inputFieldText.getAttribute("value");
    }


    public ChangePlayLimitsSection clickYesSetLimitsButtonInModal() {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//button[text()='Yes, Set My Limit']"));
        btn.click();
        return this;
    }

    public ChangePlayLimitsSection clickBackToPlayLimitButton() {
        SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//button[text()='Back to Play limits']")).click();
        return this;
    }

    public String getPromptTitlePopUpSetLimits() {
        WebElement response = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("(//div[@id='confirmDialog']//h5)[2]"));
        return response.getText();
    }

    public BigDecimal getPromptDailyAmountToBeSetPopUpSetLimits() {
        String dailyLimitToBeSetFullText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(text(),'Your Limit will be set to a Daily amount of')]")).getText();
        String newDailyLimitToBeSet = dailyLimitToBeSetFullText.substring(
                dailyLimitToBeSetFullText.indexOf("$")+1).replace(",", "");
        return new BigDecimal(newDailyLimitToBeSet);
    }

    public BigDecimal getPromptWeeklyAmountToBeSetPopUpSetLimits() {
        String weeklyLimitToBeSetFullText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(text(),'Your Limit will be set to a Weekly amount of')]")).getText();
        String newWeeklyLimitToBeSet = weeklyLimitToBeSetFullText.substring(
                weeklyLimitToBeSetFullText.indexOf("$")+1).replace(",", "");
        return new BigDecimal(newWeeklyLimitToBeSet);
    }

    public BigDecimal getPromptMonthlyAmountToBeSetPopUpSetLimits() {
        String monthlyLimitToBeSetFullText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(text(),'Your Limit will be set to a Monthly amount of')]")).getText();
        String newMonthlyLimitToBeSet = monthlyLimitToBeSetFullText.substring(
                monthlyLimitToBeSetFullText.indexOf("$")+1).replace(",", "");
        return new BigDecimal(newMonthlyLimitToBeSet);
    }

    public BigDecimal getSuccessChangeDailyPlayLimitValue() {
        String weeklyLimitFullText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                        By.xpath("//div[contains(text(),'your Daily Limit has been set to')]")).getText();
        String newDailyLimitSet = weeklyLimitFullText.substring(weeklyLimitFullText.indexOf("$")+1,weeklyLimitFullText.length()-1).replace(",", "");
        return new BigDecimal(newDailyLimitSet);
    }

    public BigDecimal getSuccessChangeWeeklyPlayLimitValue() {
        String weeklyLimitFullText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(text(),'your Weekly Limit has been set to')]")).getText();
        String newWeeklyLimitSet = weeklyLimitFullText.substring(weeklyLimitFullText.indexOf("$")+1,weeklyLimitFullText.length()-1).replace(",", "");
        return new BigDecimal(newWeeklyLimitSet);
    }

    public BigDecimal getSuccessChangeMonthlyPlayLimitValue() {
        String monthlyLimitFullText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(text(),'your Monthly Limit has been set to')]")).getText();
        String newMonthlyLimitSet = monthlyLimitFullText.substring(monthlyLimitFullText.indexOf("$")+1,monthlyLimitFullText.length()-1).replace(",", "");
        return new BigDecimal(newMonthlyLimitSet);
    }

}
