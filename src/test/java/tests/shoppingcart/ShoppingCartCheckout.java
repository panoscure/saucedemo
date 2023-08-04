package tests.shoppingcart;

import base.test.BaseTest;
import canvas.page.objects.CartDrawer;
import canvas.page.objects.HomePage;
import canvas.page.objects.account.drawer.AccountDrawer;
import canvas.page.objects.account.drawer.Tabs;
import canvas.page.objects.account.drawer.wallet.gaminghistory.GamingHistory;
import canvas.page.objects.account.drawer.wallet.gaminghistory.Ticket;
import canvas.page.objects.games.MegaMillionsGamesManualPlay;
import canvas.page.objects.games.PowerBallGamesManualPlay;
import canvas.page.objects.games.drawer.GamesDrawer;
import canvas.page.objects.modals.PlayGameConfirmationModal;
import common.utils.Properties;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import selenium.WebDriverInit;

import java.math.BigDecimal;
import java.util.List;

import static canvas.helpers.HelperMethods.*;
import static com.google.common.truth.Truth.assertWithMessage;

@ExtendWith(WebDriverInit.class)
public class ShoppingCartCheckout extends BaseTest {
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
    @DisplayName("QAAUT-1669 :: Shopping Cart | Checkout button functionality")
    public void QAAUT$1669$ShoppingCartCheckoutButtonFunctionality(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);

        //powerball game
        gamesDrawer.clickBuyNowButtonInGamesDrawer("POWERBALL");

        select5NumbersToPlay(webDriver, 11);
        PowerBallGamesManualPlay powerBallGamesManualPlay = new PowerBallGamesManualPlay(webDriver);
        powerBallGamesManualPlay.selectOneNumberToPlay("17")
                .setMultiDaysTo12();

        BigDecimal powerBallBetAmount = new BigDecimal("2.00");

        int numberOfGames = powerBallGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        powerBallGamesManualPlay.clickPowerPlayToggle();

        powerBallBetAmount = powerBallBetAmount.add(new BigDecimal("1.00"));

        String numberOfDaysToBet = powerBallGamesManualPlay.getNumberOfDraws().replace("x ", "");
        BigDecimal numOfGamesAsBigDecimal = new BigDecimal(powerBallGamesManualPlay.getYourPlaysList().size());
        BigDecimal numOfDaysAsBigDecimal = new BigDecimal(numberOfDaysToBet);
        BigDecimal powerBallTotalBetCost = powerBallBetAmount.multiply(numOfGamesAsBigDecimal).multiply(numOfDaysAsBigDecimal);
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(powerBallGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(powerBallTotalBetCost.toString()))
                .isTrue();

        powerBallGamesManualPlay.clickAddToCartButton();

        assertWithMessage("incorrect number of games to cart")
                .that(homePage.numberOfGameInCart())
                .isEqualTo("1");

        //MegaMillions game
        homePage.clickLinkInTopMenu("GAMES");
        gamesDrawer.clickBuyNowButtonInGamesDrawer("MEGA MILLIONS");

        MegaMillionsGamesManualPlay megaMillionsGamesManualPlay = new MegaMillionsGamesManualPlay(webDriver);
        assertWithMessage("incorrect page game")
                .that(megaMillionsGamesManualPlay.isGamePageCorrect("megamillions"))
                .isTrue();

        select5NumbersToPlay(webDriver, 6);
        megaMillionsGamesManualPlay.selectOneNumberToPlay("8");

        BigDecimal megaMillionsBetAmount = new BigDecimal("2.00");

