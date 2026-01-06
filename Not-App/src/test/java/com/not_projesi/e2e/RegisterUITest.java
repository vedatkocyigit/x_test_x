package com.not_projesi.e2e;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegisterUITest {

    @Order(1)
    @Test
    void shouldRegisterUser() {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String testUsername = "testuser_" + uniqueId;
        String testEmail = "test_" + uniqueId + "@test.com";

        try {
            driver.get("http://localhost/register");

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")))
                    .sendKeys(testUsername);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("ogrenciSifre")))
                    .sendKeys("123456");

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("ogrenciAdi")))
                    .sendKeys("Test");

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("ogrenciSoyadi")))
                    .sendKeys("User");

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("ogrenciEmail")))
                    .sendKeys(testEmail);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("bolumId")))
                    .sendKeys("1");

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[type='submit']")
            )).click();

            WebElement msg = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.id("registerMessage")
                    )
            );

            Assertions.assertTrue(
                    msg.getText().toLowerCase().contains("kayıt"),
                    "Kayıt başarılı mesajı görünmedi!"
            );

        } finally {
            driver.quit();
        }
    }
}
