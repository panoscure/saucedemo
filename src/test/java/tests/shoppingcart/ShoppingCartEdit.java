package tests.shoppingcart;

import base.test.BaseTest;
import canvas.page.objects.CartDrawer;
import canvas.page.objects.HomePage;
import canvas.page.objects.games.dc.DCGamesManualPlay;
import canvas.page.objects.games.drawer.GamesDrawer;
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
public class ShoppingCartEdit extends BaseTest {
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
    @DisplayName("QAAUT-1642 :: Shopping Cart | Edit a DC4 Straight/Box - Day - Multi Days 2 (add a Quick Pick play)")
    public void QAAUT$1642$ShoppingCartEditDC4StraightBoxDayMultiDays2AddQuickPickPlay(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);

        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-4");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionStraightBox();
        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();

        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        enterNumbersToPlayDCGamesIncremental(webDriver, 3, 4);
        String numbersPlay1 = dcGamesManualPlay.get4NumbersPlayedPerPlay(1);
        dcGamesManualPlay.deselectDrawTime("Day", Properties.getPropertyValue("dc4.game.id"))
                .setMultiDaysTo2();
        String numberOfDaysToBet = dcGamesManualPlay.getNumberOfDays().replace("x ", "");
        //assert number of days per draw time selection
        assertWithMessage("number of days to play game is incorrect")
                .that(numberOfDaysToBet.equalsIgnoreCase("2"))
                .isTrue();

        Integer numberOfGames = dcGamesManualPlay.getYourPlaysList().size();

        BigDecimal multiplier = new BigDecimal(dcGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal numOfDaysToBetAsBigDecimal = new BigDecimal(numberOfDaysToBet);
        BigDecimal numOfGamesAsBigDecimal = new BigDecimal(numberOfGames);
        BigDecimal totalBetCost = multiplier.multiply(numOfDaysToBetAsBigDecimal).multiply(numOfGamesAsBigDecimal);

        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(dcGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();


        dcGamesManualPlay.clickAddToCartButton();

        assertWithMessage("incorrect number of games to cart")
                .that(homePage.numberOfGameInCart())
                .isEqualTo("1");
        homePage.clickCartButton();

        //cart assertions
        CartDrawer cartDrawer = new CartDrawer(webDriver);

        assertWithMessage("cart did not open")
                .that(cartDrawer.isGameCorrect("dc4", 0))
                .isTrue();

        assertWithMessage("incorrect number of games in cart")
                .that(cartDrawer.getNumberOfPlaysInCart())
                .isEqualTo("1");
        assertWithMessage("incorrect type of game in cart")
                .that(cartDrawer.getPlayType(0).equalsIgnoreCase("Standard"))
                .isTrue();

        assertWithMessage("incorrect cost")
                .that(cartDrawer.getPlayPrice())
                .isEqualTo("$" + totalBetCost.toString());
        assertWithMessage("incorrect total")
                .that(cartDrawer.getPlayTotal())
                .isEqualTo("$" + totalBetCost.toString());

        cartDrawer.clickExpandTicketDetailsButton(0);

        String firstGameInCart = cartDrawer.getGameInCart(1);
        assertWithMessage("incorrect game")
                .that(firstGameInCart.contains(numbersPlay1 + "\n" +
                        "Play Type = \n" +
                        "Straight/Box - 24 Ways"))
                .isTrue();

        //edit game in cart
        cartDrawer.clickCartTicketButton("Edit");

        dcGamesManualPlay.clickAddQuickPlayButton();

        numOfGamesAsBigDecimal = new BigDecimal(homePage.numberOfGameInCart());
        totalBetCost = multiplier.add(new BigDecimal("0.50")).multiply(numOfDaysToBetAsBigDecimal).multiply(numOfGamesAsBigDecimal);

        //cart assertions after edit
        assertWithMessage("total bet cost is incorrect")
                .that(dcGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();

        String numbersPlay2 = dcGamesManualPlay.get4NumbersPlayedPerPlay(2);
        dcGamesManualPlay.clickUpdateCartButton();

        assertWithMessage("incorrect number of games to cart")
                .that(homePage.numberOfGameInCart())
                .isEqualTo("1");

        assertWithMessage("incorrect cost")
                .that(cartDrawer.getPlayPrice())
                .isEqualTo("$" + totalBetCost.toString());

        assertWithMessage("incorrect total")
                .that(cartDrawer.getPlayTotal())
                .isEqualTo("$" + totalBetCost.toString());

        assertWithMessage("incorrect game")
                .that(firstGameInCart.contains(numbersPlay1 + "\n" +
                        "Play Type = \n" +
                        "Straight/Box - 24 Ways"))
                .isTrue();

        String secondGameInCart = cartDrawer.getGameInCart(2);

        assertWithMessage("incorrect game")
                .that(secondGameInCart.contains(numbersPlay2 + "\n" +
                        "Play Type = \n" +
                        "Straight"))
                .isTrue();

        homePage.clickUserIcon();
    }

}
