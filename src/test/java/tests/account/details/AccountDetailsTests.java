package tests.account.details;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.account.drawer.AccountDrawer;
import canvas.page.objects.account.drawer.account.details.AccountDetails;
import canvas.page.objects.account.drawer.account.details.ChangeAccountDetails;
import canvas.page.objects.account.drawer.account.details.ChangeAddress;
import common.utils.Properties;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import selenium.WebDriverInit;

import java.util.Random;

import static canvas.helpers.HelperMethods.logInCanvas;
import static canvas.helpers.HelperMethods.logOutCanvas;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

@ExtendWith(WebDriverInit.class)
public class AccountDetailsTests extends BaseTest {

    public String username = Properties.getPropertyValue("canvas.usr111.username");
    public String password = Properties.getPropertyValue("canvas.usr111.password");
    public String email = Properties.getPropertyValue("canvas.email4");


    @BeforeEach
    public void setUp(WebDriver webDriver) {
        logInCanvas(webDriver, canvasUrl, username, password);
    }

    @AfterEach
    public void tearDown(WebDriver webDriver) {
        logOutCanvas(webDriver);
    }

    @Test
    @DisplayName("QAAUT-1463 :: Change Username | Field Validations")
    public void QAAUT$1463$ChangeUsernameFieldValidations(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        homePage.clickUserIcon();

        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("ACCOUNT DETAILS");

        String inputFieldUsername = "username";
        AccountDetails accountDetails = new AccountDetails(webDriver);
        accountDetails.clickChangeAccountDetail(inputFieldUsername);

        String expectedErrorMessage = "Username must be 3 to 15 alphabetical and numerical characters, at least 3 should be alphabetical";
        ChangeAccountDetails changeAccountDetails = new ChangeAccountDetails(webDriver);

        //No spaces are permitted
        changeAccountDetails.enterInputValue(inputFieldUsername, "E na");
        String usernameErrorMessage = changeAccountDetails.getInputFieldValidationMessage(inputFieldUsername);
        assertWithMessage("incorrect error message")
                .that(usernameErrorMessage.equalsIgnoreCase(expectedErrorMessage))
                .isTrue();

        //No symbols are permitted
        changeAccountDetails.enterInputValue(inputFieldUsername, "@El/na?");
        assertWithMessage("incorrect error message")
                .that(usernameErrorMessage.equalsIgnoreCase(expectedErrorMessage))
                .isTrue();

        //No less than 3 alphanumeric chars are permitted
        changeAccountDetails.enterInputValue(inputFieldUsername, "Eg");
        assertWithMessage("incorrect error message")
                .that(usernameErrorMessage.equalsIgnoreCase(expectedErrorMessage))
                .isTrue();

        //No more than 15 alphanumeric chars are permitted
        changeAccountDetails.enterInputValue(inputFieldUsername, "egQAAutomation22");
        assertWithMessage("incorrect error message")
                .that(usernameErrorMessage.equalsIgnoreCase(expectedErrorMessage))
                .isTrue();

        //current username error
        changeAccountDetails.enterInputValue(inputFieldUsername, username);
        usernameErrorMessage = changeAccountDetails.getInputFieldValidationMessage(inputFieldUsername);
        expectedErrorMessage = "Cannot be same as the current user name or email";
        assertWithMessage("incorrect error message")
                .that(usernameErrorMessage.equalsIgnoreCase(expectedErrorMessage))
                .isTrue();

        //empty field
        changeAccountDetails.enterInputValue(inputFieldUsername, "");
        usernameErrorMessage = changeAccountDetails.getInputFieldValidationMessage(inputFieldUsername);
        expectedErrorMessage = "USERNAME is required";
        assertWithMessage("incorrect error message")
                .that(usernameErrorMessage.equalsIgnoreCase(expectedErrorMessage))
                .isTrue();

    }

