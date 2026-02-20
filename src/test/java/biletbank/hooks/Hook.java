package biletbank.hooks;

import biletbank.utilities.Driver;
import biletbank.utilities.ReusableMethods; // Screenshot için eklendi
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.time.Duration;

public class Hook { // Genelde 'Hooks' olarak adlandırılır

    @Before
    public void setup(){
        Driver.get().manage().window().maximize();
        Driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
    }

    @After
    public void tearDown(Scenario scenario){
        if (scenario.isFailed()){
            // Senaryo hata aldığında ekran görüntüsünü rapora ekler
            final byte[] screenshot = ((TakesScreenshot) Driver.get()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "failed_scenario_screenshot");
        }

        // Test bittiğinde tarayıcıyı tamamen kapatır
        Driver.closeDriver();
    }
}