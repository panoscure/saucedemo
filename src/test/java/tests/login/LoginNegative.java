package tests.login;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.LoginObject;
import com.google.common.truth.Truth;
import common.utils.Properties;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class LoginNegative extends BaseTest {

    public String username = Properties.getPropertyValue("sauce.user");
    public String password = Properties.getPropertyValue("sauce.wrongPassword");



        @Test
        @DisplayName("Test to check if i have wrong password the message")
        public void wrongPassword() {


            WebDriverManager.chromedriver().driverVersion("115.0").setup();
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments(new String[]{"--start-maximized"});
            chromeOptions.addArguments(new String[]{"--ignore-certificate-errors"});
            chromeOptions.addArguments(new String[]{"--remote-debugging-port=9222"});

            WebDriver webDriver = new ChromeDriver(chromeOptions);
            webDriver.get(url);


            LoginObject loginObject= new LoginObject(webDriver);
            HomePage homePage= loginObject.fillUsername(webDriver, username).fillPassword(webDriver, password).clickLoginButton(webDriver);
            String message = String.valueOf(loginObject.getLoginErrorMessage(webDriver));
            System.out.println("Message is:"+ message);
            Truth.assertThat(message).contains("Username and password do not match");

            System.out.println("Log in was successful");


            webDriver.close();



        }

}
