package canvas.page.objects.account.drawer;

import canvas.page.objects.account.drawer.wallet.FinancialHistorySection;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Tabs {

    WebDriver webDriver;

    public Tabs(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public Tabs clickSectionInTab(String sectionName) {
        Awaitility
                .await()
                .pollInterval(2, TimeUnit.SECONDS)
                .atMost(30, TimeUnit.SECONDS)
                .until(() ->
                {
                    WebElement section = SeleniumWaits.visibilityOfElementLocated(webDriver,
                            By.xpath("//div[contains(@class, 'treeLinkManager')]//div[text()='"+sectionName+"']//span"));
                    JavascriptExecutor js = (JavascriptExecutor) webDriver;
                    js.executeScript("arguments[0].scrollIntoView();", section);

                    js.executeScript("arguments[0].click();", section);

                    return sectionIsInvisible(sectionName);
                });

        return this;
    }



    public Boolean sectionIsInvisible(String sectionName) {
        Boolean sectionIsInvisible = SeleniumWaits.invisibilityOfElementLocated(webDriver,
                By.xpath("//div[text()='"+sectionName+"']/following-sibling::span"));
        return sectionIsInvisible;
    }


}
