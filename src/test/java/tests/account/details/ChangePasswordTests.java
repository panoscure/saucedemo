package tests.account.details;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.account.drawer.AccountDrawer;
import canvas.page.objects.account.drawer.account.details.AccountDetails;
import canvas.page.objects.account.drawer.account.details.ChangeAccountDetails;
import canvas.page.objects.account.drawer.account.details.ChangePassword;
import common.utils.Properties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import selenium.WebDriverInit;

import java.util.Random;

import static canvas.helpers.HelperMethods.*;
import static com.google.common.truth.Truth.assertWithMessage;

@ExtendWith(WebDriverInit.class)
public class ChangePasswordTests extends BaseTest {
    public String username = Properties.getPropertyValue("canvas.usr111.username");
    public String password = Properties.getPropertyValue("canvas.usr111.password");


    @BeforeEach
    public void setUp(WebDriver webDriver) {
        logInCanvas(webDriver, canvasUrl, username, password);
    }

    @AfterEach
    public void tearDown(WebDriver webDriver) {
        logOutCanvas(webDriver);
    }

    @Test
    @DisplayName("QAAUT-1589 :: DC iLottery - Desktop - Account - Change password (successful scenario)")
    public void QAAUT$1589$DCiLotteryDesktopAccountChangePasswordSuccessfulScenario(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        homePage.clickUserIcon();

        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("ACCOUNT DETAILS");

        AccountDetails accountDetails = new AccountDetails(webDriver);
        accountDetails.clickChangePassword();

        ChangePassword changePassword = new ChangePassword(webDriver);
        Random randomNum = new Random();
        String newPassword1 = password + randomNum.nextInt(100);

        //password1: 3 to 15 characters long, alphabetical and numerical characters only with no spaces
        changePassword.enterPassword("password", password)
                .enterPassword("newPassword", newPassword1)
                .enterPassword("confirmNewPassword", newPassword1);
                //clickChangePasswordButton();

        ChangeAccountDetails changeAccountDetails = new ChangeAccountDetails(webDriver);

        String successMessage = changeAccountDetails.getSuccessAccountDetailUpdated();
        assertWithMessage("invalid password")
                .that(successMessage.equalsIgnoreCase("Your password has been successfully changed!"))
                .isTrue();

        changePassword.clickBackToAccountDetailsButton();

        accountDetails.clickChangePassword();
        String newPassword2 = password + randomNum.nextInt(100);

        //password2: 3 to 15 characters long, alphabetical and numerical characters only with no spaces
        changePassword.enterPassword("password", newPassword1)
                .enterPassword("newPassword", newPassword2)
                .enterPassword("confirmNewPassword", newPassword2);
                //clickChangePasswordButton();

        successMessage = changeAccountDetails.getSuccessAccountDetailUpdated();
        assertWithMessage("invalid password")
                .that(successMessage.equalsIgnoreCase("Your password has been successfully changed!"))
                .isTrue();

        changePassword.clickBackToAccountDetailsButton();

        accountDetails.clickChangePassword();
        String newPassword3 = password + randomNum.nextInt(100);

        //password3: 3 to 15 characters long, alphabetical and numerical characters only with no spaces
        changePassword.enterPassword("password", newPassword2)
                .enterPassword("newPassword", newPassword3)
                .enterPassword("confirmNewPassword", newPassword3);
                //clickChangePasswordButton();

        successMessage = changeAccountDetails.getSuccessAccountDetailUpdated();
        assertWithMessage("invalid password")
                .that(successMessage.equalsIgnoreCase("Your password has been successfully changed!"))
                .isTrue();

        changePassword.clickBackToAccountDetailsButton();

        accountDetails.clickChangePassword();

        //password
        changePassword.enterPassword("password", newPassword3)
                .enterPassword("newPassword", password)
                .enterPassword("confirmNewPassword", password);
                //clickChangePasswordButton();

        successMessage = changeAccountDetails.getSuccessAccountDetailUpdated();
        assertWithMessage("invalid password")
                .that(successMessage.equalsIgnoreCase("Your password has been successfully changed!"))
                .isTrue();

        logOutCanvas(webDriver);

        logInCanvas(webDriver, canvasUrl, username, password);
        homePage.clickUserIcon();

    }


}
