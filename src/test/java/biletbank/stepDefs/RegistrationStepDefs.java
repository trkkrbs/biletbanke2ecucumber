package biletbank.stepDefs;

import biletbank.pages.RegistrationPage;
import biletbank.utilities.ConfigurationReader;
import biletbank.utilities.Driver;
import biletbank.utilities.ExcelReader;
import biletbank.utilities.ReusableMethods;
import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;


public class RegistrationStepDefs {

    RegistrationPage registrationPage = new RegistrationPage();

    @Given("Kullanici biletbank acente sayfasina gider")
    public void kullanici_biletbank_acente_sayfasina_gider() {
        Driver.get().get(ConfigurationReader.get("url"));
    }

    @When("{string} linkine tiklar")
    public void linkine_tiklar(String linkText) {
        registrationPage.yeniAcenteLink.click();

        // Yeni sekme kontrolü ve geçiş
        for (String handle : Driver.get().getWindowHandles()) {
            Driver.get().switchTo().window(handle);
        }
        ReusableMethods.waitForPageToLoad();
    }

    @And("Sozlesmeyi onaylayip devam eder")
    public void sozlesmeyi_onaylayip_devam_eder() {
        ReusableMethods.waitForClickability(registrationPage.sozlesmeCheckbox, 10).click();
        registrationPage.devamEtBtn.click();
    }

    @Then("Formdaki alanlarin dogrulugunu kontrol eder")
    public void formdaki_alanlarin_dogrulugunu_kontrol_eder() {
        ReusableMethods.waitForPageToLoad();
        ReusableMethods.verifyElementDisplayed(registrationPage.acenteAdiInput);
        Assert.assertTrue("Mail alanı aktif değil!", registrationPage.mailInput.isEnabled());
    }

    @And("Acente bilgilerini {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string} olarak girer")
    public void acenteBilgileriniOlarakGirer(
            String acenteAdi, String mailAdresi, String sabitTel, String cepTel,
            String faxNo, String belgeNo, String acenteSinifi, String ulke,
            String sehir, String ilce, String semt, String cadde,
            String adresDetayi, String yolTarifi, String acenteFaaliyet, String faturaUnvani,
            String vergiDairesi, String vergiNumarasi, String faturaAdresi, String bankaIsmi,
            String faturaSehri, String IBAN, String sirketYetkilisiAdSoyad, String yetkiliCepTel,
            String yetkiliMailAdresi, String kullaniciAdi, String kullaniciMailAdresi, String kullaniciSifre) {

        // --- ACENTE BİLGİLERİ ---
        registrationPage.acenteAdiInput.sendKeys(acenteAdi);
        registrationPage.mailInput.sendKeys(mailAdresi);
        registrationPage.sabitTelInput.sendKeys(sabitTel);
        registrationPage.cepTelInput.sendKeys(cepTel);
        registrationPage.faxNoInput.sendKeys(faxNo);
        registrationPage.belgeNoInput.sendKeys(belgeNo);

        // --- DROPDOWN SEÇİMLERİ ---
        ReusableMethods.selectByVisibleText(registrationPage.acenteSinifiDropdown, acenteSinifi);
        ReusableMethods.selectByVisibleText(registrationPage.ulkeDropdown, ulke);

        // Şehir seçimi sonrası İlçe'nin yüklenmesi için bekleme
        ReusableMethods.selectByVisibleText(registrationPage.sehirDropdown, sehir);
        ReusableMethods.waitFor(2);

        // İlçe seçimi
        ReusableMethods.selectByVisibleText(registrationPage.ilceDropdown, ilce);
        ReusableMethods.waitFor(1);

        // --- ADRES VE FAALİYET (TAB Zinciri ile Giriş) ---
        // 1. SEMT
        registrationPage.semtInput.clear();
        registrationPage.semtInput.sendKeys(semt + Keys.TAB);
        ReusableMethods.waitFor(1);

        // 2. CADDE
        registrationPage.caddeInput.sendKeys(cadde + Keys.TAB);
        ReusableMethods.waitFor(1);

        // 3. ADRES DETAYI
        registrationPage.adresDetayiInput.sendKeys(adresDetayi + Keys.TAB);
        ReusableMethods.waitFor(1);

        // 4. YOL TARİFİ
        Driver.get().switchTo().activeElement().sendKeys(yolTarifi);


        // Acente Faaliyet Alanlari
        ReusableMethods.selectCheckBox(registrationPage.ucakCheckbox, acenteFaaliyet.equalsIgnoreCase("Uçak"));


        // --- FATURA BİLGİLERİ ---
        registrationPage.faturaUnvaniInput.sendKeys(faturaUnvani);
        registrationPage.vergiDairesiInput.sendKeys(vergiDairesi);
        registrationPage.vergiNoInput.sendKeys(vergiNumarasi);
        registrationPage.faturaAdresiInput.sendKeys(faturaAdresi);
        registrationPage.bankaIsmiInput.sendKeys(bankaIsmi);
        registrationPage.faturaSehriInput.sendKeys(faturaSehri);
        registrationPage.ibanInput.sendKeys(IBAN);

        // --- YÖNETİCİ & KULLANICI GİRİŞ ---
        registrationPage.yetkiliAdSoyadInput.sendKeys(sirketYetkilisiAdSoyad);
        registrationPage.yetkiliCepInput.sendKeys(yetkiliCepTel);
        registrationPage.yetkiliMailInput.sendKeys(yetkiliMailAdresi);
        registrationPage.kullaniciAdiInput.sendKeys(kullaniciAdi);
        registrationPage.kullaniciMailInput.sendKeys(kullaniciMailAdresi);
        registrationPage.sifreInput.sendKeys(kullaniciSifre);
    }

