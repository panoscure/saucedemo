package tests.account.registration;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.RegistrationForm;
import canvas.page.objects.modals.AccountLoginModal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import selenium.WebDriverInit;

import static com.google.common.truth.Truth.assertWithMessage;

@ExtendWith(WebDriverInit.class)
public class RegistrationFormTests extends BaseTest {

    @Test
    @DisplayName("Registration Form Tests")
    public void test(WebDriver webDriver) {
        webDriver.get(canvasUrl);

        HomePage homePage = new HomePage(webDriver);
        homePage.clickLoginButton();

        AccountLoginModal accountLoginModal = new AccountLoginModal(webDriver);
        accountLoginModal.clickRegisterButton();

        RegistrationForm registrationForm = new RegistrationForm(webDriver);

        assertWithMessage("not registration form")
                .that(registrationForm.getPageTitle().equalsIgnoreCase("REGISTRATION"))
                        .isTrue();

        assertWithMessage("incorrect registration step")
                .that(registrationForm.getRegistrationStepName().equalsIgnoreCase("PERSONAL DETAILS"))
                .isTrue();

        //label TITLE, ETHNICITY

        // @id firstName *, lastName-label *, GENDER*, ssn-label *, mobile *, phone
        registrationForm.enterValueIntoField("firstName", "Name")
                .enterValueIntoField("lastName", "Surname")
                .enterValueInDropdownList("GENDER")
                .clickDateOfBirthField()
                .expandAvailableYearsDropdown()
                .enterValueIntoSecondaryField("ssn", "0000")
                .enterValueIntoSecondaryField("mobile", "0123456789")
                .clickNextStepButton();


    }
}