    @Test
    @DisplayName("QAAUT-1466 :: Change Username")
    public void QAAUT$1466$ChangeUsername(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        homePage.clickUserIcon();

        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("ACCOUNT DETAILS");

        String inputFieldUsername = "username";
        String inputFieldPassword = "password";
        AccountDetails accountDetails = new AccountDetails(webDriver);
        accountDetails.clickChangeAccountDetail(inputFieldUsername);

        ChangeAccountDetails changeAccountDetails = new ChangeAccountDetails(webDriver);
        String newUsername = "automat1on03";

        //username: 3 to 15 characters long, alphabetical and numerical characters only with no spaces
        changeAccountDetails.enterInputValue(inputFieldUsername, newUsername);
        String usernameIsValidMessage = changeAccountDetails.getInputFieldValidationMessage(inputFieldUsername);
        String expectedUsernameValidationMessage = "USERNAME IS VALID";
        assertWithMessage("incorrect error message")
                .that(usernameIsValidMessage.equalsIgnoreCase(expectedUsernameValidationMessage))
                .isTrue();
        changeAccountDetails.enterInputValue(inputFieldPassword, password)
                .clickChangeAccountDetailButton();

        String successMessage = changeAccountDetails.getSuccessAccountDetailUpdated();
        assertWithMessage("invalid username")
                .that(successMessage.equalsIgnoreCase("Your " + inputFieldUsername + " has been successfully changed!"))
                .isTrue();

        changeAccountDetails.clickBackToAccountDetailsButton();
        accountDetails.clickViewMoreButtonInPersonalDetails();
        assertThat(accountDetails.getUsername()).isEqualTo(newUsername);

        logOutCanvas(webDriver);

        logInCanvas(webDriver, canvasUrl, newUsername, password);
        homePage.clickUserIcon();
        accountDrawer.clickTabInAccountDrawer("ACCOUNT DETAILS");
        accountDetails.clickChangeAccountDetail(inputFieldUsername);

        changeAccountDetails.enterInputValue(inputFieldUsername, username)
                .enterInputValue(inputFieldPassword, password)
                .clickChangeAccountDetailButton();

        assertWithMessage("invalid username")
                .that(successMessage.equalsIgnoreCase("Your " + inputFieldUsername + " has been successfully changed!"))
                .isTrue();

        changeAccountDetails.clickBackToAccountDetailsButton();
        accountDetails.clickViewMoreButtonInPersonalDetails();
        assertThat(accountDetails.getUsername()).isEqualTo(username);
    }

    @Test
    @DisplayName("QAAUT-1470 :: Change Email Address")
    public void QAAUT$1470$ChangeEmailAddress(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        homePage.clickUserIcon();

        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("ACCOUNT DETAILS");

        String inputFieldEmail = "email";
        String inputFieldPassword = "password";
        AccountDetails accountDetails = new AccountDetails(webDriver);
        accountDetails.clickChangeAccountDetail(inputFieldEmail);

        ChangeAccountDetails changeAccountDetails = new ChangeAccountDetails(webDriver);
        Random randomNum = new Random();
        String newEmail = "stavros3@intralot" + randomNum.nextInt(100) + ".us";


        //username: 3 to 15 characters long, alphabetical and numerical characters only with no spaces
        changeAccountDetails.enterInputValue(inputFieldEmail, newEmail);
        String emailIsValidMessage = changeAccountDetails.getInputFieldValidationMessage(inputFieldEmail);
        String expectedEmailValidationMessage = "EMAIL ADDRESS IS VALID";
        assertWithMessage("incorrect error message")
                .that(emailIsValidMessage.equalsIgnoreCase(expectedEmailValidationMessage))
                .isTrue();
        changeAccountDetails.enterInputValue(inputFieldPassword, password)
                .clickChangeEmailButton();
        assertThat(changeAccountDetails.getSuccessVerificationCodeSentMessage()).isEqualTo("Verification code sent to " + newEmail + "");
        changeAccountDetails.clickContinueButton();
        assertThat(changeAccountDetails.getSuccessAccountDetailUpdated()).isEqualTo("Your email has been successfully changed!");
        changeAccountDetails.clickBackToAccountDetailsButton();
        accountDetails.clickViewMoreButtonInPersonalDetails();
        assertThat(accountDetails.getEmail()).isEqualTo(newEmail);

        logOutCanvas(webDriver);

        logInCanvas(webDriver, canvasUrl, newEmail, password);
        homePage.clickUserIcon();
        accountDrawer.clickTabInAccountDrawer("ACCOUNT DETAILS");
        accountDetails.clickChangeAccountDetail(inputFieldEmail);

        changeAccountDetails.enterInputValue(inputFieldEmail, email)
                .enterInputValue(inputFieldPassword, password)
                .clickChangeEmailButton();

        assertThat(changeAccountDetails.getSuccessVerificationCodeSentMessage()).isEqualTo("Verification code sent to " + email);
        changeAccountDetails.clickContinueButton();
        assertThat(changeAccountDetails.getSuccessAccountDetailUpdated()).isEqualTo("Your email has been successfully changed!");

        changeAccountDetails.clickBackToAccountDetailsButton();
        accountDetails.clickViewMoreButtonInPersonalDetails();
        assertThat(accountDetails.getEmail()).isEqualTo(email);

    }

