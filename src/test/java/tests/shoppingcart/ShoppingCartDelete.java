package tests.shoppingcart;

import base.test.BaseTest;
import canvas.page.objects.CartDrawer;
import canvas.page.objects.HomePage;
import canvas.page.objects.games.*;
import canvas.page.objects.games.dc.DCGamesManualPlay;
import canvas.page.objects.games.drawer.GamesDrawer;
import canvas.page.objects.modals.DeleteGameFromCartConfirmationModal;
import common.utils.Properties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import selenium.WebDriverInit;

import java.math.BigDecimal;

import static canvas.helpers.HelperMethods.*;
import static com.google.common.truth.Truth.assertWithMessage;

@ExtendWith(WebDriverInit.class)
public class ShoppingCartDelete extends BaseTest {
    public String username = Properties.getPropertyValue("canvas.user");
    public String password = Properties.getPropertyValue("canvas.password");


    @BeforeEach
    public void setUp(WebDriver webDriver) {
        logInCanvas(webDriver, canvasUrl, username, password);
    }

    @AfterEach
    public void tearDown(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        homePage.clickUserIcon();
        logOutCanvas(webDriver);
    }



    @Test
    @DisplayName("QAAUT-1667 :: Shopping Cart | Add (3) DC5 Games and Delete them all")
    public void QAAUT$1667$ShoppingCartAdd3DC5GamesAndDeleteThemAll(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-5");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        //(1) Front Three-Night-Multiplier $1.00- Multi Days 7
        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionComboDC3FrontThreeDC5()
                .clickToExpandMultiplierDropdown()
                .setMultiplierOptionTo$1();
        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();

        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        dcGamesManualPlay.enterNumberToPlay(0, "3")
                .enterNumberToPlay(1, "9")
                .enterNumberToPlay(2, "9")
                .deselectDrawTime("Evening", Properties.getPropertyValue("dc5.game.id"))
                .setMultiDaysTo7();
        String playedNumbers1 = dcGamesManualPlay.get3NumbersPlayedPerPlay(1);

        String numberOfDaysToBet1 = dcGamesManualPlay.getNumberOfDays().replace("x ", "");
        //assert number of days per draw time selection
        assertWithMessage("number of days to play game is incorrect")
                .that(numberOfDaysToBet1.equalsIgnoreCase("7"))
                .isTrue();

        BigDecimal multiplier1 = new BigDecimal(dcGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal numOfDaysToBetAsBigDecimal1 = new BigDecimal(numberOfDaysToBet1);
        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(dcGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        BigDecimal numOfGamesAsBigDecimal1 = new BigDecimal(dcGamesManualPlay.getYourPlaysList().size());
        BigDecimal gameCostPerDay1 = multiplier1.multiply(numOfGamesAsBigDecimal1);
        BigDecimal totalBetCost1 = multiplier1.multiply(numOfDaysToBetAsBigDecimal1).multiply(numOfGamesAsBigDecimal1);
        assertWithMessage("total bet cost is incorrect")
                .that(dcGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost1.toString()))
                .isTrue();
        assertWithMessage("bet is not successful")
                .that(new BigDecimal(dcGamesManualPlay.getDCGamesPrice("DC-5")))
                .isEqualTo(gameCostPerDay1);

        dcGamesManualPlay.clickAddToCartButton();
        assertWithMessage("incorrect number of games to cart")
                .that(homePage.numberOfGameInCart())
                .isEqualTo("1");

        //(1) Straight Manual-Multi Days 7
        enterNumbersToPlayDCGamesIncremental(webDriver, 5,5);
        dcGamesManualPlay.setMultiDaysTo7();

        String playedNumbers2 = dcGamesManualPlay.get5NumbersPlayedPerPlay(1);

        String numberOfDaysToBet2 = dcGamesManualPlay.getNumberOfDays().replace("x ", "");
        //assert number of days per draw time selection
        assertWithMessage("number of days to play game is incorrect")
                .that(numberOfDaysToBet2.equalsIgnoreCase("14"))
                .isTrue();

        BigDecimal multiplier2 = new BigDecimal(dcGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal numOfDaysToBetAsBigDecimal2 = new BigDecimal(numberOfDaysToBet2);

        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(dcGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        BigDecimal numOfGamesAsBigDecimal2 = new BigDecimal(dcGamesManualPlay.getYourPlaysList().size());
        BigDecimal gameCostPerDay2 = multiplier2.multiply(numOfGamesAsBigDecimal2);
        BigDecimal totalBetCost2 = multiplier2.multiply(numOfDaysToBetAsBigDecimal2).multiply(numOfGamesAsBigDecimal2);
        assertWithMessage("total bet cost is incorrect")
                .that(dcGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost2.toString()))
                .isTrue();
        assertWithMessage("bet is not successful")
                .that(new BigDecimal(dcGamesManualPlay.getDCGamesPrice("DC-5")))
                .isEqualTo(gameCostPerDay2);
        dcGamesManualPlay.clickAddToCartButton();

        assertWithMessage("incorrect number of games to cart")
                .that(homePage.numberOfGameInCart())
                .isEqualTo("2");

        //(1) Quick Pick-1 Play Straight $1.00-Multi Days 7
        GamePageCommonElements gamePageCommonElements = new GamePageCommonElements(webDriver);
        gamePageCommonElements.selectGameEntryTypeTab("Quick Picks");
        QuickPicksTab quickPicksTab = new QuickPicksTab(webDriver);
        quickPicksTab.quickPicksButton("$1.00").get(0).click();
        quickPicksTab.setMultiDaysTo7();

        String playedNumbers3 = quickPicksTab.get5NumbersPlayedPerPlay(1);
        String numberOfDaysToBet3 = dcGamesManualPlay.getNumberOfDays().replace("x ", "");
        //assert number of days per draw time selection
        assertWithMessage("number of days to play game is incorrect")
                .that(numberOfDaysToBet3.equalsIgnoreCase("7"))
                .isTrue();

        BigDecimal multiplier3 = new BigDecimal(dcGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal numOfDaysToBetAsBigDecimal3 = new BigDecimal(numberOfDaysToBet3);

        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(dcGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        BigDecimal numOfGamesAsBigDecimal3 = new BigDecimal(dcGamesManualPlay.getYourPlaysList().size());
        BigDecimal gameCostPerDay3 = multiplier3.multiply(numOfGamesAsBigDecimal3);
        BigDecimal totalBetCost3 = multiplier3.multiply(numOfDaysToBetAsBigDecimal3).multiply(numOfGamesAsBigDecimal3);
        assertWithMessage("total bet cost is incorrect")
                .that(dcGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost3.toString()))
                .isTrue();
        assertWithMessage("bet is not successful")
                .that(new BigDecimal(dcGamesManualPlay.getDCGamesPrice("DC-5")))
                .isEqualTo(gameCostPerDay3);

        dcGamesManualPlay.clickAddToCartButton();

        assertWithMessage("incorrect number of games to cart")
                .that(homePage.numberOfGameInCart())
                .isEqualTo("3");

        homePage.clickCartButton();

        //cart assertions
        CartDrawer cartDrawer = new CartDrawer(webDriver);

        assertWithMessage("cart did not open")
                .that(cartDrawer.isGameCorrect("dc5", 0))
                .isTrue();

        //Front Three Standard
        assertWithMessage("incorrect number of games in cart")
                .that(cartDrawer.getNumberOfPlaysInCart())
                .isEqualTo("1");
        assertWithMessage("incorrect type of game in cart")
                .that(cartDrawer.getPlayType(0).equalsIgnoreCase("Standard"))
                .isTrue();

        assertWithMessage("incorrect cost")
                .that(cartDrawer.getPlayPrice())
                .isEqualTo("$" + totalBetCost1.toString());

        cartDrawer.clickExpandTicketDetailsButton(0);

        String firstGameInCart = cartDrawer.getGameInCart(1);

        assertWithMessage("incorrect game")
                .that(firstGameInCart.contains(playedNumbers1 + "\n" + "-\n" +
                        "-\n" +
                        "Play Type = \n" +
                        "Front Three"))
                .isTrue();
        cartDrawer.clickCollapseTicketDetailsButton();

        //Straight Standard
        assertWithMessage("incorrect number of games in cart")
                .that(cartDrawer.getNumberOfPlaysInCart())
                .isEqualTo("1");
        assertWithMessage("incorrect type of game in cart")
                .that(cartDrawer.getPlayType(1).equalsIgnoreCase("Standard"))
                .isTrue();

        assertWithMessage("incorrect cost")
                .that(cartDrawer.getPlayPrice())
                .isEqualTo("$" + totalBetCost2.toString());

        cartDrawer.clickExpandTicketDetailsButton(1);

        String secondGameInCart = cartDrawer.getGameInCart(2);

        assertWithMessage("incorrect game")
                .that(secondGameInCart.contains(playedNumbers2 + "\n" +
                        "Play Type = \n" + "Straight"))
                .isTrue();
        cartDrawer.clickCollapseTicketDetailsButton();

        //Quick Picks
        assertWithMessage("incorrect number of games in cart")
                .that(cartDrawer.getNumberOfPlaysInCart())
                .isEqualTo("1");
        assertWithMessage("incorrect type of game in cart")
                .that(cartDrawer.getPlayType(2).equalsIgnoreCase("Quick Pick"))
                .isTrue();

        assertWithMessage("incorrect cost")
                .that(cartDrawer.getPlayPrice())
                .isEqualTo("$" + totalBetCost3.toString());

        cartDrawer.clickExpandTicketDetailsButton(2);

        String thirdGameInCart = cartDrawer.getGameInCart(3);

        assertWithMessage("incorrect game")
                .that(thirdGameInCart.contains(playedNumbers3 + "\n" +
                        "Play Type = \n" + "Straight"))
                .isTrue();
        cartDrawer.clickCollapseTicketDetailsButton();

        //all three bets cost
        BigDecimal totalBetCost = totalBetCost1.add(totalBetCost2).add(totalBetCost3);
        assertWithMessage("incorrect total")
                .that(cartDrawer.getPlayTotal())
                .isEqualTo("$" + totalBetCost.toString());

        //delete all
        cartDrawer.clickCartButton("Delete All");
        DeleteGameFromCartConfirmationModal deleteGameFromCartConfirmationModal = new DeleteGameFromCartConfirmationModal(webDriver);
        deleteGameFromCartConfirmationModal.clickButtonInModal("Yes");

        assertWithMessage("cart is not empty")
                .that(cartDrawer.getEmptyCartText().equalsIgnoreCase("Your cart is empty"))
                .isTrue();

    }

    @Test
    @DisplayName("QAAUT-1668 :: Add a LuckyForLife-Multi Days 2 game and Delete it")
    public void QAAUT$1668$AddLuckyForLifeMultiDays2GameAndDeleteIt(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("LUCKY FOR LIFE");

        select5NumbersToPlay(webDriver, 6);
        LuckyForLifeGamesManualPlay luckyForLifeGamesManualPlay = new LuckyForLifeGamesManualPlay(webDriver);
        luckyForLifeGamesManualPlay.selectOneNumberToPlay("8")
                .setMultiDaysTo2();

        BigDecimal betAmount = new BigDecimal("2.00");
        int numberOfGames = luckyForLifeGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        String numberOfDaysToBet = luckyForLifeGamesManualPlay.getNumberOfDraws().replace("x ", "");
        BigDecimal numOfGamesAsBigDecimal = new BigDecimal(luckyForLifeGamesManualPlay.getYourPlaysList().size());
        BigDecimal numOfDaysAsBigDecimal = new BigDecimal(numberOfDaysToBet);
        BigDecimal totalBetCost = betAmount.multiply(numOfGamesAsBigDecimal).multiply(numOfDaysAsBigDecimal);
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(luckyForLifeGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        luckyForLifeGamesManualPlay.clickAddToCartButton();

        assertWithMessage("incorrect number of games to cart")
                .that(homePage.numberOfGameInCart())
                .isEqualTo("1");

        homePage.clickCartButton();

        //cart assertions
        CartDrawer cartDrawer = new CartDrawer(webDriver);

        assertWithMessage("cart did not open")
                .that(cartDrawer.isGameCorrect("lucky-for-life", 0))
                .isTrue();

        //LuckyForLife
        assertWithMessage("incorrect number of games in cart")
                .that(cartDrawer.getNumberOfPlaysInCart())
                .isEqualTo("1");
        assertWithMessage("incorrect type of game in cart")
                .that(cartDrawer.getPlayType(0).equalsIgnoreCase("Standard"))
                .isTrue();

        assertWithMessage("incorrect cost")
                .that(cartDrawer.getPlayPrice())
                .isEqualTo("$" + totalBetCost.toString());

        cartDrawer.clickExpandTicketDetailsButton(0);

        String firstGameInCart = cartDrawer.getGameInCart(1);

        assertWithMessage("incorrect game")
                .that(firstGameInCart.contains("6\n" +
                        "8\n" +
                        "10\n" +
                        "12\n" +
                        "14\n" +
                        "8"))
                .isTrue();

        //delete
        cartDrawer.clickCartTicketButton("DELETE");
        DeleteGameFromCartConfirmationModal deleteGameFromCartConfirmationModal = new DeleteGameFromCartConfirmationModal(webDriver);
        deleteGameFromCartConfirmationModal.clickButtonInModal("Yes");

        assertWithMessage("cart is not empty")
                .that(cartDrawer.getEmptyCartText().equalsIgnoreCase("Your cart is empty"))
                .isTrue();


    }

}
