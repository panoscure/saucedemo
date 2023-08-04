package tests.shoppingcart;

import base.test.BaseTest;
import canvas.page.objects.CartDrawer;
import canvas.page.objects.HomePage;
import canvas.page.objects.games.*;
import canvas.page.objects.games.dc.DCGamesManualPlay;
import canvas.page.objects.games.drawer.GamesDrawer;

import canvas.page.objects.modals.JustTheJackpotConfirmationModal;
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
public class ShoppingCartAdd extends BaseTest {
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
    @DisplayName("QAAUT-1621 :: Shopping Cart | Add a DC3 Back Pair-Both-Multiplier $1.0-Quick Pick-Multi Days 2")
    public void QAAUT$1621$ShoppingCartAddDC3BackPairBothMultiplier1QuickPickMultiDays2(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-3");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionBackPairDC3DC5BackThreeDC4()
                .clickToExpandMultiplierDropdown()
                .setMultiplierOptionTo$1();

        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        dcGamesManualPlay.clickQuickPicksButtonUnderNumbersSection()
                .deselectDrawTime("Select All", Properties.getPropertyValue("dc3.game.id"))
                .setMultiDaysTo2();

        String numbersPlay1 = dcGamesManualPlay.getDC3BackPairNumbers(1);

        String numberOfDaysToBet = dcGamesManualPlay.getNumberOfDays().replace("x ", "");

        //num of days
        assertWithMessage("number of days to play game is incorrect")
                .that(numberOfDaysToBet.equalsIgnoreCase("4"))
                .isTrue();

        //num of games
        assertWithMessage("bet is not successful")
                .that(dcGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        BigDecimal multiplier = new BigDecimal(dcGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal numOfDaysToBetAsBigDecimal = new BigDecimal(numberOfDaysToBet);
        BigDecimal expectedTotalCost = new BigDecimal("4.00");
        BigDecimal numOfGamesAsBigDecimal = new BigDecimal(dcGamesManualPlay.getYourPlaysList().size());

        //total price
        BigDecimal gameCostPerDay = multiplier.multiply(numOfGamesAsBigDecimal);
        assertWithMessage("bet is not successful")
                .that(new BigDecimal(dcGamesManualPlay.getDCGamesPrice("DC-3")))
                .isEqualTo(gameCostPerDay);

        BigDecimal totalBetCost = multiplier.multiply(numOfDaysToBetAsBigDecimal).multiply(numOfGamesAsBigDecimal);
        assertWithMessage("total bet cost is incorrect")
                .that(dcGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();

        assertWithMessage("bet is not successful")
                .that(totalBetCost)
                .isEqualTo(expectedTotalCost);

        dcGamesManualPlay.clickAddToCartButton();

        assertWithMessage("incorrect number of games to cart")
                .that(homePage.numberOfGameInCart())
                .isEqualTo("1");
        homePage.clickCartButton();

        CartDrawer cartDrawer = new CartDrawer(webDriver);

        assertWithMessage("cart did not open")
                .that(cartDrawer.isGameCorrect("dc3", 0))
                .isTrue();

        assertWithMessage("incorrect number of games in cart")
                .that(cartDrawer.getNumberOfPlaysInCart())
                .isEqualTo("1");
        assertWithMessage("incorrect type of game in cart")
                .that(cartDrawer.getPlayType(0).equalsIgnoreCase("Quick Pick"))
                .isTrue();

        assertWithMessage("incorrect cost")
                .that(cartDrawer.getPlayPrice())
                .isEqualTo("$" + totalBetCost.toString());
        assertWithMessage("incorrect total")
                .that(cartDrawer.getPlayTotal())
                .isEqualTo("$" + totalBetCost.toString());

        cartDrawer.clickExpandTicketDetailsButton(0);

        assertWithMessage("incorrect game")
                    .that(cartDrawer.getGameInCart(1).contains(numbersPlay1 + "\n" + "Play Type = \n" +
                            "Back Pair"))
                    .isTrue();
    }

    @Test
    @DisplayName("QAAUT-1651 :: Shopping Cart | Add (3) DC5 (1) Front Three-Night-Multiplier $1.00-Multi Days 7 + (1) Straight Manual-Multi Days 7 + (1) Quick Pick-1 Play Straight $1.00-Multi Days 7")
    public void QAAUT$1651$DC5GameClientPlay1DC5GameFrontThreeNightMultiplier1Days7AndStraightManualDays7AndQuickPick1PlayStraight1Days7(WebDriver webDriver) {

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

    }

    @Test
    @DisplayName("QAAUT-1657 :: Shopping Cart | Add a Race2Riches-Exacta-Multi Days 7 game")
    public void QAAUT$1657$ShoppingCartAddRace2RichesExactaMultiDays7Game(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);
        race2RichesGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionExactA();
        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();

        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        race2RichesGamesManualPlay.clickNumberToSelectHorse(0, "12")
                .clickNumberToSelectHorse(1, "3");

        race2RichesGamesManualPlay.setMultiDays(7);
        BigDecimal multiplier = new BigDecimal(race2RichesGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal race2RichesShowTotalCost = new BigDecimal("7.00");
        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(race2RichesGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        String numberOfDaysToBet = race2RichesGamesManualPlay.getNumberOfRaces().replace("x ", "");
        //assert number of days per draw time selection
        assertWithMessage("number of days to play game is incorrect")
                .that(numberOfDaysToBet.equalsIgnoreCase("7"))
                .isTrue();

        BigDecimal numOfGamesAsBigDecimal = new BigDecimal(race2RichesGamesManualPlay.getYourPlaysList().size());
        BigDecimal numOfDaysAsBigDecimal = new BigDecimal(numberOfDaysToBet);
        BigDecimal gameCostPerDay = multiplier.multiply(numOfGamesAsBigDecimal);
        BigDecimal totalBetCost = multiplier.multiply(numOfGamesAsBigDecimal).multiply(numOfDaysAsBigDecimal);
        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        assertWithMessage("bet is not successful")
                .that(new BigDecimal(race2RichesGamesManualPlay.getGamesPrice("Race2Riches")))
                .isEqualTo(gameCostPerDay);

        assertWithMessage("bet is not successful")
                .that(totalBetCost)
                .isEqualTo(race2RichesShowTotalCost);

        race2RichesGamesManualPlay.clickAddToCartButton();

        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(race2RichesGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        assertWithMessage("bet is not successful")
                .that(new BigDecimal(race2RichesGamesManualPlay.getGamesPrice("Race2Riches")))
                .isEqualTo(gameCostPerDay);

        race2RichesGamesManualPlay.clickAddToCartButton();

        assertWithMessage("incorrect number of games to cart")
                .that(homePage.numberOfGameInCart())
                .isEqualTo("1");

        homePage.clickCartButton();

        //cart assertions
        CartDrawer cartDrawer = new CartDrawer(webDriver);

        assertWithMessage("cart did not open")
                .that(cartDrawer.isGameCorrect("race2riches", 0))
                .isTrue();

        //Exacta Standard
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
                .that(firstGameInCart.contains( "12\n" + "3\n" +
                        "Play Type = \n" + "EXACTA"))
                .isTrue();

    }

    @Test
    @DisplayName("QAAUT-1652 :: Shopping Cart | Add a PowerBall-Power Play-Multi Days 2 game")
    public void QAAUT$1652$ShoppingCartAddPowerBallPowerPlayMultiDays2Game(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("POWERBALL");

        select5NumbersToPlay(webDriver, 11);
        PowerBallGamesManualPlay powerBallGamesManualPlay = new PowerBallGamesManualPlay(webDriver);
        powerBallGamesManualPlay.selectOneNumberToPlay("17")
                .setMultiDaysTo2();

        BigDecimal betAmount = new BigDecimal("2.00");

        //assert quick pick PowerBall page elements
        int numberOfGames = powerBallGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        powerBallGamesManualPlay.clickPowerPlayToggle();

        betAmount = betAmount.add(new BigDecimal("1.00"));

        //assert quick pick PowerBall page elements
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

    }

    @Test
    @DisplayName("QAAUT-1653 :: Shopping Cart | Add a MegaMillions-Megaplier-Multi Days 2 game")
    public void QAAUT$1653$ShoppingCartAddMegaMillionsMegaplierMultiDays2Game(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("MEGA MILLIONS");

        select5NumbersToPlay(webDriver, 13);
        MegaMillionsGamesManualPlay megaMillionsGamesManualPlay = new MegaMillionsGamesManualPlay(webDriver);
        megaMillionsGamesManualPlay.selectOneNumberToPlay("1")
                .setMultiDaysTo2();

        BigDecimal betAmount = new BigDecimal("2.00");

        //assert quick pick MegaMillions page elements
        int numberOfGames = megaMillionsGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        megaMillionsGamesManualPlay.clickMegaplierToggle();

        betAmount = betAmount.add(new BigDecimal("1.00"));

        String numberOfDaysToBet = megaMillionsGamesManualPlay.getNumberOfDraws().replace("x ", "");
        BigDecimal numOfGamesAsBigDecimal = new BigDecimal(megaMillionsGamesManualPlay.getYourPlaysList().size());
        BigDecimal numOfDaysAsBigDecimal = new BigDecimal(numberOfDaysToBet);
        BigDecimal totalBetCost = betAmount.multiply(numOfGamesAsBigDecimal).multiply(numOfDaysAsBigDecimal);
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(megaMillionsGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        megaMillionsGamesManualPlay.clickAddToCartButton();

        assertWithMessage("incorrect number of games to cart")
                .that(homePage.numberOfGameInCart())
                .isEqualTo("1");

        homePage.clickCartButton();

        //cart assertions
        CartDrawer cartDrawer = new CartDrawer(webDriver);

        assertWithMessage("cart did not open")
                .that(cartDrawer.isGameCorrect("megamillions.id", 0))
                .isTrue();

        //Megamillions Standard
        assertWithMessage("incorrect number of games in cart")
                .that(cartDrawer.getNumberOfPlaysInCart())
                .isEqualTo("1");
        assertWithMessage("incorrect type of game in cart")
                .that(cartDrawer.getPlayType(0).equalsIgnoreCase("Standard"))
                .isTrue();
        assertWithMessage("incorrect type of game in cart")
                .that(cartDrawer.gameContainsMegaplier().equalsIgnoreCase("Yes"))
                .isTrue();

        assertWithMessage("incorrect type of game in cart")
                .that(cartDrawer.megamillionsContainsJustTheJackPot().equalsIgnoreCase("No"))
                .isTrue();

        assertWithMessage("incorrect cost")
                .that(cartDrawer.getPlayPrice())
                .isEqualTo("$" + totalBetCost.toString());

        cartDrawer.clickExpandTicketDetailsButton(0);

        String firstGameInCart = cartDrawer.getGameInCart(1);

        assertWithMessage("incorrect game")
                .that(firstGameInCart.contains("13\n" +
                        "15\n" +
                        "17\n" +
                        "19\n" +
                        "21\n" +
                        "1"))
                .isTrue();

    }

    @Test
    @DisplayName("QAAUT-1654 :: Shopping Cart | Add a MegaMillions-Just the Jackpot-Multi Days 2 game")
    public void QAAUT$1654$ShoppingCartAddMegaMillionsJustTheJackpotMultiDays2Game(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("MEGA MILLIONS");

        select5NumbersToPlay(webDriver, 13);
        MegaMillionsGamesManualPlay megaMillionsGamesManualPlay = new MegaMillionsGamesManualPlay(webDriver);
        megaMillionsGamesManualPlay.setMultiDaysTo2()
                .selectJustTheJackpot();
        JustTheJackpotConfirmationModal justTheJackpotConfirmationModal = new JustTheJackpotConfirmationModal(webDriver);
        justTheJackpotConfirmationModal.clickButtonInModal("Yes");

        BigDecimal betAmount = new BigDecimal("3.00");

        //assert quick pick MegaMillions page elements
        int numberOfGames = megaMillionsGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        String numberOfDaysToBet = megaMillionsGamesManualPlay.getNumberOfDraws().replace("x ", "");
        BigDecimal numOfGamesAsBigDecimal = new BigDecimal(megaMillionsGamesManualPlay.getYourPlaysList().size());
        BigDecimal numOfDaysAsBigDecimal = new BigDecimal(numberOfDaysToBet);
        BigDecimal totalBetCost = betAmount.multiply(numOfGamesAsBigDecimal).multiply(numOfDaysAsBigDecimal);
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(megaMillionsGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        megaMillionsGamesManualPlay.clickAddToCartButton();

        assertWithMessage("incorrect number of games to cart")
                .that(homePage.numberOfGameInCart())
                .isEqualTo("1");

        homePage.clickCartButton();

        //cart assertions
        CartDrawer cartDrawer = new CartDrawer(webDriver);

        assertWithMessage("cart did not open")
                .that(cartDrawer.isGameCorrect("megamillions.id", 0))
                .isTrue();

        //MegaMillions JustTheJackpot
        assertWithMessage("incorrect number of games in cart")
                .that(cartDrawer.getNumberOfPlaysInCart())
                .isEqualTo("2");
        assertWithMessage("incorrect type of game in cart")
                .that(cartDrawer.getPlayType(0).equalsIgnoreCase("Quick Pick"))
                .isTrue();
        assertWithMessage("incorrect type of game in cart")
                .that(cartDrawer.gameContainsMegaplier().equalsIgnoreCase("No"))
                .isTrue();

        assertWithMessage("incorrect type of game in cart")
                .that(cartDrawer.megamillionsContainsJustTheJackPot().equalsIgnoreCase("Yes"))
                .isTrue();

        assertWithMessage("incorrect cost")
                .that(cartDrawer.getPlayPrice())
                .isEqualTo("$" + totalBetCost.toString());

        cartDrawer.clickExpandTicketDetailsButton(0);

        String firstGameInCart = cartDrawer.getGameInCart(1);
        String secondGameInCart = cartDrawer.getGameInCart(2);

        assertWithMessage("incorrect game")
                .that(!(firstGameInCart.isEmpty()))
                .isTrue();
        assertWithMessage("incorrect game")
                .that(!(secondGameInCart.isEmpty()))
                .isTrue();

    }

    @Test
    @DisplayName("QAAUT-1656 :: Add a LuckyForLife-Multi Days 2 game")
    public void QAAUT$1656$AddLuckyForLifeMultiDays2Game(WebDriver webDriver) {
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

    }
}
