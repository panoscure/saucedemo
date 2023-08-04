package tests.gaming.history;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import common.utils.Properties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import selenium.WebDriverInit;

@ExtendWith(WebDriverInit.class)
public class Filtra extends BaseTest {

    public String username = Properties.getPropertyValue("canvas.user");
    public String password = Properties.getPropertyValue("canvas.password");

    @Test
    public void dimitrismethododtest(WebDriver webDriver) {

        webDriver.get("http://canvasweb.ilottery.dev.azure.l10.intralot.com/");
        HomePage homePage = new HomePage(webDriver);
        homePage.clickLoginButton();

    }
}
