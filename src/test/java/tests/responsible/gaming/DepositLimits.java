package tests.responsible.gaming;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.account.drawer.AccountDrawer;
import canvas.page.objects.account.drawer.Tabs;
import canvas.page.objects.account.drawer.responsible.gaming.ChangeDepositLimitsSection;
import canvas.page.objects.account.drawer.responsible.gaming.DepositLimitsSection;
import com.google.common.truth.Truth;
import common.utils.Properties;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import selenium.WebDriverInit;

import java.math.BigDecimal;

import static canvas.helpers.HelperMethods.logInCanvas;
import static canvas.helpers.HelperMethods.logOutCanvas;
import static com.google.common.truth.Truth.assertWithMessage;


@ExtendWith(WebDriverInit.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DepositLimits extends BaseTest {

    public String username = Properties.getPropertyValue("canvas.user.georgia4");
    public String password = Properties.getPropertyValue("canvas.password.georgia4");

    @BeforeEach
    public void setUp(WebDriver webDriver) {
        logInCanvas(webDriver, canvasUrl, username, password);
    }

    @AfterEach
    public void tearDown(WebDriver webDriver) {
        logOutCanvas(webDriver);
    }

    @Test
    @DisplayName("QAAU23-36 :: iLottery - Responsible Gaming - Deposit Limits")
    public void QAAU23$36$iLotteryResponsibleGamingDepositLimits(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("RESPONSIBLE GAMING");
        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("DEPOSIT LIMITS");
        DepositLimitsSection depositLimitsSection = new DepositLimitsSection(webDriver);

        BigDecimal dailyLimit = depositLimitsSection.getDailyLimitLeftToDeposit();
        BigDecimal weeklyLimit = depositLimitsSection.getWeeklyLimitLeftToDeposit();
        BigDecimal monthlyLimit = depositLimitsSection.getMonthlyLimitLeftToDeposit();

        BigDecimal dailyLimitToSet = dailyLimit.subtract(new BigDecimal(1));
        BigDecimal weeklyLimitToSet = weeklyLimit.subtract(new BigDecimal(1));
        BigDecimal monthlyLimitToSet = monthlyLimit.subtract(new BigDecimal(1));

        depositLimitsSection.clickChangeDepositLimitsButton();
        ChangeDepositLimitsSection changeDepositLimitsSection = new ChangeDepositLimitsSection(webDriver);

        String dailyLimitSelectDropdownOptionToBeSelected = "Set custom limit";
        changeDepositLimitsSection.selectOptionFromDailyLimitDropdown(dailyLimitSelectDropdownOptionToBeSelected);
        changeDepositLimitsSection.setDailyLimit(dailyLimitToSet.toString());

        String weeklyLimitSelectDropdownOptionToBeSelected = "Set custom limit";
        changeDepositLimitsSection.selectOptionFromWeeklyLimitDropdown(weeklyLimitSelectDropdownOptionToBeSelected);
        changeDepositLimitsSection.setWeeklyLimit(weeklyLimitToSet.toString());

        String monthlyLimitSelectDropdownOptionToBeSelected = "Set custom limit";
        changeDepositLimitsSection.selectOptionFromMonthlyLimitDropdown(monthlyLimitSelectDropdownOptionToBeSelected);
        changeDepositLimitsSection.setMonthlyLimit(monthlyLimitToSet.toString());

        changeDepositLimitsSection.clickSetLimitsButton();
        Truth.assertThat(changeDepositLimitsSection.getPromptTitlePopUpSetLimits()).isEqualTo("Are you sure you want to set this limit?");
        Truth.assertThat(changeDepositLimitsSection.getPromptDailyAmountToBeSetPopUpSetLimits().compareTo(dailyLimitToSet)).isEqualTo(0);
        Truth.assertThat(changeDepositLimitsSection.getPromptWeeklyAmountToBeSetPopUpSetLimits().compareTo(weeklyLimitToSet)).isEqualTo(0);
        Truth.assertThat(changeDepositLimitsSection.getPromptMonthlyAmountToBeSetPopUpSetLimits().compareTo(monthlyLimitToSet)).isEqualTo(0);
        changeDepositLimitsSection.clickYesSetLimitsButtonInModal();

        Truth.assertThat(changeDepositLimitsSection.getSuccessChangeDailyDepositLimitValue().compareTo(dailyLimitToSet)).isEqualTo(0);
        Truth.assertThat(changeDepositLimitsSection.getSuccessChangeWeeklyDepositLimitValue().compareTo(weeklyLimitToSet)).isEqualTo(0);
        Truth.assertThat(changeDepositLimitsSection.getSuccessChangeMonthlyDepositLimitValue().compareTo(monthlyLimitToSet)).isEqualTo(0);
        changeDepositLimitsSection.clickBackToDepositLimitButton();

        Truth.assertThat(depositLimitsSection.getDailyLimitCurrentValue().compareTo(dailyLimitToSet)).isEqualTo(0);
        Truth.assertThat(depositLimitsSection.getWeeklyLimitCurrentValue().compareTo(weeklyLimitToSet)).isEqualTo(0);
        Truth.assertThat(depositLimitsSection.getMonthlyLimitCurrentValue().compareTo(monthlyLimitToSet)).isEqualTo(0);
    }


    @Test
    @DisplayName("After All test")
    public void setDailyDepositLimits(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        homePage.clickUserIcon();

        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("RESPONSIBLE GAMING");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("DEPOSIT LIMITS");

        DepositLimitsSection depositLimitsSection = new DepositLimitsSection(webDriver);
        if (!(depositLimitsSection.getDailyDepositLimitAmount().equalsIgnoreCase("$2,000"))){

            depositLimitsSection.clickChangeDepositLimitsButton();

            ChangeDepositLimitsSection changeDepositLimitsSection = new ChangeDepositLimitsSection(webDriver);

            changeDepositLimitsSection.setDailyLimit("2000");
            String newlySetDepositDailyLimit = changeDepositLimitsSection.getNewlySetDepositLimit();
            changeDepositLimitsSection.clickSetLimitsButton()
                    .clickYesSetLimitsButtonInModal();

            String message = changeDepositLimitsSection.getPromptTitlePopUpSetLimits();

            assertWithMessage("deposit limits are not set")
                    .that(message)
                    .isEqualTo("YOUR DAILY LIMIT HAS BEEN SET TO " + newlySetDepositDailyLimit);


            changeDepositLimitsSection.clickBackToDepositLimitButton();

        }
    }

}