    @Then("Formu gonderir")
    public void formu_gonderir() {
        ReusableMethods.waitForClickability(registrationPage.gonderBtn, 10);
        ReusableMethods.scrollToElement(registrationPage.gonderBtn);
        ReusableMethods.clickWithJS(registrationPage.gonderBtn);
    }

    @And("Acente bilgilerini exceldeki {string} sayfasindaki verilerle doldurur")
    public void acenteBilgileriniExceldekiSayfasindakiVerilerleDoldurur(String sayfaIsmi) {
        String dosyaYolu = "src/test/resources/acenteTestData.xlsx";
        ExcelReader excelReader = new ExcelReader(dosyaYolu, sayfaIsmi);
        RegistrationPage registrationPage = new RegistrationPage();

        for (int i = 1; i <= excelReader.rowCount(); i++) {

            // --- 1. Bölüm: Acente Genel Bilgileri ---
            sendText(registrationPage.acenteAdiInput, excelReader.getCellData(i, 0));
            sendText(registrationPage.mailInput, excelReader.getCellData(i, 1));
            sendText(registrationPage.sabitTelInput, formatExcelNumber(excelReader.getCellData(i, 2)));
            sendText(registrationPage.cepTelInput, formatExcelNumber(excelReader.getCellData(i, 3)));
            sendText(registrationPage.faxNoInput, formatExcelNumber(excelReader.getCellData(i, 4)));
            sendText(registrationPage.belgeNoInput, formatExcelNumber(excelReader.getCellData(i, 5)));

            // --- 2. Bölüm: Dropdownlar ---
            ReusableMethods.selectByVisibleText(registrationPage.acenteSinifiDropdown, excelReader.getCellData(i, 6));
            ReusableMethods.selectByVisibleText(registrationPage.ulkeDropdown, excelReader.getCellData(i, 7));
            ReusableMethods.selectByVisibleText(registrationPage.sehirDropdown, excelReader.getCellData(i, 8));
            ReusableMethods.waitFor(1);
            ReusableMethods.selectByVisibleText(registrationPage.ilceDropdown, excelReader.getCellData(i, 9));

            // --- 3. Bölüm: Adres Bilgileri ve Yol Tarifi ---
            sendText(registrationPage.semtInput, excelReader.getCellData(i, 10));
            sendText(registrationPage.caddeInput, excelReader.getCellData(i, 11));
            sendText(registrationPage.adresDetayiInput, excelReader.getCellData(i, 12));

            // YOL TARİFİ ÇÖZÜMÜ: Standart sendKeys yerine Actions kullanarak odağı zorluyoruz
            ReusableMethods.scrollToElement(registrationPage.yolTarifiInput);
            ReusableMethods.clickWithJS(registrationPage.yolTarifiInput); // Kutuyu JS ile aktifleştir
            // Metodun başında veya döngünün başında bir kez tanımla
            Actions actions = new Actions(Driver.get());

            // Sonra istediğin yerde sadece ismini (actions) kullanarak devam et
            actions.moveToElement(registrationPage.yolTarifiInput)
                    .click()
                    .sendKeys(excelReader.getCellData(i, 13))
                    .sendKeys(Keys.TAB)
                    .perform();

            // Acente Faaliyet Alanları
            if (excelReader.getCellData(i, 14).equalsIgnoreCase("Uçak")) {
                if (!registrationPage.ucakCheckbox.isSelected()) {
                    ReusableMethods.clickWithJS(registrationPage.ucakCheckbox);
                }
            }

            // --- 4. Bölüm: Fatura ve Banka Bilgileri ---
            sendText(registrationPage.faturaUnvaniInput, excelReader.getCellData(i, 15));
            sendText(registrationPage.vergiDairesiInput, excelReader.getCellData(i, 16));

            // Vergi No (1.11E10 hatasını BigDecimal metodu ile burada çözüyoruz)
            sendText(registrationPage.vergiNoInput, formatExcelNumber(excelReader.getCellData(i, 17)));

            sendText(registrationPage.faturaAdresiInput, excelReader.getCellData(i, 18));
            sendText(registrationPage.bankaIsmiInput, excelReader.getCellData(i, 19));
            sendText(registrationPage.faturaSehriInput, excelReader.getCellData(i, 20));
            sendText(registrationPage.ibanInput, formatExcelNumber(excelReader.getCellData(i, 21)));

            // --- 5. Bölüm: Yetkili ve Giriş Bilgileri ---
            sendText(registrationPage.yetkiliAdSoyadInput, excelReader.getCellData(i, 22));
            sendText(registrationPage.yetkiliCepInput, formatExcelNumber(excelReader.getCellData(i, 23)));
            sendText(registrationPage.yetkiliMailInput, excelReader.getCellData(i, 24));
            sendText(registrationPage.kullaniciAdiInput, excelReader.getCellData(i, 25));
            sendText(registrationPage.kullaniciMailInput, excelReader.getCellData(i, 26));
            sendText(registrationPage.sifreInput, excelReader.getCellData(i, 27));
        }
    }
    // --- YARDIMCI METOTLAR ---

