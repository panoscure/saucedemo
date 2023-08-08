package tests.login;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.LoginObject;
import com.google.common.truth.Truth;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.google.common.truth.Truth.assertWithMessage;



public class LoginToSite extends BaseTest {


    public String username = BaseTest.getPropertyValue("sauce.user");
    public String password = BaseTest.getPropertyValue("sauce.password");



        @Test
        @DisplayName("Happy Path Login")
        public void performLogin() {



            WebDriverManager.chromedriver().driverVersion("115.0").setup();
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments(new String[]{"--start-maximized"});
            chromeOptions.addArguments(new String[]{"--ignore-certificate-errors"});
            chromeOptions.addArguments(new String[]{"--remote-debugging-port=9222"});

            WebDriver webDriver = new ChromeDriver(chromeOptions);
            webDriver.get(url);


            LoginObject loginObject= new LoginObject(webDriver);
            HomePage homePage= loginObject.fillUsername(webDriver, username).fillPassword(webDriver, password).clickLoginButton(webDriver);
            String message = homePage.getPageTitle();

            Truth.assertThat(message).isEqualTo("Swag Labs");
            System.out.println("Message is:"+ message);



            System.out.println("Log in was successful");


            webDriver.close();



        }

}
