package tests.menu;

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


public class about extends BaseTest {

    public String username = Properties.getPropertyValue("sauce.user");
    public String password = Properties.getPropertyValue("sauce.password");


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


            String mainPageProductAmount = homePage.getProductAmount();
            System.out.println("mainPageProductAmount: "+mainPageProductAmount);

            //Click Menu
            homePage.clickMenu();


            String cookiesScreen;
            homePage.clickAbout();
            try{
                homePage.cookiesAbout();
                cookiesScreen = "true";
            }
            catch (Exception e)
            {
                cookiesScreen = "false";
            }

        Truth.assertThat(cookiesScreen).isEqualTo("true");

        System.out.println("cookiesScreen: "+cookiesScreen);

        webDriver.close();


    }

}