        int numberOfGames2 = megaMillionsGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames2)
                .isEqualTo(1);

        BigDecimal megaMillionsTotalBetCost = megaMillionsBetAmount;

        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(megaMillionsGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(megaMillionsTotalBetCost.toString()))
                .isTrue();

        powerBallGamesManualPlay.clickAddToCartButton();

        assertWithMessage("incorrect number of games to cart")
                .that(homePage.numberOfGameInCart())
                .isEqualTo("2");

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
                .isEqualTo("$" + powerBallTotalBetCost.toString());

        cartDrawer.clickExpandTicketDetailsButton(0);

        String firstGameInCart = cartDrawer.getGameInCart(1);

        assertWithMessage("incorrect game")
                .that(firstGameInCart.contains("11\n" +
                        "13\n" +
                        "15\n" +
                        "17\n" +
                        "19\n" +
                        "17"))
                .isTrue();

        cartDrawer.clickCollapseTicketDetailsButton();

        //Megamillions Standard
        assertWithMessage("cart did not open")
                .that(cartDrawer.isGameCorrect("megamillions", 0))
                .isTrue();

        assertWithMessage("incorrect number of games in cart")
                .that(cartDrawer.getNumberOfPlaysInCart())
                .isEqualTo("1");
        assertWithMessage("incorrect type of game in cart")
                .that(cartDrawer.getPlayType(0).equalsIgnoreCase("Standard"))
                .isTrue();
        assertWithMessage("incorrect type of game in cart")
                .that(cartDrawer.gameContainsMegaplier().equalsIgnoreCase("No"))
                .isTrue();

        assertWithMessage("incorrect type of game in cart")
                .that(cartDrawer.megamillionsContainsJustTheJackPot().equalsIgnoreCase("No"))
                .isTrue();

        assertWithMessage("incorrect cost")
                .that(cartDrawer.getPlayPrice())
                .isEqualTo("$" + powerBallTotalBetCost.toString());

        cartDrawer.clickExpandTicketDetailsButton(1);

        String secondGameInCart = cartDrawer.getGameInCart(2);
        assertWithMessage("incorrect game")
                .that(secondGameInCart.contains("6\n" +
                        "8\n" +
                        "10\n" +
                        "12\n" +
                        "14\n" +
                        "8"))
                .isTrue();


        //play game
        cartDrawer.clickCartButton("BUY ALL TICKETS IN THE CART");

        PlayGameConfirmationModal playGameConfirmationModal = new PlayGameConfirmationModal(webDriver);
        playGameConfirmationModal.clickPurchaseButton();

        BigDecimal accountBalanceAfterBet = homePage.getBalance();

        //assert balance after bet
        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(powerBallTotalBetCost).add(megaMillionsTotalBetCost));

        assertWithMessage("total cost of ticket played is incorrect")
                .that(cartDrawer.getGamedPlayedTotalCostInCart())
                .isEqualTo((powerBallTotalBetCost).add(megaMillionsTotalBetCost).toString());

        //powerball
        assertWithMessage("incorrect multi draws count")
                .that(cartDrawer.getTicketPlayedMultiDrawsInCart(0))
                .isEqualTo("12");

        String powerBallId = cartDrawer.getTicketIdPlayedInCart(0);

        assertWithMessage("incorrect bet cost")
                .that(cartDrawer.getGamedPlayedTotalCostInCart())
                .isEqualTo(powerBallTotalBetCost.add(megaMillionsTotalBetCost).toString());

        //megamillions
        assertWithMessage("incorrect multi draws count")
                .that(cartDrawer.getTicketPlayedMultiDrawsInCart(1))
                .isEqualTo("1");

        String megaMillionsId = cartDrawer.getTicketIdPlayedInCart(1);


        //gaming history
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.isGameCorrect("megamillions");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(megaMillionsId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOf5Plus1GamesInTheTicket();
        assertWithMessage("incorrect count of bets in ticket")
                .that(ticket.getListOf5Plus1GamesInTheTicket().size())
                .isEqualTo(1);

        for (String game : typeOfGamesInTicket) {
            assertWithMessage("incorrect game")
                    .that(game.contains("6\n" +
                            "8\n" +
                            "10\n" +
                            "12\n" +
                            "14\n" +
                            "8\n" +
                            "Standard"))
                    .isTrue();
        }

        ticket.clickReturnToListButton();

        gamingHistory.isGameCorrect("pb");
        gamingHistory.clickTheSecondFinancialTransaction();

        assertWithMessage("incorrect gameId")
                .that(powerBallId)
                .isEqualTo(ticket.getTicketId());

        assertWithMessage("incorrect count of bets in ticket")
                .that(ticket.getListOf5Plus1GamesInTheTicket().size())
                .isEqualTo(1);

        typeOfGamesInTicket = ticket.getListOf5Plus1GamesInTheTicket();

        for (String game : typeOfGamesInTicket) {

            assertWithMessage("incorrect game")
                    .that(game.contains("11\n" +
                            "13\n" +
                            "15\n" +
                            "17\n" +
                            "19\n" +
                            "17\n" +
                            "Standard"))
                    .isTrue();
        }




    }
}
