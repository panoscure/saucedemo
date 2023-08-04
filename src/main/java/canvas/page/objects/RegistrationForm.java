package canvas.page.objects;
import org.openqa.selenium.*;
import selenium.SeleniumWaits;

public class RegistrationForm {

    WebDriver webDriver;

    public RegistrationForm(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    //get page title
    public String getPageTitle() {
        WebElement title = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'form-header')]//h3"));
        return title.getText();
    }

    public String getRegistrationStepName() {
        WebElement step = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'form-steps')]//h5"));
        return step.getText();
    }

    //personal details
    public RegistrationForm enterValueInDropdownList(String label) {
        //TITLE, GENDER*, ETHNICITY
        WebElement inputField = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//label[text()='"+label+"']//following-sibling::div//input"));
        inputField.click();
        inputField.sendKeys(Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }

    public RegistrationForm enterValueIntoField(String id, String value) {
        //firstName *, lastName-label *, ssn-label *, mobile *, phone
        WebElement inputField = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//label[@id='"+id+"-label']//following-sibling::div//input"));
        inputField.click();
        inputField.sendKeys(value);
        return this;
    }

    public RegistrationForm enterValueIntoSecondaryField(String id, String value) {
        //firstName *, lastName-label *, ssn-label *, mobile *, phone
        WebElement inputField = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//label[@id='"+id+"-label']//following-sibling::div//input"));
        inputField.click();
        inputField.sendKeys(value);
        return this;
    }

    public RegistrationForm openCalendar() {
        WebElement inputField = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//input[@name = 'dateOfBirth']"));
        inputField.click();
        return this;
    }


    public RegistrationForm clickNextStepButton() {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[contains(@class, 'register-footer')]//button"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", btn);
        return this;
    }

    public String getRequiredFieldText() {
        WebElement textRequiredField = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//div[contains(@class, 'register-footer')]//p[contains(text(), '*')]"));
        return textRequiredField.getText();
    }

    //calendar
    public RegistrationForm clickDateOfBirthField() {
        WebElement calendar = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//input[@name = 'dateOfBirth']"));
        calendar.click();
        return this;
    }

    public RegistrationForm expandAvailableYearsDropdown() {
        WebElement yearsDropdown = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[contains(@class, 'CalendarPicker') and not(contains(@class, 'TransitionContainer'))]//button[contains(@aria-label, 'calendar view is open, switch to year view')]"));
        yearsDropdown.click();
        return this;
    }

}
