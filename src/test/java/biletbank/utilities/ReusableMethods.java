package biletbank.utilities;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ReusableMethods {

    private static final Duration DEFAULT_WAIT = Duration.ofSeconds(15);

    // --- SCREENSHOT ---

    /**
     * Ekran görüntüsü alır ve hem dosyaya kaydeder hem de Base64 döner.
     */
    public static String captureScreenshot() {
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String targetPath = System.getProperty("user.dir") + "/test-output/Screenshots/screenshot_" + date + ".png";

        TakesScreenshot ts = (TakesScreenshot) Driver.get();
        File source = ts.getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(source, new File(targetPath));
        } catch (IOException e) {
            System.err.println("Screenshot kaydedilemedi: " + e.getMessage());
        }
        return ts.getScreenshotAs(OutputType.BASE64);
    }

    // --- WAITS (BEKLEMELER) ---

    public static void waitFor(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static WebElement waitForVisibility(WebElement element, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(Driver.get(), Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForClickability(WebElement element, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(Driver.get(), Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void waitForPageToLoad() {
        new WebDriverWait(Driver.get(), DEFAULT_WAIT).until(
                driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete")
        );
    }

    // --- ACTIONS & CLICK ---

    /**
     * JavaScript ile tıklama yapmadan önce elementin tıklanabilir olmasını bekler.
     */
    public static void clickWithJS(WebElement element) {
        waitForClickability(element, (int) DEFAULT_WAIT.toSeconds());
        ((JavascriptExecutor) Driver.get()).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        ((JavascriptExecutor) Driver.get()).executeScript("arguments[0].click();", element);
    }

    public static void hover(WebElement element) {
        new Actions(Driver.get()).moveToElement(waitForVisibility(element, 10)).perform();
    }

    /**
     * Belirli bir metne sahip elemente tıklar (Daha güvenli hale getirildi)
     */
    public static void clickWithText(String text) {
        By locator = By.xpath("//*[contains(text(),'" + text + "')]");
        Driver.get().findElement(locator).click();
    }

    // --- FORM ELEMENTS ---

    public static void selectByVisibleText(WebElement element, String text) {
        Select select = new Select(waitForVisibility(element, 10));
        select.selectByVisibleText(text);
    }

    public static void selectCheckBox(WebElement element, boolean check) {
        if (check != element.isSelected()) {
            element.click();
        }
    }

    // --- UTILS ---

    /**
     * Java 8 Stream kullanarak daha hızlı ve temiz liste dönüşümü
     */
    public static List<String> getElementsText(List<WebElement> list) {
        return list.stream()
                .map(WebElement::getText)
                .filter(text -> !text.isEmpty())
                .collect(Collectors.toList());
    }

    public static void scrollToElement(WebElement element) {
        ((JavascriptExecutor) Driver.get()).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    /**
     * Window handle geçişi (Index ile - Daha pratiktir)
     */
    public static void switchToWindow(int index) {
        List<String> windows = new ArrayList<>(Driver.get().getWindowHandles());
        Driver.get().switchTo().window(windows.get(index));
    }

    // --- ASSERTIONS ---

    public static void verifyElementDisplayed(WebElement element) {
        try {
            Assert.assertTrue("Element görünmüyor!", element.isDisplayed());
        } catch (NoSuchElementException | TimeoutException e) {
            Assert.fail("Element sayfada bulunamadı veya görünür değil.");
        }
    }
}