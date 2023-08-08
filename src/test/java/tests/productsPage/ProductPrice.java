package tests.productsPage;

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



public class ProductPrice extends BaseTest {

    public String username = BaseTest.getPropertyValue("sauce.user");
    public String password = BaseTest.getPropertyValue("sauce.password");



        @Test
        @DisplayName("Product price amount check")
        public void productPrice() {


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


            //HomePage homePage = new HomePage(webDriver);
            String mainPageProductAmount = homePage.getProductAmount();
            System.out.println("mainPageProductAmount: "+mainPageProductAmount);

            homePage.clickproduct("item_1_title_link");
            String innerPageProductAmount= homePage.getInnerPageProductAmount();

            Truth.assertThat(mainPageProductAmount).isEqualTo(innerPageProductAmount);

            System.out.println("mainPageProductAmount: "+mainPageProductAmount+"innerPageProductAmount: "+innerPageProductAmount);

            webDriver.close();




        }

}
