package base.test;

import common.utils.Properties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import selenium.WebDriverInit;

import java.io.IOException;

@ExtendWith(WebDriverInit.class)
public class BaseTest {

    public String canvasUrl = Properties.getPropertyValue("canvasUrl");

    @BeforeAll
    public static void loadProperties() throws IOException {
//        String environment = System.getenv("BAMBOO_TARGETED_ENV");
        String environment = "DC_DEV";
        if (environment == null) throw new RuntimeException("No environment variables found in order to load properties");
        switch (environment) {
            case "DC_DEV":
                Properties.loadProperties("dcdev.properties");
                break;
            case "DC_QA":
                Properties.loadProperties("dcqa.properties");
                break;
        }
    }
}