    @Test
    @DisplayName("QAAUT-1563 :: Change Email Address | Field Validations")
    public void QAAUT$1563$ChangeEmailAddressFieldValidations(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        homePage.clickUserIcon();

        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("ACCOUNT DETAILS");

        String inputFieldEmail = "email";
        AccountDetails accountDetails = new AccountDetails(webDriver);
        accountDetails.clickChangeAccountDetail(inputFieldEmail);

        ChangeAccountDetails changeAccountDetails = new ChangeAccountDetails(webDriver);

        //email: @ is missing
        String newEmail = "stavros3intralot.us";

        changeAccountDetails.enterInputValue(inputFieldEmail, newEmail);
        String emailIsValidMessage = changeAccountDetails.getInputFieldValidationMessage(inputFieldEmail);
        String expectedEmailValidationMessage = "Email address is not valid";
        assertWithMessage("incorrect error message")
                .that(emailIsValidMessage.equalsIgnoreCase(expectedEmailValidationMessage))
                .isTrue();

        //email: . is missing
        newEmail = "stavros3@intralotus";

        changeAccountDetails.enterInputValue(inputFieldEmail, newEmail);

        assertWithMessage("incorrect error message")
                .that(emailIsValidMessage.equalsIgnoreCase(expectedEmailValidationMessage))
                .isTrue();

        //email: less than 6-80 characters in total
        newEmail = "2@3.c";

        changeAccountDetails.enterInputValue(inputFieldEmail, newEmail);

        assertWithMessage("incorrect error message")
                .that(emailIsValidMessage.equalsIgnoreCase(expectedEmailValidationMessage))
                .isTrue();

        //email: current email address
        changeAccountDetails.enterInputValue(inputFieldEmail, email);
        emailIsValidMessage = changeAccountDetails.getInputFieldValidationMessage(inputFieldEmail);
        expectedEmailValidationMessage = "Cannot be the same as the current email";

        assertWithMessage("incorrect error message")
                .that(emailIsValidMessage.equalsIgnoreCase(expectedEmailValidationMessage))
                .isTrue();

        //email: empty field
        changeAccountDetails.enterInputValue(inputFieldEmail, "");
        emailIsValidMessage = changeAccountDetails.getInputFieldValidationMessage(inputFieldEmail);
        expectedEmailValidationMessage = "EMAIL is required";

        assertWithMessage("incorrect error message")
                .that(emailIsValidMessage.equalsIgnoreCase(expectedEmailValidationMessage))
                .isTrue();


    }

