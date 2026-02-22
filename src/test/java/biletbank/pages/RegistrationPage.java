package biletbank.pages;

import biletbank.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class RegistrationPage {
    public RegistrationPage() {
        PageFactory.initElements(Driver.get(), this);
    }

    // --- Navigasyon ---
    @FindBy(linkText = "Yeni Acente Üyelik")
    public WebElement yeniAcenteLink;

    @FindBy(xpath = "//span[@class='checkmark']")
    public WebElement sozlesmeCheckbox;

    @FindBy(xpath = "//*[text()='Devam Et']")
    public WebElement devamEtBtn;

    // --- Acente Bilgileri (Input Indexleri) ---
    @FindBy(xpath = "(//input[@class='bltbnk-textbox'])[1]")
    public WebElement acenteAdiInput;

    @FindBy(xpath = "(//input[@class='bltbnk-textbox'])[2]")
    public WebElement mailInput;

    @FindBy(xpath = "(//input[@class='bltbnk-textbox'])[3]")
    public WebElement sabitTelInput;

    @FindBy(xpath = "(//input[@class='bltbnk-textbox'])[4]")
    public WebElement cepTelInput;

    @FindBy(xpath = "(//input[@class='bltbnk-textbox'])[5]")
    public WebElement faxNoInput;

    @FindBy(xpath = "(//input[@class='bltbnk-textbox'])[6]")
    public WebElement belgeNoInput;

    // --- Dropdownlar (Select etiketleri için index kullanımı) ---

   // Acente Sınıfı ve Ülke için mevcut yapını koruyabilirsin (Eğer çalışıyorlarsa)
    @FindBy(xpath = "(//select[@class='bb-select type3 mw-100 h36'])[1]")
    public WebElement acenteSinifiDropdown;

    @FindBy(xpath = "(//select[@class='bb-select type3 mw-100 h36'])[2]")
    public WebElement ulkeDropdown;

    @FindBy(xpath = "(//select[@class='bb-select type3 bordered mw-100 h36'])[1]") // Şehir
    public WebElement sehirDropdown;

    @FindBy(xpath = "(//select[@class='bb-select type3 bordered mw-100 h36'])[2]") // Ilce
    public WebElement ilceDropdown;

    // --- Adres Bilgileri (Inputlar devam ediyor) ---
    @FindBy(xpath = "(//input[@class='bltbnk-textbox'])[7]")
    public WebElement semtInput;

    @FindBy(xpath = "(//input[@class='bltbnk-textbox'])[8]")
    public WebElement caddeInput;

    @FindBy(xpath = "(//input[@class='bltbnk-textbox'])[9]")
    public WebElement adresDetayiInput;

    // Yol tarifi farkli bir locate anlayisina sahip
    @FindBy(xpath = "(//div[@class='bb-input'])[15]")
    public WebElement yolTarifiInput;

    // --- Acente Faaliyet Alanları (Checkboxlar) ---
    @FindBy(id = "activityAreasFlightSeller")
    public WebElement ucakCheckbox;

    // --- Fatura Bilgileri ---
    @FindBy(xpath = "(//input[@class='bltbnk-textbox'])[10]")
    public WebElement faturaUnvaniInput;

    @FindBy(xpath = "(//input[@class='bltbnk-textbox'])[11]")
    public WebElement vergiDairesiInput;

    @FindBy(xpath = "(//input[@class='bltbnk-textbox'])[12]")
    public WebElement vergiNoInput;

    @FindBy(xpath = "(//input[@class='bltbnk-textbox'])[13]")
    public WebElement faturaAdresiInput;

    @FindBy(xpath = "(//input[@class='bltbnk-textbox'])[14]")
    public WebElement bankaIsmiInput;

    @FindBy(xpath = "(//input[@class='bltbnk-textbox'])[15]")
    public WebElement faturaSehriInput;

    @FindBy(xpath = "(//input[@class='bltbnk-textbox'])[16]")
    public WebElement ibanInput;

    // --- Yönetici & Kullanıcı Giriş ---
    @FindBy(xpath = "(//input[@class='bltbnk-textbox'])[17]")
    public WebElement yetkiliAdSoyadInput;

    @FindBy(xpath = "(//input[@class='bltbnk-textbox'])[18]")
    public WebElement yetkiliCepInput;

    @FindBy(xpath = "(//input[@class='bltbnk-textbox'])[19]")
    public WebElement yetkiliMailInput;

    @FindBy(xpath = "(//input[@class='bltbnk-textbox'])[20]")
    public WebElement kullaniciAdiInput;

    @FindBy(xpath = "(//input[@class='bltbnk-textbox'])[21]")
    public WebElement kullaniciMailInput;

    @FindBy(xpath = "//input[@type='password']")
    public WebElement sifreInput;

    // --- Kaydet ---
    @FindBy(xpath = "//button[text()='Kaydet']")
    public WebElement gonderBtn;

    @FindBy(xpath = "//div[text()='İşlem Başarılı']")
    public WebElement basariliPopUpMesaji;
}