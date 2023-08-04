package canvas.helpers;

import canvas.page.objects.account.drawer.AccountDrawer;
import canvas.page.objects.account.drawer.preferences.myfavorites.MyFavoriteCoupons;
import canvas.page.objects.account.drawer.preferences.myfavorites.MyFavoriteNumbers;
import canvas.page.objects.games.LuckyForLifeGamesManualPlay;
import canvas.page.objects.games.MegaMillionsGamesManualPlay;
import canvas.page.objects.games.dc.DCGamesManualPlay;
import canvas.page.objects.modals.*;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import canvas.page.objects.HomePage;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HelperMethods {

    WebDriver webDriver;

    public HelperMethods(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public static void logInCanvas(WebDriver webDriver, String canvasUrl,String username, String password) {
        webDriver.get(canvasUrl);
        HomePage homePage = new HomePage(webDriver);
        homePage.clickLoginButton();
        logIn(webDriver, username, password);
    }

    public static void logIn(WebDriver webDriver, String username, String password) {
        AccountLoginModal accountLoginModal = new AccountLoginModal(webDriver);
        accountLoginModal.enterValueInInputField("username", username)
                .enterValueInInputField("password", password)
                .clickLoginButton();
    }

    public static void waitForUserToLogOut(WebDriver webDriver) {

        Awaitility
                .await()
                .pollInterval(2, TimeUnit.SECONDS)
                .atMost(20, TimeUnit.SECONDS)
                .until(() ->
                {
                    WebElement loginBtn = SeleniumWaits.elementToBeClickable(webDriver, By.xpath("//button[text()='Login']"));
                    return loginBtn.isDisplayed();
                });

    }

    public static void logOutCanvas(WebDriver webDriver) {
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickLogoutButton();
    }

    public static void waitForUrlToBeCanvasUrl(WebDriver webDriver, String canvasUrl) {
        Awaitility
                .await()
                .pollInterval(2, TimeUnit.SECONDS)
                .atMost(20, TimeUnit.SECONDS)
                .until(() ->
                {
                    String currentUrl = webDriver.getCurrentUrl();
                    return currentUrl.contains(canvasUrl);
                });

    }

    public static void enterNumbersToPlayDCGamesIncremental(WebDriver webDriver, Integer num, int index) {
        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        for (int i = 0; i < index; i++) {
            dcGamesManualPlay.enterNumberToPlay(i, num.toString());
            num++;
        }
    }

    public static void select5NumbersToPlay(WebDriver webDriver, Integer num) {
        LuckyForLifeGamesManualPlay luckyForLifeGamesManualPlay = new LuckyForLifeGamesManualPlay(webDriver);
        for (Integer i = num;  i < num + 10; i+=2) {
            luckyForLifeGamesManualPlay.selectFiveNumbersToPlay(i.toString());
        }
    }

    public static void select5SequentialNumbersToPlay(WebDriver webDriver, Integer num) {
        LuckyForLifeGamesManualPlay luckyForLifeGamesManualPlay = new LuckyForLifeGamesManualPlay(webDriver);
        for (Integer i = num;  i < num + 5; i++) {
            luckyForLifeGamesManualPlay.selectFiveNumbersToPlay(i.toString());
        }
    }

    public static List<String> getNumbersPlayedPerGame(WebDriver webDriver, Integer num) {
        MegaMillionsGamesManualPlay megaMillionsGamesManualPlay = new MegaMillionsGamesManualPlay(webDriver);
        List<String> numbersPlayed = new ArrayList<>();
        for (int i = num;  i <= num + 5; i++) {
            String number = megaMillionsGamesManualPlay.getNumbersPlayed().get(i);
            numbersPlayed.add(number);
        }
        return numbersPlayed;
    }

    public static List<String> getNumbersPlayed(WebDriver webDriver, Integer num, Integer countNumMinus1) {
        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        List<String> numbersPlayed = new ArrayList<>();
        for (int i = num;  i <= num + countNumMinus1; i++) {
            String number = dcGamesManualPlay.getNumbersPlayed().get(i);
            numbersPlayed.add(number);
        }
        return numbersPlayed;
    }

    public static String getLuckySum(List<String> numbersPlayed) {
        Integer count = 0;
        for (String num:numbersPlayed){
            Integer numInList = Integer.valueOf(num);
            count+=numInList;

        }
        return count.toString();
    }

    public static List<String> getNumbersPlayedPerGameInConfirmationModal(WebDriver webDriver, Integer startIndex, Integer countNumMinus1) {
        PlayGameConfirmationModal playGameConfirmationModal = new PlayGameConfirmationModal(webDriver);
        List<String> numbersPlayed = new ArrayList<>();
        for (int i = startIndex;  i <= startIndex + countNumMinus1; i++) {
            String number = playGameConfirmationModal.getListOfNumbersPlayed().get(i);
            numbersPlayed.add(number);
        }
        return numbersPlayed;
    }


    public static List<String> getNumbersPlayedPerGameInThankYouModal(WebDriver webDriver, Integer startIndex, Integer countNumMinus1) {
        ThankYouForPlayingModal thankYouForPlayingModal = new ThankYouForPlayingModal(webDriver);
        List<String> numbersPlayed = new ArrayList<>();
        for (int i = startIndex;  i <= startIndex + countNumMinus1; i++) {
            String number = thankYouForPlayingModal.getListOfNumbersPlayed().get(i);
            numbersPlayed.add(number);
        }
        return numbersPlayed;
    }

    public static List<String> getNumbersPlayedPerGameInTicketCheckerModal(WebDriver webDriver, Integer startIndex, Integer countNumMinus1) {
        TicketCheckerModal ticketCheckerModal = new TicketCheckerModal(webDriver);
        List<String> numbersPlayed = new ArrayList<>();
        for (int i = startIndex;  i <= startIndex + countNumMinus1; i++) {
            String number = ticketCheckerModal.getListOfNumbersPlayed().get(i);
            numbersPlayed.add(number);
        }
        return numbersPlayed;
    }

    public static void addFavoriteNumbers(WebDriver webDriver, Integer num) {
        MyFavoriteNumbers myFavoriteNumbers = new MyFavoriteNumbers(webDriver);
        for (Integer i = 0;  i <= num; i++) {
            myFavoriteNumbers.enterFavoriteNumber(String.valueOf(i));
            myFavoriteNumbers.clickAddNumberButton();
        }
    }

    public static void deleteFavoriteNumbers(WebDriver webDriver, Integer num) {
        for (Integer i = 0;  i < num; i++) {
            MyFavoriteNumbers myFavoriteNumbers = new MyFavoriteNumbers(webDriver);
            myFavoriteNumbers.deleteFavoriteNumber();
            ConfirmationModal confirmationModal = new ConfirmationModal(webDriver);
            confirmationModal.clickButtonInModal("Yes");
        }
    }



    public static void deleteFavoriteCoupon(WebDriver webDriver) {
        MyFavoriteCoupons myFavoriteCoupons = new MyFavoriteCoupons(webDriver);
        Awaitility
                .await()
                .pollInterval(2, TimeUnit.SECONDS)
                .atMost(5, TimeUnit.SECONDS)
                .until(() ->
                {
                    WebElement btn = SeleniumWaits.visibilityOfElementLocated(webDriver,
                            By.xpath("//div[@class='favorites']//button[text()='Delete Favorite']"));
                    return btn.isDisplayed();
                });
        myFavoriteCoupons.clickButton("Delete Favorite");

        ConfirmationModal confirmationModal = new ConfirmationModal(webDriver);
        confirmationModal.clickButtonInModal("Yes");
    }

}
