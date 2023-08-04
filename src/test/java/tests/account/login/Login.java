package tests.account.login;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.modals.AccountLoginModal;
import canvas.page.objects.modals.ForgotPasswordModal;
import canvas.page.objects.modals.PlayerDoesNotExistErrorModal;
import common.utils.Properties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import selenium.WebDriverInit;

import java.util.Random;

import static com.google.common.truth.Truth.assertWithMessage;

@ExtendWith(WebDriverInit.class)
public class Login extends BaseTest {

    public String username = Properties.getPropertyValue("canvas.usr111.username");
    public String password = Properties.getPropertyValue("canvas.usr111.password");
    public String email = Properties.getPropertyValue("canvas.email4");

    @Test
    @DisplayName("QAAUT-1445 :: Reset Password | Invalid Username")
    public void QAAUT$1445$ResetPasswordInvalidUsername(WebDriver webDriver) {
        webDriver.get(canvasUrl);

        HomePage homePage = new HomePage(webDriver);
        homePage.clickLoginButton();

        AccountLoginModal accountLoginModal = new AccountLoginModal(webDriver);
        accountLoginModal.enterValueInInputField("username", username)
                .clickForgotPasswordLink();

        Random r = new Random();
        String incorrectUsername = username + r.nextInt();
        ForgotPasswordModal forgotPasswordModal = new ForgotPasswordModal(webDriver);
        forgotPasswordModal.enterUsername(incorrectUsername)
                .clickResetPasswordButton();

        PlayerDoesNotExistErrorModal playerDoesNotExistErrorModal = new PlayerDoesNotExistErrorModal(webDriver);
        String errorMessage = playerDoesNotExistErrorModal.getErrorMessage();
        assertWithMessage("error message is incorrect")
                .that(errorMessage.equalsIgnoreCase("Player: " + username + "does not exist"));

    }

    @Test
    @DisplayName("QAAUT-1468 :: Login Process | Player | Incorrect Credentials | Failed Login")
    public void QAAUT$1468$LoginProcessPlayerIncorrectCredentialsFailedLogin(WebDriver webDriver) {
        webDriver.get(canvasUrl);

        HomePage homePage = new HomePage(webDriver);
        homePage.clickLoginButton();

        AccountLoginModal accountLoginModal = new AccountLoginModal(webDriver);
        //1.incorrect username
        accountLoginModal.enterValueInInputField("username", "autoMat0Nom0r3")
                .enterValueInInputField("password", password)
                .clickLoginButton();
        String errorMessage = accountLoginModal.getInvalidCredentialsErrorMessage();
        assertWithMessage("incorrect error message")
                .that(errorMessage.equalsIgnoreCase("Invalid username or password."))
                .isTrue();

        //2. incorrect password
        accountLoginModal.enterValueInInputField("username", username)
                .enterValueInInputField("password", "Mypassw0rd")
                .clickLoginButton();
        errorMessage = accountLoginModal.getInvalidCredentialsErrorMessage();
        System.out.println(errorMessage);
        assertWithMessage("incorrect error message")
                .that(errorMessage.contains("The username or password you entered does not exist. If you need help, contact us at 1-833-515-0574 or email us at support@dcilottery.com."))
                .isTrue();

        //3. incorrect both username&password
        accountLoginModal.enterValueInInputField("username", "autoMat0Nom0r3")
                .enterValueInInputField("password", "Mypassw0rd")
                .clickLoginButton();
        errorMessage = accountLoginModal.getInvalidCredentialsErrorMessage();
        System.out.println(errorMessage);
        assertWithMessage("incorrect error message")
                .that(errorMessage.contains("Invalid username or password."))
                .isTrue();
    }

    @Test
    @DisplayName("QAAUT-1469 :: Reset Password | Invalid Email Address")
    public void QAAUT$1469$ResetPasswordInvalidEmailAddress(WebDriver webDriver) {
        webDriver.get(canvasUrl);

        HomePage homePage = new HomePage(webDriver);
        homePage.clickLoginButton();

        AccountLoginModal accountLoginModal = new AccountLoginModal(webDriver);
        accountLoginModal.enterValueInInputField("username", email)
                .clickForgotPasswordLink();

        Random r = new Random();
        String incorrectEmail = email + r.nextInt();
        ForgotPasswordModal forgotPasswordModal = new ForgotPasswordModal(webDriver);
        forgotPasswordModal.enterUsername(incorrectEmail)
                .clickResetPasswordButton();

        PlayerDoesNotExistErrorModal playerDoesNotExistErrorModal = new PlayerDoesNotExistErrorModal(webDriver);
        String errorMessage = playerDoesNotExistErrorModal.getErrorMessage();
        assertWithMessage("error message is incorrect")
                .that(errorMessage.equalsIgnoreCase("Player: " + username + "does not exist"));

    }


}
