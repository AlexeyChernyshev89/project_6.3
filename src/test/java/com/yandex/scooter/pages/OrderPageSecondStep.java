package com.yandex.scooter.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderPageSecondStep {
    private WebDriver driver;

    // Заголовок страницы "Про аренду"
    @FindBy(className = "Order_Header__BZXOb")
    private WebElement pageHeader;

    // Поле "Когда привезти самокат"
    @FindBy(css = "input[placeholder='* Когда привезти самокат']")
    private WebElement dateField;

    // Поле "Срок аренды"
    @FindBy(className = "Dropdown-control")
    private WebElement rentalPeriodField;

    // Чекбокс "Черный жемчуг"
    @FindBy(id = "black")
    private WebElement blackCheckbox;

    // Чекбокс "Серая безысходность"
    @FindBy(id = "grey")
    private WebElement greyCheckbox;

    // Поле "Комментарий"
    @FindBy(css = "input[placeholder='Комментарий для курьера']")
    private WebElement commentField;

    // Кнопка "Заказать"
    @FindBy(xpath = "//button[text()='Заказать']")
    private WebElement orderButton;

    // Кнопка "Назад"
    @FindBy(xpath = "//button[text()='Назад']")
    private WebElement backButton;

    // Кнопка "Да" в модальном окне подтверждения
    @FindBy(xpath = "//button[text()='Да']")
    private WebElement confirmButton;

    // Кнопка "Нет" в модальном окне подтверждения
    @FindBy(xpath = "//button[text()='Нет']")
    private WebElement cancelButton;

    // Модальное окно успешного заказа
    @FindBy(className = "Order_ModalHeader__3FDaJ")
    private WebElement successModal;

    // Текст номера заказа
    @FindBy(className = "Order_Text__2broi")
    private WebElement orderText;

    // Кнопка "Посмотреть статус"
    @FindBy(xpath = "//button[text()='Посмотреть статус']")
    private WebElement viewStatusButton;

    public OrderPageSecondStep(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isPageDisplayed() {
        try {
            return pageHeader.isDisplayed() && pageHeader.getText().contains("Про аренду");
        } catch (Exception e) {
            return false;
        }
    }

    public void fillForm(String date, String rentalPeriod, String color, String comment) {
        // Заполнение даты
        setDate(date);

        // Выбор срока аренды
        selectRentalPeriod(rentalPeriod);

        // Выбор цвета
        selectColor(color);

        // Заполнение комментария
        setComment(comment);
    }

    public void setDate(String date) {
        dateField.clear();
        dateField.sendKeys(date);
        // Нажимаем ENTER для закрытия календаря
        dateField.sendKeys(Keys.ENTER);
        // Кликаем в любое другое место для гарантии
        pageHeader.click();
    }

    public void selectRentalPeriod(String rentalPeriod) {
        rentalPeriodField.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // Ищем опцию в выпадающем списке
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class, 'Dropdown-option') and text()='" + rentalPeriod + "']")
        ));

        // Прокручиваем до элемента, если нужно
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", option
        );

        option.click();
    }

    public void selectColor(String color) {
        if ("black".equalsIgnoreCase(color)) {
            if (!blackCheckbox.isSelected()) {
                blackCheckbox.click();
            }
        } else if ("grey".equalsIgnoreCase(color)) {
            if (!greyCheckbox.isSelected()) {
                greyCheckbox.click();
            }
        } else if ("both".equalsIgnoreCase(color)) {
            // Если нужно выбрать оба цвета
            if (!blackCheckbox.isSelected()) {
                blackCheckbox.click();
            }
            if (!greyCheckbox.isSelected()) {
                greyCheckbox.click();
            }
        }
    }

    public void setComment(String comment) {
        if (comment != null && !comment.trim().isEmpty()) {
            commentField.clear();
            commentField.sendKeys(comment);
        }
    }

    public void clickOrderButton() {
        orderButton.click();
    }

    public void clickBackButton() {
        backButton.click();
    }

    public void confirmOrder() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(confirmButton));
        confirmButton.click();
    }

    public void cancelOrder() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(cancelButton));
        cancelButton.click();
    }

    public boolean isSuccessModalDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(successModal));
            return successModal.isDisplayed() && successModal.getText().contains("Заказ оформлен");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isConfirmationModalDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement modalTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'Order_ModalHeader') and contains(text(), 'Хотите оформить заказ')]")
            ));
            return modalTitle.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getOrderNumber() {
        try {
            if (isSuccessModalDisplayed() && orderText.isDisplayed()) {
                String text = orderText.getText();
                // Извлекаем номер заказа из текста
                if (text.contains("Номер заказа:")) {
                    String[] parts = text.split("Номер заказа:");
                    if (parts.length > 1) {
                        String numberPart = parts[1].trim().split("\\s")[0];
                        return numberPart.replace(".", "").trim();
                    }
                }
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public void clickViewStatusButton() {
        try {
            if (viewStatusButton.isDisplayed() && viewStatusButton.isEnabled()) {
                viewStatusButton.click();
            }
        } catch (Exception e) {
            // Игнорируем, если кнопка не доступна
        }
    }

    public void waitForPageLoad() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(pageHeader));
    }
}