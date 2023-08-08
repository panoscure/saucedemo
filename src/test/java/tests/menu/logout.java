package tests.menu;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.LoginObject;
import com.google.common.truth.Truth;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class logout extends BaseTest {

    public String username = BaseTest.getPropertyValue("sauce.user");
    public String password = BaseTest.getPropertyValue("sauce.password");


    @Test
    @DisplayName("click about")
    public void about() {

        System.out.println("mainPageGetProductName: ");


            WebDriverManager.chromedriver().driverVersion("115.0").setup();
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments(new String[]{"--start-maximized"});
            chromeOptions.addArguments(new String[]{"--ignore-certificate-errors"});
            chromeOptions.addArguments(new String[]{"--remote-debugging-port=9222"});

            WebDriver webDriver = new ChromeDriver(chromeOptions);
            webDriver.get(url);
            //LoginToSite login = new LoginToSite();
            //login.performLogin();

            LoginObject loginObject= new LoginObject(webDriver);
            HomePage homePage= loginObject.fillUsername(webDriver, username).fillPassword(webDriver, password).clickLoginButton(webDriver);
            String message = homePage.getPageTitle();


            //Click Menu
            homePage.clickMenu();


            String cookiesScreen;
            homePage.clickLogout();

            String loginText = loginObject.verifyLoginScreen();

        Truth.assertThat(loginText).isEqualTo("Swag Labs");

        System.out.println("loginText: "+loginText);

        webDriver.close();



    }

}
