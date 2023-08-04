package tests.responsible.gaming;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.account.drawer.AccountDrawer;
import canvas.page.objects.account.drawer.Tabs;
import canvas.page.objects.account.drawer.responsible.gaming.CoolOffPeriodSection;

import common.utils.Properties;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import selenium.WebDriverInit;
import selenium.page.objects.pam.page.objects.common.LogInPagePam;

import static canvas.helpers.HelperMethods.logInCanvas;
import static canvas.helpers.HelperMethods.logOutCanvas;
import static com.google.common.truth.Truth.assertThat;

@ExtendWith(WebDriverInit.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CoolOffPeriod extends BaseTest {

    public String username = Properties.getPropertyValue("canvas.username.automation02");
    public String password = Properties.getPropertyValue("canvas.password.automation02");

    @BeforeEach
    public void setUp(WebDriver webDriver) {
        logInCanvas(webDriver, canvasUrl, username, password);
    }

    @AfterEach
    public void tearDown(WebDriver webDriver) {
        logOutCanvas(webDriver);
    }

    @Test
    @Disabled
    @DisplayName("QAAU23-36 :: iLottery - Responsible Gaming - Deposit Limits")
    public void QAAU23$36$iLotteryResponsibleGamingDepositLimits(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("RESPONSIBLE GAMING");
        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("COOL OFF PERIOD");
        CoolOffPeriodSection coolOffPeriodSection = new CoolOffPeriodSection(webDriver);
        coolOffPeriodSection.setCoolOffDuration("3 DAYS").clickBeginCoolOffButton().clickBeginCoolOffButtonPrompt();
        assertThat(coolOffPeriodSection.getSuccessModalHeader()).isEqualTo("Success");
        assertThat(coolOffPeriodSection.getSuccessModalBody())
                .contains("YOU HAVE UPDATE YOUR COOL OFF PERIOD SUCCESSFULLY.");
        coolOffPeriodSection.clickCloseButtonSuccessPopUp();

        webDriver.get(Properties.getPropertyValue("pam.url"));
        LogInPagePam logInPagePam = new LogInPagePam(webDriver);
        logInPagePam
                .fillUsername(Properties.getPropertyValue("pam.username"))
                .fillPassword(Properties.getPropertyValue("pam.password"))
                .clickSignInButton();


    }
}