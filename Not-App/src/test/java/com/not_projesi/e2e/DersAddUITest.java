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

import java.time.Duration;
import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DersAddUITest {

    @Order(6)
    @Test
    void registerLoginAddDersAndLogout() {

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            String randomUsername = "user" + UUID.randomUUID().toString().substring(0, 8);
            String randomEmail = "email" + UUID.randomUUID().toString().substring(0, 8) + "@test.com";

            driver.get("http://localhost/register");

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")))
                    .sendKeys(randomUsername);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("ogrenciSifre")))
                    .sendKeys("123456");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("ogrenciAdi")))
                    .sendKeys("Selenium");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("ogrenciSoyadi")))
                    .sendKeys("Test");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("ogrenciEmail")))
                    .sendKeys(randomEmail);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("bolumId")))
                    .sendKeys("1");

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[type='submit']")
            )).click();

            WebElement registerMsg = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("registerMessage"))
            );
            assert registerMsg.getText().toLowerCase().contains("kayıt");

            driver.get("http://localhost/login");

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")))
                    .sendKeys(randomUsername);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("ogrenciSifre")))
                    .sendKeys("123456");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("ogrenciAdi")))
                    .sendKeys("Selenium");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("ogrenciSoyadi")))
                    .sendKeys("Test");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("ogrenciEmail")))
                    .sendKeys(randomEmail);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("bolumId")))
                    .sendKeys("1");

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[type='submit']")
            )).click();

            wait.until(ExpectedConditions.urlContains("/home"));

            wait.until(ExpectedConditions.elementToBeClickable(By.id("btnDersEkle"))).click();
            wait.until(ExpectedConditions.urlContains("/ders-ekle"));

            String randomDersAdi = "Matematik " + UUID.randomUUID().toString().substring(0, 4);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("dersId")))
                    .sendKeys("1");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("dersAdi")))
                    .sendKeys(randomDersAdi);

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[type='submit']")
            )).click();

            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[text()='Çıkış']")
            )).click();

            wait.until(ExpectedConditions.urlContains("/login"));

        } finally {
            driver.quit();
        }
    }
}
