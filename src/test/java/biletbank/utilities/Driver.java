package biletbank.utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.time.Duration;

public class Driver {

    private static WebDriver driver;

    private Driver() {
        // Singleton pattern
    }

    public static WebDriver get() {
        if (driver == null) {
            // Browser tipini ConfigurationReader üzerinden alıyoruz
            String browser = ConfigurationReader.get("browser");

            switch (browser.toLowerCase()) {
                case "chrome":
                    driver = new ChromeDriver();
                    break;
                case "edge":
                    driver = new EdgeDriver();
                    break;
                case "safari":
                    driver = new SafariDriver();
                    break;
                case "firefox":
                    driver = new FirefoxDriver();
                    break;
                default:
                    // Eğer browser belirtilmemişse default olarak chrome başlatır
                    driver = new ChromeDriver();
            }

            // Standart driver yapılandırmaları
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        }
        return driver;
    }

    public static void closeDriver() {
        if (driver != null) {
            driver.quit();
            driver = null; // Belleği temizlemek için null'a çekiyoruz
        }
    }
}