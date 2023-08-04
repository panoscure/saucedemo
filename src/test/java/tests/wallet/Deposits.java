package tests.wallet;

import base.test.BaseTest;
import canvas.page.objects.account.drawer.Tabs;
import canvas.page.objects.account.drawer.responsible.gaming.ChangeDepositLimitsSection;
import canvas.page.objects.account.drawer.responsible.gaming.DepositLimitsSection;
import canvas.page.objects.account.drawer.wallet.FinancialHistorySection;
import com.google.common.truth.Truth;
import common.utils.Properties;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import canvas.page.objects.HomePage;
import canvas.page.objects.paypal.PayPalCheckoutPage;
import canvas.page.objects.paypal.PayPalLoginPage;
import canvas.page.objects.skrill.SkrillDashboardPage;
import canvas.page.objects.skrill.SkrillLoginPage;
import canvas.page.objects.account.drawer.AccountDrawer;
import canvas.page.objects.account.drawer.DepositSection;
import selenium.WebDriverInit;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.google.common.truth.Truth.assertWithMessage;
import static canvas.helpers.HelperMethods.*;


@ExtendWith(WebDriverInit.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Deposits extends BaseTest {

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
    @DisplayName("QAAUT-1450 :: Deposits | Skrill | Success")
    public void QAAUT$1450$DepositsSkrillSuccess(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickUserIcon();

        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickDepositButton();

        String depositAmount = "13.00";

        DepositSection depositSection = new DepositSection(webDriver);
        BigDecimal depositAmountAsBigDecimal = depositSection.getDepositAmountAsBigDecimal(depositAmount);

        depositSection.clickToSelectPaymentMethodForDeposit("skrill")
                .clickContinueButton()
                .enterDepositAmount(depositAmount)
                .clickDepositButton()
                .clickConfirmButton();

        SkrillLoginPage skrillLoginPage = new SkrillLoginPage(webDriver);
        skrillLoginPage.enterEmail(Properties.getPropertyValue("skrill.email"))
                .enterPassword(Properties.getPropertyValue("skrill.password"))
                .clickLoginButton();

        SkrillDashboardPage skrillDashboardPage = new SkrillDashboardPage(webDriver);
        skrillDashboardPage.clickPayNowButton()
                .clickConfirmButton();

        waitForUrlToBeCanvasUrl(webDriver, Properties.getPropertyValue("canvasUrl"));

        assertWithMessage("deposit is not successful")
                .that(depositSection.getSuccessMessageTitle().equalsIgnoreCase("SUCCESS!"))
                .isTrue();

        Truth.assertThat(depositSection.getSuccessMessage()).isEqualTo("Your request to deposit $"+depositAmount+" has been successfully submitted. You will receive confirmation in your email shortly. Please refresh in order to see your saved payment methods.");

        assertWithMessage("payment method is incorrect")
                .that(depositSection.isPaymentMethodCorrect("skrill"))
                .isTrue();

        BigDecimal availableBalanceAfterDeposit = depositSection.getAvailableBalanceInDepositSectionAsBigDecimal();
        BigDecimal accountBalanceAfterDeposit = homePage.getBalance();
        assertWithMessage("deposit is not successful")
                .that(accountBalanceAfterDeposit)
                .isEqualTo(availableBalanceAfterDeposit);
        assertWithMessage("deposit is not successful")
                .that(accountBalanceAfterDeposit)
                .isEqualTo(accountBalance.add(depositAmountAsBigDecimal));

        depositSection.clickDoneButton();
        accountDrawer.clickBackToMyAccountButton();

        accountDrawer.clickTabInAccountDrawer("WALLET");
        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("TRANSACTION HISTORY");

        FinancialHistorySection financialHistorySection = new FinancialHistorySection(webDriver);
        financialHistorySection.selectFinancialActivitiesSort();
        financialHistorySection.clickShowButton(depositAmount, "SKRILL");

        //status
        String transactionStatus = financialHistorySection.getLastFinancialTransactionStatus();
        assertWithMessage("transaction is incorrect")
                .that(transactionStatus.equalsIgnoreCase("Status: Approved"))
                .isTrue();

        //amount
        String transactionAmount = financialHistorySection.getLastFinancialTransactionAmount();
        assertWithMessage("transaction amount is incorrect")
                .that(transactionAmount)
                .isEqualTo(depositAmount);

        //date-time
        Date date = new Date();
        SimpleDateFormat formatTime = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String today = formatTime.format(date);
        String depositDate = financialHistorySection.getLastFinancialTransactionTime().substring(0, financialHistorySection.getLastFinancialTransactionTime().length()-3);
        assertWithMessage("transaction date is incorrect")
                .that(depositDate)
                .isEqualTo(today);

        //paymentInstrument
        String transactionPaymentInstrument = financialHistorySection.getLastFinancialTransactionPaymentInstrument();
        assertWithMessage("transaction is incorrect")
                .that(transactionPaymentInstrument.equalsIgnoreCase("SKRILL"))
                .isTrue();
    }

    @Test
    @DisplayName("QAAUT-1451 :: Deposits | PayPal | Success")
    public void QAAUT$1451$DepositsPayPalSuccess(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickUserIcon();

        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickDepositButton();

        String depositAmount = "10";

        DepositSection depositSection = new DepositSection(webDriver);
        BigDecimal depositAmountAsBigDecimal = depositSection.getDepositAmountAsBigDecimal(depositAmount);

        depositSection.clickToSelectPaymentMethodForDeposit("paypal")
                .clickContinueButton()
                .enterDepositAmount(depositAmount)
                .clickDepositButton()
                .clickConfirmButton();

        PayPalLoginPage payPalLoginPage = new PayPalLoginPage(webDriver);
        payPalLoginPage.enterEmail(Properties.getPropertyValue("paypal.email"))
                //.clickNextButton()
                .enterPassword(Properties.getPropertyValue("paypal.password"))
                .clickLoginButton();

        PayPalCheckoutPage payPalCheckoutPage = new PayPalCheckoutPage(webDriver);
        payPalCheckoutPage.clickSubmitPaymentButton();

        waitForUrlToBeCanvasUrl(webDriver, Properties.getPropertyValue("canvasUrl"));

        assertWithMessage("deposit is not successful")
                        .that(depositSection.getSuccessMessageTitle().equalsIgnoreCase("SUCCESS!"))
                        .isTrue();

        assertWithMessage("deposit is not successful")
                .that(depositSection.getSuccessMessage().contains("Your request to deposit $"+depositAmount+".00 has been successfully submitted. You will receive confirmation in your email shortly. Please refresh in order to see your saved payment methods."))
                .isTrue();

        assertWithMessage("payment method is incorrect")
                .that(depositSection.isPaymentMethodCorrect("paypal"))
                .isTrue();

        BigDecimal availableBalanceAfterDeposit = depositSection.getAvailableBalanceInDepositSectionAsBigDecimal();
        BigDecimal accountBalanceAfterDeposit = homePage.getBalance();
        assertWithMessage("deposit is not successful")
                .that(accountBalanceAfterDeposit)
                .isEqualTo(availableBalanceAfterDeposit);


        assertWithMessage("deposit is not successful")
                .that(accountBalanceAfterDeposit)
                .isEqualTo(accountBalance.add(depositAmountAsBigDecimal));
        depositSection.clickDoneButton();
    }


    @Test
    @DisplayName("QAAUT-1452 :: Deposits | Credit/Debit Card | Success")
    public void QAAUT$1452$DepositsCreditDebitCardSuccess(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickUserIcon();

        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickDepositButton();

        String depositAmount = "10.00";

        DepositSection depositSection = new DepositSection(webDriver);
        BigDecimal depositAmountAsBigDecimal = depositSection.getDepositAmountAsBigDecimal(depositAmount);

        depositSection.clickToSelectPaymentMethodForDeposit("card7")
                        .clickRadioButton()
                        .clickContinueButton()
                        .enterDepositAmount(depositAmount)
                        .clickDepositButton()
                        .clickConfirmButton();

        assertWithMessage("deposit is not successful")
                .that(depositSection.getSuccessMessageTitle().equalsIgnoreCase("SUCCESS!"))
                .isTrue();

        Truth.assertThat(depositSection.getSuccessMessage()).contains
                ("Your request to deposit $"+depositAmount+" has been successfully submitted. You will receive confirmation in your email shortly. Please refresh in order to see your saved payment methods.");

        assertWithMessage("payment method is incorrect")
                .that(depositSection.isPaymentMethodCorrect("card7"))
                .isTrue();

        BigDecimal availableBalanceAfterDeposit = depositSection.getAvailableBalanceInDepositSectionAsBigDecimal();
        BigDecimal accountBalanceAfterDeposit = homePage.getBalance();
        assertWithMessage("deposit is not successful")
                .that(accountBalanceAfterDeposit)
                .isEqualTo(availableBalanceAfterDeposit);

        assertWithMessage("deposit is not successful")
                .that(accountBalanceAfterDeposit)
                .isEqualTo(accountBalance.add(depositAmountAsBigDecimal));

        depositSection.clickDoneButton();

        accountDrawer.clickBackToMyAccountButton();

        accountDrawer.clickTabInAccountDrawer("WALLET");
        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("TRANSACTION HISTORY");

        FinancialHistorySection financialHistorySection = new FinancialHistorySection(webDriver);
        financialHistorySection.selectFinancialActivitiesSort();
        financialHistorySection.clickShowButton(depositAmount, "CREDIT/DEBIT CARD");

        //status
        String transactionStatus = financialHistorySection.getLastFinancialTransactionStatus();
        assertWithMessage("transaction is incorrect")
                .that(transactionStatus.equalsIgnoreCase("STATUS: Approved"))
                .isTrue();

        //amount
        String transactionAmount = financialHistorySection.getLastFinancialTransactionAmount();
        assertWithMessage("transaction amount is incorrect")
                .that(transactionAmount)
                .isEqualTo(depositAmount);

        //date-time
        Date date = new Date();
        SimpleDateFormat formatTime = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String today = formatTime.format(date);
        String depositDate = financialHistorySection.getLastFinancialTransactionTime().substring(0, financialHistorySection.getLastFinancialTransactionTime().length()-3);
        assertWithMessage("transaction date is incorrect")
                .that(depositDate)
                .isEqualTo(today);

        //paymentInstrument
        String transactionPaymentInstrument = financialHistorySection.getLastFinancialTransactionPaymentInstrument();
        assertWithMessage("transaction is incorrect")
                .that(transactionPaymentInstrument.equalsIgnoreCase("CREDIT/DEBIT CARD"))
                .isTrue();

    }

    @Test
    @DisplayName("QAAUT-1449 :: Deposits | VIP Preferred | Success")
    public void QAAUT$1449$DepositsVIPPreferredSuccess(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickUserIcon();

        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickDepositButton();

        String depositAmount = "10";

        DepositSection depositSection = new DepositSection(webDriver);
        BigDecimal depositAmountAsBigDecimal = depositSection.getDepositAmountAsBigDecimal(depositAmount);

        depositSection.clickToSelectPaymentMethodForDeposit("vip-final")
                .clickRadioButton()
                .clickContinueButton()
                .enterDepositAmount(depositAmount)
                .clickDepositButton()
                .clickConfirmButton();

        assertWithMessage("deposit is not successful")
                .that(depositSection.getSuccessMessageTitle().equalsIgnoreCase("SUCCESS!"))
                .isTrue();

        Truth.assertThat(depositSection.getSuccessMessage()).isEqualTo("Your request to deposit $"+depositAmount+".00 has been successfully submitted. You will receive confirmation in your email shortly. Please refresh in order to see your saved payment methods.");

        assertWithMessage("payment method is incorrect")
                .that(depositSection.isPaymentMethodCorrect("vip-final"))
                .isTrue();

        depositSection.clickDoneButton();

        accountDrawer.clickBackToMyAccountButton()
                .clickTabInAccountDrawer("WALLET");
        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("TRANSACTION HISTORY");

        FinancialHistorySection financialHistorySection = new FinancialHistorySection(webDriver);
        financialHistorySection.selectFinancialActivitiesSort();
        financialHistorySection.clickShowButton(depositAmount, "VIP PREFERRED");

        String transactionPaymentInstrument = financialHistorySection.getLastFinancialTransactionPaymentInstrument();
        assertWithMessage("transaction is incorrect")
                .that(transactionPaymentInstrument.equalsIgnoreCase("VIP PREFERRED"))
                .isTrue();

    }

    @Test
    @DisplayName("After All test")
    public void setDailyDepositLimitsFinUser(WebDriver webDriver) {

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
