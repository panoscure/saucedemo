package tests.responsible.gaming;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.account.drawer.AccountDrawer;
import canvas.page.objects.account.drawer.Tabs;
import canvas.page.objects.account.drawer.responsible.gaming.ChangeWithdrawLimitsSection;
import canvas.page.objects.account.drawer.responsible.gaming.WithdrawLimitsSection;
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
public class WithdrawLimits extends BaseTest {

    public String username = Properties.getPropertyValue("canvas.user");
    public String password = Properties.getPropertyValue("canvas.password");

    @BeforeEach
    public void setUp(WebDriver webDriver) {
        logInCanvas(webDriver, canvasUrl, username, password);
    }

    @AfterEach
    public void tearDown(WebDriver webDriver) {
        logOutCanvas(webDriver);
    }

    @Test
    @DisplayName("QAAU23-33 :: iLottery - Responsible Gaming - Withdraw Limits")
    public void QAAU23_33$iLotteryResponsibleGamingWithdrawLimits(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        homePage.clickUserIcon();

        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("RESPONSIBLE GAMING");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("WITHDRAWAL LIMITS");

        WithdrawLimitsSection withdrawLimitsSection = new WithdrawLimitsSection(webDriver);

        BigDecimal dailyLimit = withdrawLimitsSection.getDailyLimitLeftToWithdraw();
        BigDecimal weeklyLimit = withdrawLimitsSection.getWeeklyLimitLeftToWithdraw();
        BigDecimal monthlyLimit = withdrawLimitsSection.getMonthlyLimitLeftToWithdraw();

        BigDecimal dailyLimitToSet = dailyLimit.subtract(new BigDecimal(1));
        BigDecimal weeklyLimitToSet = weeklyLimit.subtract(new BigDecimal(1));
        BigDecimal monthlyLimitToSet = monthlyLimit.subtract(new BigDecimal(1));

        withdrawLimitsSection.clickChangeWithdrawLimitsButton();
        ChangeWithdrawLimitsSection changeWithdrawLimitsSection = new ChangeWithdrawLimitsSection(webDriver);

        String dailyLimitSelectDropdownOptionToBeSelected = "Set custom limit";
        changeWithdrawLimitsSection.selectOptionFromDailyLimitDropdown(dailyLimitSelectDropdownOptionToBeSelected);
        changeWithdrawLimitsSection.setDailyLimit(dailyLimitToSet.toString());

        String weeklyLimitSelectDropdownOptionToBeSelected = "Set custom limit";
        changeWithdrawLimitsSection.selectOptionFromWeeklyLimitDropdown(weeklyLimitSelectDropdownOptionToBeSelected);
        changeWithdrawLimitsSection.setWeeklyLimit(weeklyLimitToSet.toString());

        String monthlyLimitSelectDropdownOptionToBeSelected = "Set custom limit";
        changeWithdrawLimitsSection.selectOptionFromMonthlyLimitDropdown(monthlyLimitSelectDropdownOptionToBeSelected);
        changeWithdrawLimitsSection.setMonthlyLimit(monthlyLimitToSet.toString());

        changeWithdrawLimitsSection.clickSetLimitsButton();
        Truth.assertThat(changeWithdrawLimitsSection.getPromptTitlePopUpSetLimits()).isEqualTo("Are you sure you want to set this limit?");
        Truth.assertThat(changeWithdrawLimitsSection.getPromptDailyAmountToBeSetPopUpSetLimits().compareTo(dailyLimitToSet)).isEqualTo(0);
        Truth.assertThat(changeWithdrawLimitsSection.getPromptWeeklyAmountToBeSetPopUpSetLimits().compareTo(weeklyLimitToSet)).isEqualTo(0);
        Truth.assertThat(changeWithdrawLimitsSection.getPromptMonthlyAmountToBeSetPopUpSetLimits().compareTo(monthlyLimitToSet)).isEqualTo(0);
        changeWithdrawLimitsSection.clickYesSetLimitsButtonInModal();

        Truth.assertThat(changeWithdrawLimitsSection.getSuccessChangeDailyDepositLimitValue().compareTo(dailyLimitToSet)).isEqualTo(0);
        Truth.assertThat(changeWithdrawLimitsSection.getSuccessChangeWeeklyDepositLimitValue().compareTo(monthlyLimitToSet)).isEqualTo(0);
        Truth.assertThat(changeWithdrawLimitsSection.getSuccessChangeMonthlyDepositLimitValue().compareTo(weeklyLimitToSet)).isEqualTo(0);
        changeWithdrawLimitsSection.clickBackToWithdrawalLimitButton();

        Truth.assertThat(changeWithdrawLimitsSection.getDailyLimitCurrentValue().compareTo(dailyLimitToSet)).isEqualTo(0);
        Truth.assertThat(changeWithdrawLimitsSection.getWeeklyLimitCurrentValue().compareTo(weeklyLimitToSet)).isEqualTo(0);
        Truth.assertThat(changeWithdrawLimitsSection.getMonthlyLimitCurrentValue().compareTo(monthlyLimitToSet)).isEqualTo(0);

    }


    @Test
    @DisplayName("After All test")
    public void setDailyWithdrawalLimits(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        homePage.clickUserIcon();

        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("RESPONSIBLE GAMING");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("WITHDRAW LIMITS");

        WithdrawLimitsSection withdrawLimitsSection = new WithdrawLimitsSection(webDriver);

        if (!(withdrawLimitsSection.getDailyWithdrawMaxLimitAmount().equalsIgnoreCase("$4,999.99"))) {
            withdrawLimitsSection.clickChangeWithdrawLimitsButton();

            ChangeWithdrawLimitsSection changeWithdrawLimitsSection = new ChangeWithdrawLimitsSection(webDriver);

            BigDecimal dailyLimitToSetBigDecimal = new BigDecimal(4999);
            String dailyLimitToSetString = dailyLimitToSetBigDecimal.toString();
            changeWithdrawLimitsSection.setDailyLimit(dailyLimitToSetString);


            String newlySetWithdrawDailyLimit = changeWithdrawLimitsSection.getNewlySetDailyWithdrawLimit();
            changeWithdrawLimitsSection.clickSetLimitsButton()
                    .clickYesSetLimitsButtonInModal();

            Truth.assertThat(changeWithdrawLimitsSection.getSuccessChangeDailyWithdrawLimitValue().compareTo(dailyLimitToSetBigDecimal)).isEqualTo(0);
            changeWithdrawLimitsSection.clickBackToWithdrawalLimitButton();
        }
    }

}
