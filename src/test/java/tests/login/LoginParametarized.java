package tests.login;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.LoginObject;
import com.google.common.truth.Truth;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class LoginParametarized extends BaseTest {

    //public String username = Properties.getPropertyValue("sauce.user");
    public String password = BaseTest.getPropertyValue("sauce.password");



        @ParameterizedTest
        @ValueSource(strings = { "standard_user", "locked_out_user","problem_user","performance_glitch_user" }) // Provide the two values here
        @DisplayName("Parametarized")
        public void parametarized(String username) {



            WebDriverManager.chromedriver().driverVersion("115.0").setup();
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments(new String[]{"--start-maximized"});
            chromeOptions.addArguments(new String[]{"--ignore-certificate-errors"});
            chromeOptions.addArguments(new String[]{"--remote-debugging-port=9222"});

            WebDriver webDriver = new ChromeDriver(chromeOptions);
            webDriver.get(url);


            LoginObject loginObject= new LoginObject(webDriver);
            try {
                HomePage homePage = loginObject.fillUsername(webDriver, username).fillPassword(webDriver, password).clickLoginButton(webDriver);
                String message = homePage.getPageTitle();
                System.out.println("Success Login for user:"+username);
            }
            catch(Exception e) {
                //loginObject.fillUsername(webDriver, username).fillPassword(webDriver, password).clickLoginButton(webDriver);
                String message = String.valueOf(loginObject.getLoginErrorMessage(webDriver));
                System.out.println("Login was not successful for user:"+username+"with message:"+message);
            }
            webDriver.close();



        }

}
