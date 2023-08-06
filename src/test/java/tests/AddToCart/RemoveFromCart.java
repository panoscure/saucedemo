package tests.AddToCart;

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


public class RemoveFromCart extends BaseTest {

    public String username = Properties.getPropertyValue("sauce.user");
    public String password = Properties.getPropertyValue("sauce.password");


    @Test
    @DisplayName("Product add to cart price amount check")
    public void addToCart() {

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


            //HomePage homePage = new HomePage(webDriver);
            String mainPageProductAmount = homePage.getProductAmount();
            System.out.println("mainPageProductAmount: "+mainPageProductAmount);



            homePage.clickproduct("item_0_title_link");
            String mainPageGetProductName = homePage.getProductName();
            System.out.println("mainPageGetProductName: "+mainPageGetProductName);
            homePage.clickAddToCart();
            homePage.openBasket();
            String basketProductName = homePage.getBasketProductName();
            System.out.println("basketProductName: "+basketProductName);
            String basketProductAmount = homePage.getBasketProductAmount();
            System.out.println("basketProductAmount: "+basketProductAmount);

        //String innerPageProductAmount= homePage.getInnerPageProductAmount();

        Truth.assertThat(mainPageProductAmount).isEqualTo(basketProductAmount);
        Truth.assertThat(mainPageGetProductName).isEqualTo(basketProductName);
        homePage.removeFromCart();

        String itemInBasket;
        try{
            homePage.removeFromCart();
            itemInBasket = "true";
        }
        catch (Exception e)
        {
            itemInBasket = "false";
        }

        Truth.assertThat(itemInBasket).isEqualTo("false");

        System.out.println("itemInBasket: "+itemInBasket);
        webDriver.close();


    }

}
