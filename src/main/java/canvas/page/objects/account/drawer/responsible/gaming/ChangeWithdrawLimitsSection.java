package canvas.page.objects.account.drawer.responsible.gaming;

import org.awaitility.Awaitility;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import selenium.JavaScriptExecutors;
import selenium.SeleniumWaits;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class ChangeWithdrawLimitsSection {

    WebDriver webDriver;

    public ChangeWithdrawLimitsSection(WebDriver webDriver) {
        this.webDriver = webDriver;
    }


    public ChangeWithdrawLimitsSection clickDropdownArrow(String label) {
        WebElement arrow = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//label[text()='"+label+"']/ancestor::div[contains(@class, 'hasPopupIcon')]//button"));
        arrow.click();
        return this;
    }

    public ChangeWithdrawLimitsSection selectOptionFromDailyLimitDropdown(String dailyLimitSelectDropdownOptionToBeSelected) {
        SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//div[./label[text()='Daily Limit']]//input")).click();
        SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//p[text()='"+dailyLimitSelectDropdownOptionToBeSelected+"']")).click();
        return this;
    }

    public ChangeWithdrawLimitsSection selectOptionFromWeeklyLimitDropdown(String weeklyLimitSelectDropdownOptionToBeSelected) {
        SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//div[./label[text()='Weekly Limit']]//input")).click();
        SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//p[text()='"+weeklyLimitSelectDropdownOptionToBeSelected+"']")).click();
        return this;
    }

    public ChangeWithdrawLimitsSection selectOptionFromMonthlyLimitDropdown(String monthlyLimitSelectDropdownOptionToBeSelected) {
        Awaitility.await().until(()->
        {
            SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//div[./label[text()='Monthly Limit']]//input")).sendKeys(Keys.ARROW_DOWN);
            return webDriver.findElement( By.xpath("//div[./label[text()='Monthly Limit']]//input")).isDisplayed();
        });
        SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//div[./label[text()='Monthly Limit']]//input")).click();
        SeleniumWaits.visibilityOfElementLocated(webDriver, By.xpath("//p[text()='"+monthlyLimitSelectDropdownOptionToBeSelected+"']")).click();
        return this;
    }

    public ChangeWithdrawLimitsSection setDailyLimit(String dailyLimitToBeSet) {
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

    public ChangeWithdrawLimitsSection setWeeklyLimit(String weeklyLimitTobeSet) {
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

    public ChangeWithdrawLimitsSection setMonthlyLimit(String monthlyLimitTobeSet) {
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

    public BigDecimal getSuccessChangeDailyDepositLimitValue() {
        String weeklyLimitFullText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(text(),'your Daily Limit has been set to')]")).getText();
        String newDailyLimitSet = weeklyLimitFullText.substring(weeklyLimitFullText.indexOf("$")+1,weeklyLimitFullText.length()-1).replace(",", "");
        return new BigDecimal(newDailyLimitSet);
    }

    public BigDecimal getSuccessChangeWeeklyDepositLimitValue() {
        String weeklyLimitFullText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(text(),'your Weekly Limit has been set to')]")).getText();
        String newWeeklyLimitSet = weeklyLimitFullText.substring(weeklyLimitFullText.indexOf("$")+1,weeklyLimitFullText.length()-1).replace(",", "");
        return new BigDecimal(newWeeklyLimitSet);
    }

    public BigDecimal getSuccessChangeMonthlyDepositLimitValue() {
        String monthlyLimitFullText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(text(),'your Monthly Limit has been set to')]")).getText();
        String newMonthlyLimitSet = monthlyLimitFullText.substring(monthlyLimitFullText.indexOf("$")+1,monthlyLimitFullText.length()-1).replace(",", "");
        return new BigDecimal(newMonthlyLimitSet);
    }

    public ChangeWithdrawLimitsSection enterDailyIncreasedWithdrawLimit(BigDecimal withdrawLimitAsBigDecimal) {
        String inputValue = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//label[text()='Daily Limit']/ancestor::div[contains(@class, 'hasPopupIcon')]//input")).getAttribute("value");
        WebElement limitInputFieldReadonly = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//label[text()='Daily Limit']/ancestor::div[contains(@class, 'hasPopupIcon')]//input[@value='"+inputValue+"']"));
        if (inputValue.equalsIgnoreCase("$125") || inputValue.equalsIgnoreCase("$500") || inputValue.equalsIgnoreCase("$1,000")) {
            clickDropdownArrow("Daily Limit");
            limitInputFieldReadonly.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
        }

        if (inputValue.equalsIgnoreCase("SET CUSTOM LIMIT")) {

            BigDecimal plusOne = new BigDecimal(1);
            String amount = withdrawLimitAsBigDecimal.add(plusOne).toString();
            WebElement limitInputField = SeleniumWaits.visibilityOfElementLocated(webDriver,
                    By.xpath("//label[text()='Daily Limit']/ancestor::div[@class='my-4']/following-sibling::div//input"));
            limitInputField.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
            limitInputField.sendKeys(amount, Keys.TAB);
        }

        if (inputValue.equalsIgnoreCase("$2,000")) {
            limitInputFieldReadonly.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
            enterDailyIncreasedWithdrawLimit(withdrawLimitAsBigDecimal);
        }

        if (inputValue.equalsIgnoreCase("$4,999.99")) {
            clickCancelButton();
        }
        else {
            setDailyLimit(new BigDecimal(4999).toString());
        }
        return this;
    }

    public String getNewlySetDailyWithdrawLimit() {
        WebElement inputFieldText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//label[text()='Daily Limit']/ancestor::div[contains(@class, 'hasPopupIcon')]//input"));
        if (inputFieldText.getAttribute("value").equalsIgnoreCase("SET CUSTOM LIMIT")) {
            inputFieldText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                    By.xpath("//label[text()='Daily Limit']/ancestor::div[@class='my-4']/following-sibling::div//input"));
        }
        return inputFieldText.getAttribute("value");
    }

    public ChangeWithdrawLimitsSection clickBackToWithdrawalLimitButton() {
        SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//button[text()='Back to Withdrawal limits']")).click();
        return this;
    }

    //WEEKLY LIMIT
    public ChangeWithdrawLimitsSection enterWeeklyDecreasedWithdrawLimit(String amount) {
        String inputValue = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//label[text()='Weekly Limit']/ancestor::div[contains(@class, 'hasPopupIcon')]//input")).getAttribute("value");
        WebElement limitInputFieldReadonly = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//label[text()='Weekly Limit']/ancestor::div[contains(@class, 'hasPopupIcon')]//input[@value='"+inputValue+"']"));
        if (inputValue.equalsIgnoreCase("$2,000") || inputValue.equalsIgnoreCase("$3,000") || inputValue.equalsIgnoreCase("$5,000")) {
            clickDropdownArrow("Weekly Limit");
            limitInputFieldReadonly.sendKeys(Keys.ARROW_UP, Keys.ENTER);
        }

        if (inputValue.equalsIgnoreCase("$1,000")) {
            clickDropdownArrow("Weekly Limit");
            limitInputFieldReadonly.sendKeys(Keys.ARROW_UP, Keys.ENTER);
            WebElement limitInputField = SeleniumWaits.visibilityOfElementLocated(webDriver,
                    By.xpath("//label[text()='Weekly Limit']/ancestor::div[@class='my-4']/following-sibling::div//input"));
            limitInputField.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
            limitInputField.sendKeys(amount, Keys.TAB);
        }

        if (inputValue.equalsIgnoreCase("SET CUSTOM LIMIT")) {
            WebElement limitInputField = SeleniumWaits.visibilityOfElementLocated(webDriver,
                    By.xpath("//label[text()='Weekly Limit']/ancestor::div[@class='my-4']/following-sibling::div//input"));
            limitInputField.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
            limitInputField.sendKeys(amount, Keys.TAB);
        }
        return this;
    }

    public ChangeWithdrawLimitsSection enterWeeklyIncreasedWithdrawLimit(BigDecimal withdrawLimitAsBigDecimal) {
        String inputValue = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//label[text()='Weekly Limit']/ancestor::div[contains(@class, 'hasPopupIcon')]//input")).getAttribute("value");
        WebElement limitInputFieldReadonly = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//label[text()='Weekly Limit']/ancestor::div[contains(@class, 'hasPopupIcon')]//input[@value='"+inputValue+"']"));
        if (inputValue.equalsIgnoreCase("$2,000") || inputValue.equalsIgnoreCase("$3,000") || inputValue.equalsIgnoreCase("$5,000")) {
            clickDropdownArrow("Weekly Limit");
            limitInputFieldReadonly.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
        }

        if (inputValue.equalsIgnoreCase("SET CUSTOM LIMIT")) {

            BigDecimal plusOne = new BigDecimal(1);
            String amount = withdrawLimitAsBigDecimal.add(plusOne).toString();
            WebElement limitInputField = SeleniumWaits.visibilityOfElementLocated(webDriver,
                    By.xpath("//label[text()='Weekly Limit']/ancestor::div[@class='my-4']/following-sibling::div//input"));
            limitInputField.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
            limitInputField.sendKeys(amount, Keys.TAB);
        }

        if (inputValue.equalsIgnoreCase("$5,000")) {
            limitInputFieldReadonly.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
            enterWeeklyIncreasedWithdrawLimit(withdrawLimitAsBigDecimal);
        }
        else {
            setWeeklyWithdrawLimit();
        }

        return this;
    }

    public ChangeWithdrawLimitsSection setWeeklyWithdrawLimit() {
        String inputValue = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//label[text()='Weekly Limit']/ancestor::div[contains(@class, 'hasPopupIcon')]//input")).getAttribute("value");
        WebElement limitInputFieldReadonly = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//label[text()='Weekly Limit']/ancestor::div[contains(@class, 'hasPopupIcon')]//input[@value='"+inputValue+"']"));
        while (!(inputValue.equalsIgnoreCase("SET CUSTOM LIMIT"))) {
            clickDropdownArrow("Weekly Limit");
            limitInputFieldReadonly.sendKeys(Keys.ARROW_UP, Keys.ENTER);
            inputValue = SeleniumWaits.visibilityOfElementLocated(webDriver,
                    By.xpath("//label[text()='WEEKLY LIMIT']/ancestor::div[contains(@class, 'hasPopupIcon')]//input")).getAttribute("value");
            limitInputFieldReadonly = SeleniumWaits.visibilityOfElementLocated(webDriver,
                    By.xpath("//label[text()='WEEKLY LIMIT']/ancestor::div[contains(@class, 'hasPopupIcon')]//input[@value='"+inputValue+"']"));
        }

        if (inputValue.equalsIgnoreCase("SET CUSTOM LIMIT")) {

            WebElement limitInputField = SeleniumWaits.visibilityOfElementLocated(webDriver,
                    By.xpath("//label[text()='Weekly Limit']/ancestor::div[@class='my-4']/following-sibling::div//input"));
            limitInputField.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
            limitInputField.sendKeys("1000000", Keys.TAB);
        }

        return this;
    }

    public ChangeWithdrawLimitsSection enterMonthlyIncreasedWithdrawLimit(BigDecimal withdrawLimitAsBigDecimal) {
        String inputValue = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//label[text()='MONTHLY LIMIT']/ancestor::div[contains(@class, 'hasPopupIcon')]//input")).getAttribute("value");
        WebElement limitInputFieldReadonly = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//label[text()='MONTHLY LIMIT']/ancestor::div[contains(@class, 'hasPopupIcon')]//input[@value='"+inputValue+"']"));
        if (inputValue.equalsIgnoreCase("$12,500") || inputValue.equalsIgnoreCase("$50,000") || inputValue.equalsIgnoreCase("$100,000")) {
            clickDropdownArrow("MONTHLY LIMIT");
            limitInputFieldReadonly.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
        }

        if (inputValue.equalsIgnoreCase("SET CUSTOM LIMIT")) {
            BigDecimal plusOne = new BigDecimal(1);
            String amount = withdrawLimitAsBigDecimal.add(plusOne).toString();
            WebElement limitInputField = SeleniumWaits.visibilityOfElementLocated(webDriver,
                    By.xpath("//label[text()='MONTHLY LIMIT']/ancestor::div[@class='my-4']/following-sibling::div//input"));
            limitInputField.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
            limitInputField.sendKeys(amount, Keys.TAB);
        }

        if (inputValue.equalsIgnoreCase("$100,000")) {
            limitInputFieldReadonly.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
            enterMonthlyIncreasedWithdrawLimit(withdrawLimitAsBigDecimal);
        }
        else {
            setMonthlyWithdrawLimit();
        }
        return this;
    }

    public ChangeWithdrawLimitsSection setMonthlyWithdrawLimit() {
        String inputValue = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//label[text()='MONTHLY LIMIT']/ancestor::div[contains(@class, 'hasPopupIcon')]//input")).getAttribute("value");
        WebElement limitInputFieldReadonly = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//label[text()='MONTHLY LIMIT']/ancestor::div[contains(@class, 'hasPopupIcon')]//input[@value='"+inputValue+"']"));
        while (!(inputValue.equalsIgnoreCase("SET CUSTOM LIMIT"))) {
            clickDropdownArrow("MONTHLY LIMIT");
            limitInputFieldReadonly.sendKeys(Keys.ARROW_UP, Keys.ENTER);
            inputValue = SeleniumWaits.visibilityOfElementLocated(webDriver,
                    By.xpath("//label[text()='MONTHLY LIMIT']/ancestor::div[contains(@class, 'hasPopupIcon')]//input")).getAttribute("value");
            limitInputFieldReadonly = SeleniumWaits.visibilityOfElementLocated(webDriver,
                    By.xpath("//label[text()='MONTHLY LIMIT']/ancestor::div[contains(@class, 'hasPopupIcon')]//input[@value='"+inputValue+"']"));
        }

        if (inputValue.equalsIgnoreCase("SET CUSTOM LIMIT")) {

            WebElement limitInputField = SeleniumWaits.visibilityOfElementLocated(webDriver,
                    By.xpath("//label[text()='MONTHLY LIMIT']/ancestor::div[@class='my-4']/following-sibling::div//input"));
            limitInputField.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
            limitInputField.sendKeys("1000000", Keys.TAB);
        }

        return this;
    }

    public ChangeWithdrawLimitsSection clickSetLimitsButton() {
        Actions at = new Actions(webDriver);
        at.sendKeys(Keys.PAGE_DOWN).build().perform();



        SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//button[text()='Set limits']")).click();
        return this;
    }

    public ChangeWithdrawLimitsSection clickCancelButton() {

        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//button[text()='CANCEL']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", btn);
        //btn.click();
        return this;
    }

    public ChangeWithdrawLimitsSection clickYesSetLimitsButtonInModal() {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//button[text()='Yes, Set My Limit']"));
        btn.click();
        return this;
    }

    public BigDecimal getSuccessChangeDailyWithdrawLimitValue() {
        String weeklyLimitFullText = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(text(),'your Daily Limit has been set to')]")).getText();
        String newDailyLimitSet = weeklyLimitFullText.substring(weeklyLimitFullText.indexOf("$")+1,weeklyLimitFullText.length()-1).replace(",", "");
        return new BigDecimal(newDailyLimitSet);
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

}
