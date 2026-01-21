package com.yandex.scooter.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы для аккордеона
    private final By faqSection = By.xpath("//div[text()='Вопросы о важном']");
    private final By accordionItems = By.cssSelector("[data-accordion-component='AccordionItem']");
    private final By questionButtons = By.cssSelector("[data-accordion-component='AccordionItemButton']");
    private final By answerPanels = By.cssSelector("[data-accordion-component='AccordionItemPanel']");

    // Кнопки заказа
    private final By headerOrderButton = By.xpath("//button[text()='Заказать' and parent::div[@class='Header_Nav__AGCXC']]");
    private final By bottomOrderButton = By.xpath("//button[text()='Заказать' and parent::div[@class='Home_FinishButton__1_cWm']]");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Метод для прокрутки к разделу FAQ
    public void scrollToFaq() {
        WebElement faqElement = driver.findElement(faqSection);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", faqElement);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Получение количества вопросов
    public int getQuestionsCount() {
        return driver.findElements(questionButtons).size();
    }

    // Получение текста вопроса по индексу
    public String getQuestionText(int index) {
        List<WebElement> questions = driver.findElements(questionButtons);
        if (index < questions.size()) {
            return questions.get(index).getText();
        }
        return "";
    }

    // Клик по вопросу по индексу
    public void clickQuestion(int index) {
        List<WebElement> questions = driver.findElements(questionButtons);
        if (index < questions.size()) {
            WebElement question = questions.get(index);

            // Прокручиваем к элементу
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", question);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Кликаем
            question.click();

            // Ждем анимации
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Получение текста ответа по индексу
    public String getAnswerText(int index) {
        List<WebElement> answers = driver.findElements(answerPanels);
        if (index < answers.size()) {
            WebElement answer = answers.get(index);
            wait.until(ExpectedConditions.visibilityOf(answer));
            return answer.getText();
        }
        return "";
    }

    // Проверка, что ответ отображается
    public boolean isAnswerDisplayed(int index) {
        try {
            List<WebElement> answers = driver.findElements(answerPanels);
            if (index < answers.size()) {
                WebElement answer = answers.get(index);
                return answer.isDisplayed();
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // Проверка атрибута aria-expanded
    public boolean isQuestionExpanded(int index) {
        List<WebElement> questions = driver.findElements(questionButtons);
        if (index < questions.size()) {
            WebElement question = questions.get(index);
            String ariaExpanded = question.getAttribute("aria-expanded");
            return "true".equals(ariaExpanded);
        }
        return false;
    }

    // Клик по кнопке заказа в хедере
    public void clickHeaderOrderButton() {
        driver.findElement(headerOrderButton).click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Клик по нижней кнопке заказа
    public void clickBottomOrderButton() {
        WebElement button = driver.findElement(bottomOrderButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", button);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        button.click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Ожидания
    public void waitForPageLoad() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}