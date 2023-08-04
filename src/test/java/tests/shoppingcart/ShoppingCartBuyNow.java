package tests.shoppingcart;

import base.test.BaseTest;
import canvas.page.objects.CartDrawer;
import canvas.page.objects.HomePage;
import canvas.page.objects.account.drawer.AccountDrawer;
import canvas.page.objects.account.drawer.Tabs;
import canvas.page.objects.account.drawer.wallet.gaminghistory.GamingHistory;
import canvas.page.objects.account.drawer.wallet.gaminghistory.Ticket;
import canvas.page.objects.games.PowerBallGamesManualPlay;
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

import static canvas.helpers.HelperMethods.*;
import static com.google.common.truth.Truth.assertWithMessage;

@ExtendWith(WebDriverInit.class)
public class ShoppingCartBuyNow extends BaseTest {
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
    @DisplayName("QAAUT-1666 :: Shopping Cart | Play a PowerBall-Power Play-Multi Days 7 game")
    public void QAAUT$1666$ShoppingCartPlayPowerBallPowerPlayMultiDays7Game(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("POWERBALL");

        select5NumbersToPlay(webDriver, 11);
        PowerBallGamesManualPlay powerBallGamesManualPlay = new PowerBallGamesManualPlay(webDriver);
        powerBallGamesManualPlay.selectOneNumberToPlay("17")
                .setMultiDaysTo12();

        BigDecimal betAmount = new BigDecimal("2.00");

        int numberOfGames = powerBallGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        powerBallGamesManualPlay.clickPowerPlayToggle();

        betAmount = betAmount.add(new BigDecimal("1.00"));

        String numberOfDaysToBet = powerBallGamesManualPlay.getNumberOfDraws().replace("x ", "");
        BigDecimal numOfGamesAsBigDecimal = new BigDecimal(powerBallGamesManualPlay.getYourPlaysList().size());
        BigDecimal numOfDaysAsBigDecimal = new BigDecimal(numberOfDaysToBet);
        BigDecimal totalBetCost = betAmount.multiply(numOfGamesAsBigDecimal).multiply(numOfDaysAsBigDecimal);
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(powerBallGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        powerBallGamesManualPlay.clickAddToCartButton();

        assertWithMessage("incorrect number of games to cart")
                .that(homePage.numberOfGameInCart())
                .isEqualTo("1");

        homePage.clickCartButton();

        //cart assertions
        CartDrawer cartDrawer = new CartDrawer(webDriver);

        assertWithMessage("cart did not open")
                .that(cartDrawer.isGameCorrect("pb", 0))
                .isTrue();

        //PowerBall Standard
        assertWithMessage("incorrect number of games in cart")
                .that(cartDrawer.getNumberOfPlaysInCart())
                .isEqualTo("1");
        assertWithMessage("incorrect type of game in cart")
                .that(cartDrawer.getPlayType(0).equalsIgnoreCase("Standard"))
                .isTrue();
        assertWithMessage("incorrect type of game in cart")
                .that(cartDrawer.gameContainsPowerPlay().equalsIgnoreCase("Yes"))
                .isTrue();

        assertWithMessage("incorrect cost")
                .that(cartDrawer.getPlayPrice())
                .isEqualTo("$" + totalBetCost.toString());

        cartDrawer.clickExpandTicketDetailsButton(0);

        String firstGameInCart = cartDrawer.getGameInCart(1);

        assertWithMessage("incorrect game")
                .that(firstGameInCart.contains( "11\n" +
                        "13\n" +
                        "15\n" +
                        "17\n" +
                        "19\n" +
                        "17"))
                .isTrue();


        //play game
        cartDrawer.clickCartTicketButton("BUY NOW");

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
        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        //gaming history
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.isGameCorrect("pb");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOf5Plus1GamesInTheTicket();

        for (String game: typeOfGamesInTicket) {
            assertWithMessage("incorrect game")
                    .that(game.equalsIgnoreCase("11\n" +
                            "13\n" +
                            "15\n" +
                            "17\n" +
                            "19\n" +
                            "17\n" +
                            "Standard"))
                    .isTrue();
        }

        assertWithMessage("incorrect channel")
                .that(ticket.getTicketChannel().equalsIgnoreCase("Web"))
                .isTrue();

        assertWithMessage("incorrect type")
                .that(ticket.getTicketType().equalsIgnoreCase("Standard"))
                .isTrue();

        assertWithMessage("incorrect count of boards")
                .that(ticket.getNumberOfBoardsInGame())
                .isEqualTo("1");


        assertWithMessage("incorrect cost")
                .that(ticket.getTicketCost().equalsIgnoreCase("$" + totalBetCost.toString()))
                .isTrue();

    }



}