    @Test
    @DisplayName("QAAUT-1608 :: DC iLottery - Desktop - Account - Change address details, mobile and home number")
    public void QAAUT$1608$DCiLotteryDesktopAccountChangeAddressDetailsMobileAndHomeNumber(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        homePage.clickUserIcon();

        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("ACCOUNT DETAILS");
        AccountDetails accountDetails = new AccountDetails(webDriver);

        ChangeAccountDetails changeAccountDetails = new ChangeAccountDetails(webDriver);

        //1. change address
        String inputFieldAddress = "address";
        accountDetails.clickChangeAccountDetail(inputFieldAddress);

        ChangeAddress changeAddress = new ChangeAddress(webDriver);

        //country cannot be updated
        assertWithMessage("country field is not disabled")
                .that(changeAddress.isCountryFieldDisabled())
                .isTrue();

        //streetName, streetNumber, postCode, password
        String streetName = RandomStringUtils.randomAlphabetic(6);
        String streetNumber = RandomStringUtils.randomNumeric(4);
        String postCode = RandomStringUtils.randomNumeric(5);

        changeAddress.enterValueToInputField("streetName", streetName)
                .enterValueToInputField("streetNumber", streetNumber)
                .selectComboboxOption("State");
        String state = changeAddress.getComboboxOption("State");

        changeAddress.selectComboboxOption("City");
        String city = changeAddress.getComboboxOption("City");

        changeAddress.enterValueToInputField("postCode", postCode)
                .enterValueToInputField("password", password)
                .clickSubmitButton();


        String successMessage = changeAccountDetails.getSuccessAccountDetailUpdated();

        assertWithMessage("address not updated")
                .that(successMessage.equalsIgnoreCase("Your " + inputFieldAddress + " has been successfully changed!"))
                .isTrue();


        changeAccountDetails.clickBackToAccountDetailsButton();
        accountDrawer.clickTabInAccountDrawer("RESPONSIBLE GAMING");
        accountDrawer.clickPreviousArrowButtonInAccountDrawer();
        accountDrawer.clickTabInAccountDrawer("ACCOUNT DETAILS");


        //2. change mobile number
        String inputFieldMobile = "mobile number";

        accountDetails.clickChangeAccountDetail(inputFieldMobile);

        String mobileNumber = "(666)555-" + RandomStringUtils.randomNumeric(4);

        changeAccountDetails.enterInputValue("mobile", mobileNumber)
                .enterInputValue("password", password)
                .clickChangeMobileButton();

        successMessage = changeAccountDetails.getSuccessAccountDetailUpdated();

        assertWithMessage("mobile number not updated")
                .that(successMessage.equalsIgnoreCase("Your " + inputFieldMobile + " has been successfully changed!"))
                .isTrue();

        changeAccountDetails.clickBackToAccountDetailsButton();
        accountDrawer.clickTabInAccountDrawer("RESPONSIBLE GAMING");
        accountDrawer.clickPreviousArrowButtonInAccountDrawer();
        accountDrawer.clickTabInAccountDrawer("ACCOUNT DETAILS");

        //3.change home number
        String inputFieldHome = "home number";

        accountDetails.clickChangeAccountDetail(inputFieldHome);

        String homeNumber = "(666)555-33" + RandomStringUtils.randomNumeric(2);

        changeAccountDetails.enterInputValue("phone", homeNumber)
                .enterInputValue("password", password)
                .clickChangeHomeNumberButton();

        successMessage = changeAccountDetails.getSuccessAccountDetailUpdated();

        assertWithMessage("home number not updated")
                .that(successMessage.equalsIgnoreCase("Your " + inputFieldHome + " has been successfully changed!"))
                .isTrue();

        changeAccountDetails.clickBackToAccountDetailsButton();
        accountDrawer.clickTabInAccountDrawer("RESPONSIBLE GAMING");
        accountDrawer.clickPreviousArrowButtonInAccountDrawer();
        accountDrawer.clickTabInAccountDrawer("ACCOUNT DETAILS");

        //assert account details updates
        accountDetails.clickViewMoreButtonInPersonalDetails();

        String viewMoreHomeNumber = accountDetails.getPersonalDetails("Home number");
        assertWithMessage("home number incorrect")
                .that(homeNumber.equalsIgnoreCase(viewMoreHomeNumber))
                .isTrue();
        String viewMoreMobile = accountDetails.getPersonalDetails("Mobile number");
        assertWithMessage("mobile number incorrect")
                .that(mobileNumber.equalsIgnoreCase(viewMoreMobile))
                .isTrue();

        String viewMoreStreetName = accountDetails.getAddressDetails("Street name");
        assertWithMessage("street name incorrect")
                .that(streetName.equalsIgnoreCase(viewMoreStreetName))
                .isTrue();

        String viewMoreStreetNumber = accountDetails.getAddressDetails("Street number");
        assertWithMessage("street number incorrect")
                .that(streetNumber.equalsIgnoreCase(viewMoreStreetNumber))
                .isTrue();

        String viewMoreState = accountDetails.getAddressDetails("State");
        assertWithMessage("state incorrect")
                .that(state.equalsIgnoreCase(viewMoreState))
                .isTrue();

        String viewMoreCity = accountDetails.getAddressDetails("City");
        assertWithMessage("city incorrect")
                .that(city.equalsIgnoreCase(viewMoreCity))
                .isTrue();

        String viewMorePostCode = accountDetails.getAddressDetails("Zip code");
        assertWithMessage("post code incorrect")
                .that(postCode.equalsIgnoreCase(viewMorePostCode))
                .isTrue();


    }

}
