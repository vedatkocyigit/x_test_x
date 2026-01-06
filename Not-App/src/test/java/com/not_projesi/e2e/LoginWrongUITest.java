package com.not_projesi.e2e;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.UUID;

public class LoginWrongUITest {

    @Order(1)
    @Test
    void shouldRegisterUserAndThenFailLogin() {

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
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("ogrenciSifre")))
                    .sendKeys("123456");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("ogrenciAdi")))
                    .sendKeys("Test");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("ogrenciSoyadi")))
                    .sendKeys("User");
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

            // =====================
            // LOGIN (WRONG PASSWORD)
            // =====================
            driver.get("http://localhost/login");

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")))
                    .sendKeys(randomUsername);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("ogrenciSifre")))
                    .sendKeys("YANLIS");

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[type='submit']")
            )).click();

            WebElement error = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("loginError"))
            );

            assert error.getText().toLowerCase().contains("hat");

        } finally {
            driver.quit();
        }
    }
}
