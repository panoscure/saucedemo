package tests.ticketchecker;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.TicketCheckerPage;
import canvas.page.objects.games.dc.DCGamesManualPlay;
import canvas.page.objects.games.drawer.GamesDrawer;
import canvas.page.objects.modals.InvalidTicketIdModal;
import canvas.page.objects.modals.PlayGameConfirmationModal;
import canvas.page.objects.modals.ThankYouForPlayingModal;
import canvas.page.objects.modals.TicketCheckerModal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import selenium.WebDriverInit;

import java.math.BigDecimal;
import java.util.List;

import static canvas.helpers.HelperMethods.*;
import static canvas.helpers.HelperMethods.getNumbersPlayedPerGameInTicketCheckerModal;
import static com.google.common.truth.Truth.assertWithMessage;

@ExtendWith(WebDriverInit.class)
public class TicketChecker extends BaseTest {
    @Test
    @DisplayName("QAAUT-1716 :: iLottery - Ticket Checker - Invalid Serial Number (Negative)")
    public void QAAUT$1716$iLotteryTicketCheckerInvalidSerialNumberNegative(WebDriver webDriver) {

        webDriver.get(canvasUrl);
        HomePage homePage = new HomePage(webDriver);

        String incorrectTicketId = "111111111111111111111111111111";

        homePage.clickTicketCheckerInTopMenu();

        //ticketChecker
        TicketCheckerPage ticketCheckerPage = new TicketCheckerPage(webDriver);

        ticketCheckerPage.enterTicketId(incorrectTicketId);
        ticketCheckerPage.clickCheckMyNumbersButton();

        InvalidTicketIdModal invalidTicketIdModal = new InvalidTicketIdModal(webDriver);

        String modalTitle = invalidTicketIdModal.getModalTitle();
        assertWithMessage("modal doesn't open")
                .that(modalTitle.equalsIgnoreCase("Code is not valid"))
                .isTrue();

        String errorMessage = invalidTicketIdModal.getModalMessage();
        assertWithMessage("modal doesn't open")
                .that(errorMessage.equalsIgnoreCase("Please enter a valid barcode."))
                .isTrue();

        invalidTicketIdModal.closeModal();

    }
}
