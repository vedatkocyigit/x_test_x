package com.not_projesi.e2e;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.nio.file.Files;
import java.time.Duration;
import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DersNotuAddAndCheckUITest {

    @Order(7)
    @Test
    void registerLoginAddDersNotuAndCheckNotlarimThenLogout() throws Exception {

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            String randomUsername = "user" + UUID.randomUUID().toString().substring(0, 8);
            String randomEmail = "email" + UUID.randomUUID().toString().substring(0, 8) + "@test.com";

            // =====================
            // REGISTER
            // =====================
            driver.get("http://localhost/register");

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")))
                    .sendKeys(randomUsername);
            driver.findElement(By.name("ogrenciSifre")).sendKeys("123456");
            driver.findElement(By.name("ogrenciAdi")).sendKeys("Selenium7");
            driver.findElement(By.name("ogrenciSoyadi")).sendKeys("Test");
            driver.findElement(By.name("ogrenciEmail")).sendKeys(randomEmail);
            driver.findElement(By.name("bolumId")).sendKeys("1");

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[type='submit']")
            )).click();

            WebElement registerMsg = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("registerMessage"))
            );
            assert registerMsg.getText().toLowerCase().contains("kayıt");

            // =====================
            // LOGIN
            // =====================
            driver.get("http://localhost/login");

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")))
                    .sendKeys(randomUsername);
            driver.findElement(By.name("ogrenciSifre")).sendKeys("123456");

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[type='submit']")
            )).click();

            wait.until(ExpectedConditions.urlContains("/home"));

            // =====================
            // ADD DERS NOTU
            // =====================
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("btnDersNotuEkle")
            )).click();

            wait.until(ExpectedConditions.urlContains("/ders-notu-ekle"));

            String randomDersNotAdi = "Test Notu " + UUID.randomUUID().toString().substring(0, 4);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("dersNotAdi")))
                    .sendKeys(randomDersNotAdi);
            driver.findElement(By.name("dersNotIcerik"))
                    .sendKeys("Bu test ders notudur.");
            driver.findElement(By.name("dersNotFiyat")).sendKeys("100");
            driver.findElement(By.name("notTuruId")).sendKeys("1");

            // =====================
            // TEMP PDF FILES (CI SAFE)
            // =====================
            File pdf = Files.createTempFile("ders-not", ".pdf").toFile();
            File pdfPreview = Files.createTempFile("ders-not-preview", ".pdf").toFile();

            driver.findElement(By.name("pdfFile"))
                    .sendKeys(pdf.getAbsolutePath());
            driver.findElement(By.name("pdfOnizlemeFile"))
                    .sendKeys(pdfPreview.getAbsolutePath());

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[type='submit']")
            )).click();

            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();

            // =====================
            // NOTLARIM KONTROL
            // =====================
            driver.get("http://localhost/home");

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("btnNotlarim")
            )).click();

            wait.until(ExpectedConditions.urlContains("/notlarim"));

            WebElement addedNote = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//h3[text()='" + randomDersNotAdi + "']")
                    )
            );

            assert addedNote != null : "Ders notu Notlarım sayfasında görünmüyor!";

            // =====================
            // LOGOUT
            // =====================
            driver.get("http://localhost/home");

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[text()='Çıkış']")
            )).click();

            wait.until(ExpectedConditions.urlContains("/login"));

        } finally {
            driver.quit();
        }
    }
}
