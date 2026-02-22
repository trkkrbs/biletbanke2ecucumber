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

        // i=1 den başlıyoruz (Header/Başlık satırını atlamak için)
        for (int i = 1; i <= excelReader.rowCount(); i++) {

            // --- 1. Bölüm: Acente Genel Bilgileri (Inputlar) ---
            registrationPage.acenteAdiInput.sendKeys(excelReader.getCellData(i, 0));
            registrationPage.mailInput.sendKeys(excelReader.getCellData(i, 1));
            registrationPage.sabitTelInput.sendKeys(excelReader.getCellData(i, 2));
            registrationPage.cepTelInput.sendKeys(excelReader.getCellData(i, 3));
            registrationPage.faxNoInput.sendKeys(excelReader.getCellData(i, 4));
            registrationPage.belgeNoInput.sendKeys(excelReader.getCellData(i, 5));

            // --- 2. Bölüm: Dropdownlar (Senin Reusable Metodun ile) ---
            ReusableMethods.selectByVisibleText(registrationPage.acenteSinifiDropdown, excelReader.getCellData(i, 6));
            ReusableMethods.selectByVisibleText(registrationPage.ulkeDropdown, excelReader.getCellData(i, 7));
            ReusableMethods.selectByVisibleText(registrationPage.sehirDropdown, excelReader.getCellData(i, 8));

            // İlçe dropdown'ının dolması için kısa bir bekleme (ReusableMethods.waitFor kullanıldı)
            ReusableMethods.waitFor(1);
            ReusableMethods.selectByVisibleText(registrationPage.ilceDropdown, excelReader.getCellData(i, 9));

            // --- 3. Bölüm: Adres Bilgileri ve Yol Tarifi (Scroll ile) ---
            registrationPage.semtInput.sendKeys(excelReader.getCellData(i, 10));
            registrationPage.caddeInput.sendKeys(excelReader.getCellData(i, 11));
            registrationPage.adresDetayiInput.sendKeys(excelReader.getCellData(i, 12));


// 2. Driver'ı Yol Tarifi'ne götür (Scroll)
            ReusableMethods.scrollToElement(registrationPage.yolTarifiInput);
            ReusableMethods.waitFor(1); // Sayfanın durulması için kısa bir es

// 3. JAVASCRIPT ile odağı zorla Yol Tarifi div'inin içine, yani input'a taşı
// Bu satır activeElement() karmaşasını ve kaymayı bitirir
            ((JavascriptExecutor) Driver.get()).executeScript("arguments[0].querySelector('input').focus();", registrationPage.yolTarifiInput);

// 4. Şimdi veriyi gönder (Odak artık içerideki input'ta)
            Driver.get().switchTo().activeElement().sendKeys(excelReader.getCellData(i, 13));

            // --- 4. Bölüm: Fatura ve Banka Bilgileri ---
            registrationPage.faturaUnvaniInput.sendKeys(excelReader.getCellData(i, 14));
            registrationPage.vergiDairesiInput.sendKeys(excelReader.getCellData(i, 15));
            registrationPage.vergiNoInput.sendKeys(excelReader.getCellData(i, 16));
            registrationPage.faturaAdresiInput.sendKeys(excelReader.getCellData(i, 17));
            registrationPage.bankaIsmiInput.sendKeys(excelReader.getCellData(i, 18));
            registrationPage.faturaSehriInput.sendKeys(excelReader.getCellData(i, 19));
            registrationPage.ibanInput.sendKeys(excelReader.getCellData(i, 20));

            // --- 5. Bölüm: Yetkili ve Giriş Bilgileri ---
            registrationPage.yetkiliAdSoyadInput.sendKeys(excelReader.getCellData(i, 21));
            registrationPage.yetkiliCepInput.sendKeys(excelReader.getCellData(i, 22));
            registrationPage.yetkiliMailInput.sendKeys(excelReader.getCellData(i, 23));
            registrationPage.kullaniciAdiInput.sendKeys(excelReader.getCellData(i, 24));
            registrationPage.kullaniciMailInput.sendKeys(excelReader.getCellData(i, 25));
            registrationPage.sifreInput.sendKeys(excelReader.getCellData(i, 26));
        }
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
