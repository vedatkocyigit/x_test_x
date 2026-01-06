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
public class DersNotuAddUITest {

    @Order(5)
    @Test
    void registerLoginAndAddDersNotu() throws Exception {

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
            driver.findElement(By.name("ogrenciAdi")).sendKeys("Test");
            driver.findElement(By.name("ogrenciSoyadi")).sendKeys("User");
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
            // DERS NOTU EKLE
            // =====================
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("btnDersNotuEkle")
            )).click();

            wait.until(ExpectedConditions.urlContains("/ders-notu-ekle"));

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("dersNotAdi")))
                    .sendKeys("Test Ders Notu");
            driver.findElement(By.name("dersNotIcerik"))
                    .sendKeys("Bu bir test ders notudur.");
            driver.findElement(By.name("dersNotFiyat")).sendKeys("50");
            driver.findElement(By.name("notTuruId")).sendKeys("1");

            // =====================
            // TEMP PDF FILES (CI SAFE)
            // =====================
            File pdf = Files.createTempFile("test-not", ".pdf").toFile();
            File pdfPreview = Files.createTempFile("test-preview", ".pdf").toFile();

            driver.findElement(By.name("pdfFile"))
                    .sendKeys(pdf.getAbsolutePath());
            driver.findElement(By.name("pdfOnizlemeFile"))
                    .sendKeys(pdfPreview.getAbsolutePath());

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[type='submit']")
            )).click();

            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();

        } finally {
            driver.quit();
        }
    }
}