    private void sendText(org.openqa.selenium.WebElement element, String text) {
        if (text == null || text.isEmpty()) return;
        ReusableMethods.waitForVisibility(element, 10);
        ReusableMethods.scrollToElement(element);
        element.clear();
        element.sendKeys(text);
        ReusableMethods.waitFor(1);
    }

    private String formatExcelNumber(String value) {
        if (value == null || value.isEmpty()) return "";
        try {
            if (value.toLowerCase().contains("e")) {
                return new java.math.BigDecimal(value).toPlainString();
            }
        } catch (Exception e) {
            return value.replaceAll("[^0-9]", "");
        }
        return value;
    }

    @Then("Kayit isleminin basariyla tamamlandigini dogrular")
    public void kayit_isleminin_basariyla_tamamlandigini_dogrular() {
        // 1. Popup'ın görünmesini bekle (Maksimum 10 saniye)
        ReusableMethods.waitForVisibility(registrationPage.basariliPopUpMesaji, 10);

        // 2. Başlık Doğrulaması
        String expectedBaslik = "İşlem Başarılı";
        String actualBaslik = registrationPage.basariliPopUpMesaji.getText();
        Assert.assertEquals("Başarı başlığı hatalı!", expectedBaslik, actualBaslik);

        // 3. (Opsiyonel) Rapor için ekran görüntüsü al
        ReusableMethods.captureScreenshot();

        System.out.println("Test Başarıyla Tamamlandı: Kayıt oluşturuldu.");
    }


}
