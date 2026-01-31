package com.yandex.scooter.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderPageFirstStep {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы
    private final By pageTitle = By.xpath("//div[text()='Для кого самокат']");
    private final By nameField = By.xpath("//input[@placeholder='* Имя']");
    private final By surnameField = By.xpath("//input[@placeholder='* Фамилия']");
    private final By addressField = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");
    private final By metroField = By.xpath("//input[@placeholder='* Станция метро']");
    private final By phoneField = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");
    private final By nextButton = By.xpath("//button[text()='Далее']");

    public OrderPageFirstStep(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isPageDisplayed() {
        try {
            return driver.findElement(pageTitle).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void fillForm(String name, String surname, String address, String metro, String phone) {
        // Заполняем имя
        WebElement nameInput = driver.findElement(nameField);
        nameInput.clear();
        nameInput.sendKeys(name);

        // Заполняем фамилию
        WebElement surnameInput = driver.findElement(surnameField);
        surnameInput.clear();
        surnameInput.sendKeys(surname);

        // Заполняем адрес
        WebElement addressInput = driver.findElement(addressField);
        addressInput.clear();
        addressInput.sendKeys(address);

        // Выбираем станцию метро
        selectMetroStation(metro);

        // Заполняем телефон
        WebElement phoneInput = driver.findElement(phoneField);
        phoneInput.clear();
        phoneInput.sendKeys(phone);
    }

    private void selectMetroStation(String stationName) {
        // Кликаем на поле метро
        driver.findElement(metroField).click();

        // Ждем появления выпадающего списка
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Выбираем станцию
        By stationLocator = By.xpath("//div[@class='select-search__select']//*[text()='" + stationName + "']");
        wait.until(ExpectedConditions.elementToBeClickable(stationLocator)).click();
    }

    public void clickNextButton() {
        driver.findElement(nextButton).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}