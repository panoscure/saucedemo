package tests.luckyforlife;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.account.drawer.AccountDrawer;
import canvas.page.objects.account.drawer.Tabs;
import canvas.page.objects.account.drawer.wallet.gaminghistory.GamingHistory;
import canvas.page.objects.account.drawer.wallet.gaminghistory.Ticket;
import canvas.page.objects.games.GamePageCommonElements;
import canvas.page.objects.games.LuckyForLifeGamesManualPlay;
import canvas.page.objects.games.QuickPicksTab;
import canvas.page.objects.games.drawer.GamesDrawer;
import canvas.page.objects.modals.PlayGameConfirmationModal;
import canvas.page.objects.modals.ThankYouForPlayingModal;
import common.utils.Properties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import selenium.WebDriverInit;

import java.math.BigDecimal;
import java.util.List;

import static canvas.helpers.HelperMethods.logInCanvas;
import static canvas.helpers.HelperMethods.logOutCanvas;
import static com.google.common.truth.Truth.assertWithMessage;

@ExtendWith(WebDriverInit.class)
public class LuckyForLifeQuickPicks extends BaseTest {

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
    @DisplayName("QAAUT-1551 :: LuckyForLife Game Client | Quick Pick tab | 1 Play $2.00")
    public void QAAUT$1551$LuckyForLifeGameClientQuickPickTab1Play$200(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("LUCKY FOR LIFE");

        GamePageCommonElements gamePageCommonElements = new GamePageCommonElements(webDriver);
        gamePageCommonElements.selectGameEntryTypeTab("Quick Picks");

        String betAmount = "2.00";

        QuickPicksTab quickPicksTab = new QuickPicksTab(webDriver);
        quickPicksTab.quickPicksButton(betAmount).get(0).click();

        //assert quick pick LuckyForLife page elements
        LuckyForLifeGamesManualPlay luckyForLifeGamesManualPlay = new LuckyForLifeGamesManualPlay(webDriver);

        int numberOfGames = luckyForLifeGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        BigDecimal totalBetCost = new BigDecimal(betAmount);
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(luckyForLifeGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        luckyForLifeGamesManualPlay.clickPlayNowButton();

        PlayGameConfirmationModal playGameConfirmationModal = new PlayGameConfirmationModal(webDriver);
        playGameConfirmationModal.clickPurchaseButton();

        ThankYouForPlayingModal thankYouForPlayingModal = new ThankYouForPlayingModal(webDriver);
        String messageTitle = thankYouForPlayingModal.getModalTitle();

        assertWithMessage("bet is not placed")
                .that(messageTitle.equalsIgnoreCase("Thank You"))
                .isTrue();

        thankYouForPlayingModal.clickOkButton();

        BigDecimal accountBalanceAfterBet = homePage.getBalance();
        //assert balance after bet
        assertWithMessage("game is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        homePage.clickUserIcon();
    }

    @Test
    @DisplayName("QAAUT-1552 :: LuckyForLife Game Client | Quick Pick tab | 2 Plays $4.00")
    public void QAAUT$1552$LuckyForLifeGameClientQuickPickTab2Plays$400(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("LUCKY FOR LIFE");

        GamePageCommonElements gamePageCommonElements = new GamePageCommonElements(webDriver);
        gamePageCommonElements.selectGameEntryTypeTab("Quick Picks");

        int numOfGames = 2;
        String bet1Amount = "2.00";
        BigDecimal betAmountAsBigDecimal = (new BigDecimal(numOfGames)).multiply(new BigDecimal(bet1Amount));
        String betAmount = betAmountAsBigDecimal.toString();

        QuickPicksTab quickPicksTab = new QuickPicksTab(webDriver);
        quickPicksTab.quickPicksButton(betAmount).get(0).click();

        //assert quick pick LuckyForLife page elements
        LuckyForLifeGamesManualPlay luckyForLifeGamesManualPlay = new LuckyForLifeGamesManualPlay(webDriver);

        int numberOfGames = luckyForLifeGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(numOfGames);

        BigDecimal totalBetCost = betAmountAsBigDecimal;
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(luckyForLifeGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        luckyForLifeGamesManualPlay.clickPlayNowButton();

        PlayGameConfirmationModal playGameConfirmationModal = new PlayGameConfirmationModal(webDriver);
        playGameConfirmationModal.clickPurchaseButton();

        ThankYouForPlayingModal thankYouForPlayingModal = new ThankYouForPlayingModal(webDriver);
        String messageTitle = thankYouForPlayingModal.getModalTitle();

        assertWithMessage("bet is not placed")
                .that(messageTitle.equalsIgnoreCase("Thank You"))
                .isTrue();

        thankYouForPlayingModal.clickOkButton();

        BigDecimal accountBalanceAfterBet = homePage.getBalance();
        //assert balance after bet
        assertWithMessage("game is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        homePage.clickUserIcon();

    }

    @Test
    @DisplayName("QAAUT-1553 :: LuckyForLife Game Client | Quick Pick tab | 3 Plays $6.00")
    public void QAAUT$1553$LuckyForLifeGameClientQuickPickTab3Plays$600(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("LUCKY FOR LIFE");

        GamePageCommonElements gamePageCommonElements = new GamePageCommonElements(webDriver);
        gamePageCommonElements.selectGameEntryTypeTab("Quick Picks");

        int numOfGames = 3;
        String bet1Amount = "2.00";
        BigDecimal betAmountAsBigDecimal = (new BigDecimal(numOfGames)).multiply(new BigDecimal(bet1Amount));
        String betAmount = betAmountAsBigDecimal.toString();

        QuickPicksTab quickPicksTab = new QuickPicksTab(webDriver);
        quickPicksTab.quickPicksButton(betAmount).get(0).click();

        //assert quick pick LuckyForLife page elements
        LuckyForLifeGamesManualPlay luckyForLifeGamesManualPlay = new LuckyForLifeGamesManualPlay(webDriver);

        int numberOfGames = luckyForLifeGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(numOfGames);

        BigDecimal totalBetCost = betAmountAsBigDecimal;
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(luckyForLifeGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        luckyForLifeGamesManualPlay.clickPlayNowButton();

        PlayGameConfirmationModal playGameConfirmationModal = new PlayGameConfirmationModal(webDriver);
        playGameConfirmationModal.clickPurchaseButton();

        ThankYouForPlayingModal thankYouForPlayingModal = new ThankYouForPlayingModal(webDriver);
        String messageTitle = thankYouForPlayingModal.getModalTitle();

        assertWithMessage("bet is not placed")
                .that(messageTitle.equalsIgnoreCase("Thank You"))
                .isTrue();

        thankYouForPlayingModal.clickOkButton();

        BigDecimal accountBalanceAfterBet = homePage.getBalance();
        //assert balance after bet
        assertWithMessage("game is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        homePage.clickUserIcon();

    }

    @Test
    @DisplayName("QAAUT-1554 :: LuckyForLife Game Client | Quick Pick tab | 5 Plays $10.00")
    public void QAAUT$1554$LuckyForLifeGameClientQuickPickTab5Plays$1000(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("LUCKY FOR LIFE");

        GamePageCommonElements gamePageCommonElements = new GamePageCommonElements(webDriver);
        gamePageCommonElements.selectGameEntryTypeTab("Quick Picks");

        int numOfGames = 5;
        String bet1Amount = "2.00";
        BigDecimal betAmountAsBigDecimal = (new BigDecimal(numOfGames)).multiply(new BigDecimal(bet1Amount));
        String betAmount = betAmountAsBigDecimal.toString();

        QuickPicksTab quickPicksTab = new QuickPicksTab(webDriver);
        quickPicksTab.quickPicksButton(betAmount).get(0).click();

        //assert quick pick LuckyForLife page elements
        LuckyForLifeGamesManualPlay luckyForLifeGamesManualPlay = new LuckyForLifeGamesManualPlay(webDriver);

        int numberOfGames = luckyForLifeGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(numOfGames);

        BigDecimal totalBetCost = betAmountAsBigDecimal;
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(luckyForLifeGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        luckyForLifeGamesManualPlay.clickPlayNowButton();

        PlayGameConfirmationModal playGameConfirmationModal = new PlayGameConfirmationModal(webDriver);
        playGameConfirmationModal.clickPurchaseButton();

        ThankYouForPlayingModal thankYouForPlayingModal = new ThankYouForPlayingModal(webDriver);
        String gameId = thankYouForPlayingModal.getTicketId();
        String messageTitle = thankYouForPlayingModal.getModalTitle();

        assertWithMessage("bet is not placed")
                .that(messageTitle.equalsIgnoreCase("Thank You"))
                .isTrue();

        thankYouForPlayingModal.clickOkButton();

        BigDecimal accountBalanceAfterBet = homePage.getBalance();
        //assert balance after bet
        assertWithMessage("game is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        //gaming history
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.isGameCorrect("lucky-for-life");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOf5Plus1GamesInTheTicket();
        assertWithMessage("incorrect count of bets in ticket")
                .that(ticket.getListOf5Plus1GamesInTheTicket().size())
                .isEqualTo(5);

        for (String game: typeOfGamesInTicket) {
            assertWithMessage("incorrect game")
                    .that(game.contains(
                            "Quick Pick"))
                    .isTrue();
        }

        assertWithMessage("incorrect channel")
                .that(ticket.getTicketChannel().equalsIgnoreCase("Web"))
                .isTrue();

        assertWithMessage("incorrect cost")
                .that(ticket.getTicketCost().equalsIgnoreCase("$" + totalBetCost.toString()))
                .isTrue();

    }

}
