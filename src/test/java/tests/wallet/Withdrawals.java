package tests.wallet;

import base.test.BaseTest;
import canvas.page.objects.account.drawer.AccountDrawer;
import canvas.page.objects.account.drawer.Tabs;
import canvas.page.objects.account.drawer.WithdrawSection;
import canvas.page.objects.account.drawer.responsible.gaming.ChangeWithdrawLimitsSection;
import canvas.page.objects.account.drawer.responsible.gaming.WithdrawLimitsSection;
import canvas.page.objects.account.drawer.wallet.FinancialHistorySection;
import com.google.common.truth.Truth;
import common.utils.Properties;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import canvas.page.objects.HomePage;
import selenium.WebDriverInit;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import static canvas.helpers.HelperMethods.logOutCanvas;
import static com.google.common.truth.Truth.assertWithMessage;
import static canvas.helpers.HelperMethods.logInCanvas;



@ExtendWith(WebDriverInit.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Withdrawals extends BaseTest {

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
    @DisplayName("QAAUT-1458 :: Paypal | Withdraw | Online | Pending")
    public void QAAUT$1458$PaypalWithdrawOnlinePending(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickUserIcon();

        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickWithdrawButton();

        String withdrawAmount = "108";
        WithdrawSection withdrawSection = new WithdrawSection(webDriver);
        withdrawSection.clickToSelectPaymentForTheWithdraw("paypal")
                .clickRadioButton()
                .clickContinueButton()
                .enterWithdrawAmount(withdrawAmount)
                .clickRequestWithdrawButton()
                .clickConfirmButton();

        BigDecimal availableBalanceAfterWithdraw = withdrawSection.getAvailableBalanceInWithdrawSectionAsBigDecimal();
        BigDecimal accountBalanceAfterWithdraw = homePage.getBalance();
        assertWithMessage("withdraw is not successful")
                .that(accountBalanceAfterWithdraw)
                .isEqualTo(availableBalanceAfterWithdraw);
        BigDecimal withdrawAmountAsBigDecimal = withdrawSection.getWithdrawAmountAsBigDecimal(withdrawAmount);
        assertWithMessage("withdraw is not successful")
                .that(accountBalance)
                .isEqualTo(availableBalanceAfterWithdraw.add(withdrawAmountAsBigDecimal));

        assertWithMessage("withdraw is not successful")
                .that(withdrawSection.getSuccessMessageTitle().equalsIgnoreCase("SUCCESS!"))
                .isTrue();
        assertWithMessage("withdraw is not successful")
                .that(withdrawSection.getSuccessMessage().equalsIgnoreCase("Your request to withdraw $"+withdrawAmount+".00 has been successfully submitted. You will receive confirmation in your email shortly. Please refresh in order to see your saved payment methods."))
                .isTrue();
        assertWithMessage("payment method is incorrect")
                .that(withdrawSection.isPaymentMethodCorrect("paypal"))
                .isTrue();

        withdrawSection.clickDoneButton();

        accountDrawer.clickBackToMyAccountButton()
                .clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("TRANSACTION HISTORY");

        FinancialHistorySection financialHistorySection = new FinancialHistorySection(webDriver);

        financialHistorySection.clickShowButton(withdrawAmount, "PAYPAL");
        String transactionStatus = financialHistorySection.getLastFinancialTransactionStatus();
        assertWithMessage("transaction is incorrect")
                .that(transactionStatus.contains("Status: Pending"))
                .isTrue();
        String transactionPaymentInstrument = financialHistorySection.getLastFinancialTransactionPaymentInstrument();
        assertWithMessage("transaction is incorrect")
                .that(transactionPaymentInstrument.equalsIgnoreCase("PAYPAL"))
                .isTrue();


    }

    @Test
    @DisplayName("QAAUT-1461 :: Skrill | Withdraw | Online | Pending")
    public void QAAUT$1461$SkrillWithdrawOnlinePending(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickUserIcon();

        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickWithdrawButton();

        String withdrawAmount = "103.00";
        WithdrawSection withdrawSection = new WithdrawSection(webDriver);
        withdrawSection.clickToSelectPaymentForTheWithdraw("skrill")
                .clickRadioButton()
                .clickContinueButton()
                .enterWithdrawAmount(withdrawAmount)
                .clickRequestWithdrawButton()
                .clickConfirmButton();

        BigDecimal availableBalanceAfterWithdraw = withdrawSection.getAvailableBalanceInWithdrawSectionAsBigDecimal();
        BigDecimal accountBalanceAfterWithdraw = homePage.getBalance();
        assertWithMessage("withdraw is not successful")
                .that(accountBalanceAfterWithdraw)
                .isEqualTo(availableBalanceAfterWithdraw);
        BigDecimal withdrawAmountAsBigDecimal = withdrawSection.getWithdrawAmountAsBigDecimal(withdrawAmount);
        assertWithMessage("withdraw is not successful")
                .that(accountBalance)
                .isEqualTo(availableBalanceAfterWithdraw.add(withdrawAmountAsBigDecimal));

        assertWithMessage("withdraw is not successful")
                .that(withdrawSection.getSuccessMessageTitle().equalsIgnoreCase("SUCCESS!"))
                .isTrue();

        Truth.assertThat(withdrawSection.getSuccessMessage()).isEqualTo("Your request to withdraw $"+withdrawAmount+" has been successfully submitted. You will receive confirmation in your email shortly. Please refresh in order to see your saved payment methods.");

        assertWithMessage("payment method is incorrect")
                .that(withdrawSection.isPaymentMethodCorrect("skrill"))
                .isTrue();

        withdrawSection.clickDoneButton();
        accountDrawer.clickBackToMyAccountButton()
                .clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("TRANSACTION HISTORY");

        FinancialHistorySection financialHistorySection = new FinancialHistorySection(webDriver);

        financialHistorySection.selectActivity("Withdrawal");
        financialHistorySection.clickShowButton(withdrawAmount, "SKRILL");
        String transactionStatus = financialHistorySection.getLastFinancialTransactionStatus();
        assertWithMessage("transaction is incorrect")
                .that(transactionStatus.contains("Status: Pending"))
                .isTrue();

        //paymentInstrument
        String transactionPaymentInstrument = financialHistorySection.getLastFinancialTransactionPaymentInstrument();
        assertWithMessage("transaction is incorrect")
                .that(transactionPaymentInstrument.equalsIgnoreCase("SKRILL"))
                .isTrue();

        //amount
        String transactionAmount = financialHistorySection.getLastFinancialTransactionAmount();
        assertWithMessage("transaction amount is incorrect")
                .that(transactionAmount)
                .isEqualTo("$" +withdrawAmount);

        //date-time
        Date date = new Date();
        SimpleDateFormat formatTime = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String today = formatTime.format(date);
        String withdrawDate = financialHistorySection.getLastFinancialTransactionTime().substring(0, financialHistorySection.getLastFinancialTransactionTime().length()-3);
        assertWithMessage("transaction date is incorrect")
                .that(withdrawDate)
                .isEqualTo(today);

    }

    @Test
    @DisplayName("QAAUT-1462 :: Vip Preferred | Withdraw | Online | Pending")
    public void QAAUT$1462$VipPreferredWithdrawOnlinePending(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickUserIcon();

        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickWithdrawButton();

        String withdrawAmount = "101";
        WithdrawSection withdrawSection = new WithdrawSection(webDriver);
        withdrawSection.clickToSelectPaymentForTheWithdraw("vip-final")
                .clickRadioButton()
                .clickContinueButton()
                .enterWithdrawAmount(withdrawAmount)
                .clickRequestWithdrawButton()
                .clickConfirmButton();

        BigDecimal availableBalanceAfterWithdraw = withdrawSection.getAvailableBalanceInWithdrawSectionAsBigDecimal();
        BigDecimal accountBalanceAfterWithdraw = homePage.getBalance();
        assertWithMessage("withdraw is not successful")
                .that(accountBalanceAfterWithdraw)
                .isEqualTo(availableBalanceAfterWithdraw);
        BigDecimal withdrawAmountAsBigDecimal = withdrawSection.getWithdrawAmountAsBigDecimal(withdrawAmount);
        assertWithMessage("withdraw is not successful")
                .that(accountBalance)
                .isEqualTo(availableBalanceAfterWithdraw.add(withdrawAmountAsBigDecimal));

        assertWithMessage("withdraw is not successful")
                .that(withdrawSection.getSuccessMessageTitle().equalsIgnoreCase("SUCCESS!"))
                .isTrue();
        assertWithMessage("withdraw is not successful")
                .that(withdrawSection.getSuccessMessage().equalsIgnoreCase("Your request to withdraw $"+withdrawAmount+".00 has been successfully submitted. You will receive confirmation in your email shortly. Please refresh in order to see your saved payment methods."))
                .isTrue();
        assertWithMessage("payment method is incorrect")
                .that(withdrawSection.isPaymentMethodCorrect("vip-final"))
                .isTrue();

        withdrawSection.clickDoneButton();

        accountDrawer.clickBackToMyAccountButton()
                .clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("TRANSACTION HISTORY");

        FinancialHistorySection financialHistorySection = new FinancialHistorySection(webDriver);
        financialHistorySection.clickShowButton(withdrawAmount, "VIP PREFERRED");
        String transactionStatus = financialHistorySection.getLastFinancialTransactionStatus();
        assertWithMessage("transaction is incorrect")
                .that(transactionStatus.contains("Status: Pending"))
                .isTrue();
        String transactionPaymentInstrument = financialHistorySection.getLastFinancialTransactionPaymentInstrument();
        assertWithMessage("transaction is incorrect")
                .that(transactionPaymentInstrument.equalsIgnoreCase("VIP PREFERRED"))
                .isTrue();

    }
    @Test
    @DisplayName("QAAUT-1446 :: Skrill | Withdraw | Online | Successful")
    public void QAAUT$1446$SkrillWithdrawOnlineSuccessful(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickUserIcon();

        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickWithdrawButton();

        String withdrawAmount = "11";
        WithdrawSection withdrawSection = new WithdrawSection(webDriver);
        withdrawSection.clickToSelectPaymentForTheWithdraw("skrill")
                .clickRadioButton()
                .clickContinueButton()
                .enterWithdrawAmount(withdrawAmount)
                .clickRequestWithdrawButton()
                .clickConfirmButton();

        BigDecimal availableBalanceAfterWithdraw = withdrawSection.getAvailableBalanceInWithdrawSectionAsBigDecimal();
        BigDecimal accountBalanceAfterWithdraw = homePage.getBalance();
        assertWithMessage("withdraw is not successful")
                .that(accountBalanceAfterWithdraw)
                .isEqualTo(availableBalanceAfterWithdraw);
        BigDecimal withdrawAmountAsBigDecimal = withdrawSection.getWithdrawAmountAsBigDecimal(withdrawAmount);
        assertWithMessage("withdraw is not successful")
                .that(accountBalance)
                .isEqualTo(availableBalanceAfterWithdraw.add(withdrawAmountAsBigDecimal));

        assertWithMessage("withdraw is not successful")
                .that(withdrawSection.getSuccessMessageTitle().equalsIgnoreCase("SUCCESS!"))
                .isTrue();
        Truth.assertThat(withdrawSection.getSuccessMessage()).isEqualTo("Your request to withdraw $"+withdrawAmount+".00 has been successfully submitted. You will receive confirmation in your email shortly. Please refresh in order to see your saved payment methods.");
        assertWithMessage("payment method is incorrect")
                .that(withdrawSection.isPaymentMethodCorrect("skrill"))
                .isTrue();

        withdrawSection.clickDoneButton();

        accountDrawer.clickBackToMyAccountButton()
                .clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("TRANSACTION HISTORY");

        FinancialHistorySection financialHistorySection = new FinancialHistorySection(webDriver);

        financialHistorySection.clickShowButton(withdrawAmount, "SKRILL");
        String transactionStatus = financialHistorySection.getLastFinancialTransactionStatus();
        assertWithMessage("transaction is incorrect")
                .that(transactionStatus.equalsIgnoreCase("Status: Approved"))
                .isTrue();
        String transactionPaymentInstrument = financialHistorySection.getLastFinancialTransactionPaymentInstrument();
        assertWithMessage("transaction is incorrect")
                .that(transactionPaymentInstrument.equalsIgnoreCase("SKRILL"))
                .isTrue();

    }

    @Test
    @DisplayName("QAAUT-1447 :: PayPal | Withdraw | Online | Successful")
    public void QAAUT$1447$PayPalWithdrawOnlineSuccessful(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickUserIcon();

        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickWithdrawButton();

        String withdrawAmount = "15";
        WithdrawSection withdrawSection = new WithdrawSection(webDriver);
        withdrawSection.clickToSelectPaymentForTheWithdraw("paypal")
                .clickRadioButton()
                .clickContinueButton()
                .enterWithdrawAmount(withdrawAmount)
                .clickRequestWithdrawButton()
                .clickConfirmButton();

        BigDecimal availableBalanceAfterWithdraw = withdrawSection.getAvailableBalanceInWithdrawSectionAsBigDecimal();
        BigDecimal accountBalanceAfterWithdraw = homePage.getBalance();
        assertWithMessage("withdraw is not successful")
                .that(accountBalanceAfterWithdraw)
                .isEqualTo(availableBalanceAfterWithdraw);
        BigDecimal withdrawAmountAsBigDecimal = withdrawSection.getWithdrawAmountAsBigDecimal(withdrawAmount);
        assertWithMessage("withdraw is not successful")
                .that(accountBalance)
                .isEqualTo(availableBalanceAfterWithdraw.add(withdrawAmountAsBigDecimal));

        assertWithMessage("withdraw is not successful")
                .that(withdrawSection.getSuccessMessageTitle().equalsIgnoreCase("SUCCESS!"))
                .isTrue();
        assertWithMessage("withdraw is not successful")
                .that(withdrawSection.getSuccessMessage().equalsIgnoreCase("Your request to withdraw $"+withdrawAmount+".00 has been successfully submitted. You will receive confirmation in your email shortly. Please refresh in order to see your saved payment methods."))
                .isTrue();
        assertWithMessage("payment method is incorrect")
                .that(withdrawSection.isPaymentMethodCorrect("paypal"))
                .isTrue();

        withdrawSection.clickDoneButton();

        accountDrawer.clickBackToMyAccountButton()
                .clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("TRANSACTION HISTORY");

        FinancialHistorySection financialHistorySection = new FinancialHistorySection(webDriver);

        financialHistorySection.clickShowButton(withdrawAmount, "PAYPAL");
        String transactionStatus = financialHistorySection.getLastFinancialTransactionStatus();

        String transactionPaymentInstrument = financialHistorySection.getLastFinancialTransactionPaymentInstrument();
        assertWithMessage("transaction is incorrect")
                .that(transactionPaymentInstrument.equalsIgnoreCase("PAYPAL"))
                .isTrue();

    }

    @Test
    @DisplayName("QAAUT-1448 :: VIP Preferred | Withdraw | Online | Successful")
    public void QAAUT$1448$VIPPreferredWithdrawOnlineSuccessful(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickUserIcon();

        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickWithdrawButton();

        String withdrawAmount = "10";
        WithdrawSection withdrawSection = new WithdrawSection(webDriver);
        withdrawSection.clickToSelectPaymentForTheWithdraw("vip-final")
                .clickRadioButton()
                .clickContinueButton()
                .enterWithdrawAmount(withdrawAmount)
                .clickRequestWithdrawButton()
                .clickConfirmButton();

        BigDecimal availableBalanceAfterWithdraw = withdrawSection.getAvailableBalanceInWithdrawSectionAsBigDecimal();
        BigDecimal accountBalanceAfterWithdraw = homePage.getBalance();
        assertWithMessage("withdraw is not successful")
                .that(accountBalanceAfterWithdraw)
                .isEqualTo(availableBalanceAfterWithdraw);
        BigDecimal withdrawAmountAsBigDecimal = withdrawSection.getWithdrawAmountAsBigDecimal(withdrawAmount);
        assertWithMessage("withdraw is not successful")
                .that(accountBalance)
                .isEqualTo(availableBalanceAfterWithdraw.add(withdrawAmountAsBigDecimal));

        assertWithMessage("withdraw is not successful")
                .that(withdrawSection.getSuccessMessageTitle().equalsIgnoreCase("SUCCESS!"))
                .isTrue();
        assertWithMessage("withdraw is not successful")
                .that(withdrawSection.getSuccessMessage().equalsIgnoreCase("Your request to withdraw $"+withdrawAmount+".00 has been successfully submitted. You will receive confirmation in your email shortly. Please refresh in order to see your saved payment methods."))
                .isTrue();
        assertWithMessage("payment method is incorrect")
                .that(withdrawSection.isPaymentMethodCorrect("vip-final"))
                .isTrue();

        withdrawSection.clickDoneButton();

        accountDrawer.clickBackToMyAccountButton()
                .clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("TRANSACTION HISTORY");

        FinancialHistorySection financialHistorySection = new FinancialHistorySection(webDriver);

        financialHistorySection.clickShowButton(withdrawAmount, "VIP PREFERRED");
        String transactionStatus = financialHistorySection.getLastFinancialTransactionStatus();
        assertWithMessage("transaction is incorrect")
                .that(transactionStatus.equalsIgnoreCase("STATUS: Approved"))
                .isTrue();
        String transactionPaymentInstrument = financialHistorySection.getLastFinancialTransactionPaymentInstrument();
        assertWithMessage("transaction is incorrect")
                .that(transactionPaymentInstrument.equalsIgnoreCase("VIP PREFERRED"))
                .isTrue();

    }

    @Test
    @DisplayName("After All test")
    public void setDailyWithdrawalLimitsFinUser(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        homePage.clickUserIcon();

        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("RESPONSIBLE GAMING");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("WITHDRAW LIMITS");

        WithdrawLimitsSection withdrawLimitsSection = new WithdrawLimitsSection(webDriver);

        if (!(withdrawLimitsSection.getDailyWithdrawMaxLimitAmount().equalsIgnoreCase("$4,999.00"))) {
            withdrawLimitsSection.clickChangeWithdrawLimitsButton();

            ChangeWithdrawLimitsSection changeWithdrawLimitsSection = new ChangeWithdrawLimitsSection(webDriver);

            BigDecimal dailyLimitToSetBigDecimal = new BigDecimal(4999);
            String dailyLimitToSetString = dailyLimitToSetBigDecimal.toString();

            changeWithdrawLimitsSection.setDailyLimit(dailyLimitToSetString);

            String newlySetDepositDailyLimit = changeWithdrawLimitsSection.getNewlySetDailyWithdrawLimit();
            changeWithdrawLimitsSection.clickSetLimitsButton()
                    .clickYesSetLimitsButtonInModal();

            Truth.assertThat(changeWithdrawLimitsSection.getSuccessChangeDailyWithdrawLimitValue().compareTo(dailyLimitToSetBigDecimal)).isEqualTo(0);

            changeWithdrawLimitsSection.clickBackToWithdrawalLimitButton();
        }
    }

}
