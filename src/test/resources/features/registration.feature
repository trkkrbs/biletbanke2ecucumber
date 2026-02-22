Feature: Biletbank Acente Kayıt Formu Kontrolleri

  @positiveRegistration
  Scenario Outline: Yeni Acente Kayıt Formu Doğrulama ve Kayıt
    Given Kullanici biletbank acente sayfasina gider
    When "Yeni Acente Üyelik" linkine tiklar
    And Sozlesmeyi onaylayip devam eder
    Then Formdaki alanlarin dogrulugunu kontrol eder
    And Acente bilgilerini "<acenteAdi>", "<mailAdresi>", "<sabitTel>", "<cepTel>", "<faxNo>", "<belgeNo>", "<acenteSinifi>", "<ulke>", "<sehir>", "<ilce>", "<semt>", "<cadde>", "<adresDetayi>", "<yolTarifi>", "<acenteFaaliyet>", "<faturaUnvani>", "<vergiDairesi>", "<vergiNumarasi>", "<faturaAdresi>", "<bankaIsmi>", "<faturaSehri>", "<IBAN>", "<sirketYetkilisiAdSoyad>", "<yetkiliCepTel>", "<yetkiliMailAdresi>", "<kullaniciAdi>", "<kullaniciMailAdresi>", "<kullaniciSifre>" olarak girer
    And Formu gonderir
    Then Kayit isleminin basariyla tamamlandigini dogrular


    Examples:
      | acenteAdi | mailAdresi | sabitTel | cepTel | faxNo | belgeNo | acenteSinifi | ulke | sehir | ilce | semt | cadde | adresDetayi | yolTarifi | acenteFaaliyet | faturaUnvani | vergiDairesi | vergiNumarasi | faturaAdresi | bankaIsmi | faturaSehri | IBAN | sirketYetkilisiAdSoyad | yetkiliCepTel | yetkiliMailAdresi | kullaniciAdi | kullaniciMailAdresi | kullaniciSifre |
      | TEST-TARIKKARABAS-04 | karabastarik@gmail.com | 507 777 7777 | 507 777 7777 | 507 777 7777 | 11111111 | A Grubu | Türkiye | ANKARA | ETIMESGUT | TEST | TEST | TEST | TEST | Uçak | TEST | TEST | 11111111111 | TEST | TEST BANK | TESTSEHIR | TR890001000000000000000001 | TESTADSOYAD | 507 777 7777 | karabastarik@gmail.com | TESTTARIKKARABAS04 | karabastarik@gmail.com | Sifre123! |


  @positiveRegistrationWithExcel
  Scenario: Yeni Acente Kayıt Formu Doğrulama ve Kayıt - Excel ile
    Given Kullanici biletbank acente sayfasina gider
    When "Yeni Acente Üyelik" linkine tiklar
    And Sozlesmeyi onaylayip devam eder
    Then Formdaki alanlarin dogrulugunu kontrol eder
    And Acente bilgilerini exceldeki "Sheet1" sayfasindaki verilerle doldurur
    Then Formu gonderir