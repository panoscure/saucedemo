package base.test;

import common.utils.Properties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import selenium.WebDriverInit;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

//@ExtendWith(WebDriverInit.class)
public class BaseTest {

    public static final java.util.Properties properties = new java.util.Properties();

    public static String url;

    public BaseTest()
    {
        loadProperties("dcdev.properties");
        url = BaseTest.getPropertyValue("sause.url");

    }

    public static void loadProperties(String filename) {
        InputStream inputStream = BaseTest.class.getClassLoader().getResourceAsStream(filename);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator<Object> itr = properties.keySet().iterator();
        while (itr.hasNext()) {
            String key = (String) itr.next();
            System.setProperty(key, properties.getProperty(key));
        }
    }

    public static String getPropertyValue(String key) {
        return properties.getProperty(key);
    }

    public static void setPropertyValue(String key, String value) {
        properties.setProperty(key, value);
    }
}
