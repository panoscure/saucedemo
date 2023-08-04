package tests.responsible.gaming;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.account.drawer.AccountDrawer;
import canvas.page.objects.account.drawer.DepositSection;
import canvas.page.objects.account.drawer.Tabs;
import canvas.page.objects.account.drawer.responsible.gaming.ChangeDepositLimitsSection;
import canvas.page.objects.account.drawer.responsible.gaming.ChangePlayLimitsSection;
import canvas.page.objects.account.drawer.responsible.gaming.DepositLimitsSection;
import canvas.page.objects.account.drawer.responsible.gaming.PlayLimitsSection;
import canvas.page.objects.account.drawer.wallet.FinancialHistorySection;
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
public class PlayLimits extends BaseTest {

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
    @DisplayName("QAAU23-67 :: iLottery - Responsible Gaming - Play Limits")
    public void QAAUT23_67$iLotteryResponsibleGamingPlayLimits(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("RESPONSIBLE GAMING");
        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("PLAY LIMITS");
        PlayLimitsSection playLimitsSection = new PlayLimitsSection(webDriver);

        BigDecimal dailyLimit = playLimitsSection.getDailyLimitLeftToPlay();
        BigDecimal weeklyLimit = playLimitsSection.getWeeklyLimitLeftToPlay();
        BigDecimal monthlyLimit = playLimitsSection.getMonthlyLimitLeftToPlay();

        BigDecimal dailyLimitToSet = dailyLimit.subtract(new BigDecimal(1));
        BigDecimal weeklyLimitToSet = weeklyLimit.subtract(new BigDecimal(1));
        BigDecimal monthlyLimitToSet = monthlyLimit.subtract(new BigDecimal(1));

        playLimitsSection.clickChangePlayLimitsButton();
        ChangePlayLimitsSection changePlayLimitsSection = new ChangePlayLimitsSection(webDriver);

        String dailyLimitSelectDropdownOptionToBeSelected = "Set custom limit";
        changePlayLimitsSection.selectOptionFromDailyLimitDropdown(dailyLimitSelectDropdownOptionToBeSelected);
        changePlayLimitsSection.setDailyLimit(dailyLimitToSet.toString());

        String weeklyLimitSelectDropdownOptionToBeSelected = "Set custom limit";
        changePlayLimitsSection.selectOptionFromWeeklyLimitDropdown(weeklyLimitSelectDropdownOptionToBeSelected);
        changePlayLimitsSection.setWeeklyLimit(weeklyLimitToSet.toString());

        String monthlyLimitSelectDropdownOptionToBeSelected = "Set custom limit";
        changePlayLimitsSection.selectOptionFromMonthlyLimitDropdown(monthlyLimitSelectDropdownOptionToBeSelected);
        changePlayLimitsSection.setMonthlyLimit(monthlyLimitToSet.toString());

        changePlayLimitsSection.clickSetLimitsButton();
        Truth.assertThat(changePlayLimitsSection.getPromptTitlePopUpSetLimits()).isEqualTo("Are you sure you want to set this limit?");
        Truth.assertThat(changePlayLimitsSection.getPromptDailyAmountToBeSetPopUpSetLimits().compareTo(dailyLimitToSet)).isEqualTo(0);
        Truth.assertThat(changePlayLimitsSection.getPromptWeeklyAmountToBeSetPopUpSetLimits().compareTo(weeklyLimitToSet)).isEqualTo(0);
        Truth.assertThat(changePlayLimitsSection.getPromptMonthlyAmountToBeSetPopUpSetLimits().compareTo(monthlyLimitToSet)).isEqualTo(0);
        changePlayLimitsSection.clickYesSetLimitsButtonInModal();

        Truth.assertThat(changePlayLimitsSection.getSuccessChangeDailyPlayLimitValue().compareTo(dailyLimitToSet)).isEqualTo(0);
        Truth.assertThat(changePlayLimitsSection.getSuccessChangeWeeklyPlayLimitValue().compareTo(weeklyLimitToSet)).isEqualTo(0);
        Truth.assertThat(changePlayLimitsSection.getSuccessChangeMonthlyPlayLimitValue().compareTo(monthlyLimitToSet)).isEqualTo(0);
        changePlayLimitsSection.clickBackToPlayLimitButton();

        Truth.assertThat(playLimitsSection.getDailyLimitCurrentValue().compareTo(dailyLimitToSet)).isEqualTo(0);
        Truth.assertThat(playLimitsSection.getWeeklyLimitCurrentValue().compareTo(weeklyLimitToSet)).isEqualTo(0);
        Truth.assertThat(playLimitsSection.getMonthlyLimitCurrentValue().compareTo(monthlyLimitToSet)).isEqualTo(0);

    }
}
