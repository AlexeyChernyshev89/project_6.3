package com.yandex.scooter.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

public class TestBase {
    protected WebDriver driver;

    @BeforeEach
    public void setUp() {
        String browser = System.getProperty("browser", "chrome");

        if ("firefox".equalsIgnoreCase(browser)) {
            driver = new FirefoxDriver();
        } else {
            driver = new ChromeDriver();
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://qa-scooter.praktikum-services.ru/");

        // Ждем загрузки страницы
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}