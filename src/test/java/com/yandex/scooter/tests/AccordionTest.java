package com.yandex.scooter.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccordionTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
        driver.manage().window().maximize();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testFirstAccordionItem() {
        driver.get("https://qa-scooter.praktikum-services.ru/");

        // Закрываем окно с куками, если оно есть
        closeCookieBanner();

        // Прокручиваем до аккордеона
        scrollToElement(By.className("accordion"));

        // Локаторы для первого вопроса
        By firstQuestionLocator = By.id("accordion__heading-0");
        By firstAnswerLocator = By.id("accordion__panel-0");

        WebElement firstQuestion = driver.findElement(firstQuestionLocator);
        System.out.println("Первый вопрос: " + firstQuestion.getText());

        // Кликаем на первый вопрос через JavaScript, чтобы избежать перекрытия
        clickWithJavaScript(firstQuestion);

        // Ждем появления ответа
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstAnswerLocator));

        WebElement firstAnswer = driver.findElement(firstAnswerLocator);
        String answerText = firstAnswer.getText();
        System.out.println("Ответ: " + answerText);

        // Проверяем, что ответ появился
        assertTrue(isAnswerVisible(0), "Ответ должен отображаться после клика");
        assertTrue(answerText.contains("Сутки — 400 рублей"),
                "Должен содержать текст 'Сутки — 400 рублей'");
    }

    @Test
    public void testAllAccordionItemsShowCorrectText() {
        driver.get("https://qa-scooter.praktikum-services.ru/");

        // Закрываем окно с куками
        closeCookieBanner();

        // Прокручиваем до аккордеона
        scrollToElement(By.className("accordion"));

        // Находим все вопросы
        List<WebElement> questions = driver.findElements(
                By.xpath("//div[contains(@id, 'accordion__heading-')]")
        );

        System.out.println("Найдено вопросов: " + questions.size());

        // Ожидаемые ответы (используем части текста, которые точно есть)
        String[] expectedAnswerParts = {
                "Сутки — 400 рублей",
                "один заказ — один самокат",
                "Допустим, вы оформляете заказ на 8 мая",
                "Только начиная с завтрашнего",
                "Пока что нет!",
                "Самокат приезжает к вам с полной зарядкой",
                "Да, пока самокат не привезли",  // Исправлено с "не возвращен" на "не привезли"
                "Да, обязательно"
        };

        // Проверяем каждый вопрос
        for (int i = 0; i < questions.size(); i++) {
            System.out.println("\n=== Тестируем вопрос " + (i + 1) + " ===");

            // Находим вопрос заново
            WebElement question = driver.findElement(By.id("accordion__heading-" + i));
            String questionText = question.getText();
            System.out.println("Вопрос: " + questionText);

            // Кликаем на вопрос через JavaScript
            clickWithJavaScript(question);

            // Ждем появления ответа
            By answerLocator = By.id("accordion__panel-" + i);
            wait.until(ExpectedConditions.visibilityOfElementLocated(answerLocator));

            // Проверяем, что ответ появился
            assertTrue(isAnswerVisible(i), "Ответ должен отображаться после клика");

            // Проверяем текст ответа
            WebElement answer = driver.findElement(answerLocator);
            String actualAnswer = answer.getText();
            System.out.println("Ответ: " + actualAnswer);

            // Проверяем, что ответ содержит ожидаемую часть
            assertTrue(actualAnswer.contains(expectedAnswerParts[i]),
                    "Некорректный текст ответа для вопроса " + (i + 1) +
                            ". Ожидается часть: '" + expectedAnswerParts[i] + "'" +
                            ", получено: '" + actualAnswer + "'");
        }
    }

    @Test
    public void testOnlyOneAnswerVisibleAtATime() {
        driver.get("https://qa-scooter.praktikum-services.ru/");

        // Закрываем окно с куками
        closeCookieBanner();

        // Прокручиваем до аккордеона
        scrollToElement(By.className("accordion"));

        // Находим все вопросы
        List<WebElement> questions = driver.findElements(
                By.xpath("//div[contains(@id, 'accordion__heading-')]")
        );

        // Открываем первый вопрос
        WebElement firstQuestion = driver.findElement(By.id("accordion__heading-0"));
        clickWithJavaScript(firstQuestion);

        // Ждем появления первого ответа
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accordion__panel-0")));

        // Проверяем, что только первый ответ виден
        assertTrue(isAnswerVisible(0), "Первый ответ должен быть виден");
        for (int i = 1; i < questions.size(); i++) {
            assertFalse(isAnswerVisible(i), "Ответ " + (i + 1) + " должен быть скрыт");
        }

        // Открываем второй вопрос
        WebElement secondQuestion = driver.findElement(By.id("accordion__heading-1"));
        clickWithJavaScript(secondQuestion);

        // Ждем появления второго ответа
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accordion__panel-1")));

        // Проверяем, что только второй ответ виден (первый должен скрыться)
        assertFalse(isAnswerVisible(0), "Первый ответ должен скрыться при открытии второго");
        assertTrue(isAnswerVisible(1), "Второй ответ должен быть виден");
        for (int i = 2; i < questions.size(); i++) {
            assertFalse(isAnswerVisible(i), "Ответ " + (i + 1) + " должен быть скрыт");
        }
    }

    @Test
    public void testAccordionTextContent() {
        driver.get("https://qa-scooter.praktikum-services.ru/");

        // Закрываем окно с куками
        closeCookieBanner();

        // Прокручиваем до аккордеона
        scrollToElement(By.className("accordion"));

        // Ожидаемые вопросы
        String[] expectedQuestions = {
                "Сколько это стоит? И как оплатить?",
                "Хочу сразу несколько самокатов! Так можно?",
                "Как рассчитывается время аренды?",
                "Можно ли заказать самокат прямо на сегодня?",
                "Можно ли продлить заказ или вернуть самокат раньше?",
                "Вы привозите зарядку вместе с самокатом?",
                "Можно ли отменить заказ?",
                "Я жизу за МКАДом, привезёте?"
        };

        // Проверяем текст всех вопросов
        List<WebElement> questions = driver.findElements(
                By.xpath("//div[contains(@id, 'accordion__heading-')]")
        );

        assertEquals(expectedQuestions.length, questions.size(),
                "Количество вопросов должно быть " + expectedQuestions.length);

        for (int i = 0; i < questions.size(); i++) {
            String actualQuestion = questions.get(i).getText();
            assertEquals(expectedQuestions[i], actualQuestion,
                    "Текст вопроса " + (i + 1) + " не совпадает");
        }
    }

    @Test
    public void testAccordionCanBeClosedByClickingAnother() {
        driver.get("https://qa-scooter.praktikum-services.ru/");

        // Закрываем окно с куками
        closeCookieBanner();

        // Прокручиваем до аккордеона
        scrollToElement(By.className("accordion"));

        // Открываем первый вопрос
        WebElement firstQuestion = driver.findElement(By.id("accordion__heading-0"));
        clickWithJavaScript(firstQuestion);

        // Ждем появления первого ответа
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accordion__panel-0")));
        assertTrue(isAnswerVisible(0), "Первый ответ должен быть виден");

        // Кликаем на тот же вопрос - НЕ должен закрыться (это особенность сайта)
        clickWithJavaScript(firstQuestion);

        // Проверяем, что ответ все еще виден
        assertTrue(isAnswerVisible(0), "При повторном клике на тот же вопрос ответ должен оставаться видимым");

        // Кликаем на второй вопрос
        WebElement secondQuestion = driver.findElement(By.id("accordion__heading-1"));
        clickWithJavaScript(secondQuestion);

        // Ждем появления второго ответа
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accordion__panel-1")));

        // Проверяем, что первый скрылся, а второй виден
        assertFalse(isAnswerVisible(0), "Первый ответ должен скрыться при открытии второго");
        assertTrue(isAnswerVisible(1), "Второй ответ должен быть виден");
    }

    // Упрощенный тест только для проверки открытия/закрытия
    @Test
    public void testAccordionBasicFunctionality() {
        driver.get("https://qa-scooter.praktikum-services.ru/");

        // Закрываем окно с куками
        closeCookieBanner();

        // Прокручиваем до аккордеона
        scrollToElement(By.className("accordion"));

        // Проверяем несколько вопросов
        int[] questionIndices = {0, 3, 6}; // 1-й, 4-й и 7-й вопросы

        for (int index : questionIndices) {
            System.out.println("\nПроверка вопроса " + (index + 1));

            // Находим вопрос
            WebElement question = driver.findElement(By.id("accordion__heading-" + index));
            String questionText = question.getText();
            System.out.println("Вопрос: " + questionText);

            // Кликаем на вопрос
            clickWithJavaScript(question);

            // Ждем появления ответа
            By answerLocator = By.id("accordion__panel-" + index);
            wait.until(ExpectedConditions.visibilityOfElementLocated(answerLocator));

            // Проверяем, что ответ появился
            WebElement answer = driver.findElement(answerLocator);
            assertTrue(answer.isDisplayed(), "Ответ должен отображаться после клика");
            assertFalse(answer.getText().trim().isEmpty(), "Ответ должен содержать текст");

            System.out.println("Ответ отображается: ДА");

            // Кликаем на другой вопрос, чтобы закрыть текущий
            if (index < questionIndices[questionIndices.length - 1]) {
                WebElement nextQuestion = driver.findElement(By.id("accordion__heading-" + (index + 1)));
                clickWithJavaScript(nextQuestion);

                // Ждем скрытия текущего ответа
                wait.until(ExpectedConditions.invisibilityOf(answer));
                assertFalse(answer.isDisplayed(), "Ответ должен скрыться при открытии другого вопроса");
                System.out.println("Ответ скрылся при открытии другого вопроса: ДА");
            }
        }
    }

    // Вспомогательный метод для закрытия окна с куками
    private void closeCookieBanner() {
        try {
            // Небольшая пауза для появления окна
            Thread.sleep(1000);

            // Ищем окно с куками
            List<WebElement> cookieBanners = driver.findElements(
                    By.className("App_CookieConsent__1yUIN")
            );

            if (!cookieBanners.isEmpty()) {
                WebElement cookieBanner = cookieBanners.get(0);

                // Ищем кнопку согласия
                List<WebElement> buttons = cookieBanner.findElements(By.tagName("button"));
                for (WebElement button : buttons) {
                    String buttonText = button.getText().toLowerCase();
                    if (buttonText.contains("принять") || buttonText.contains("все привыкли") ||
                            buttonText.contains("да") || buttonText.contains("хорошо")) {
                        clickWithJavaScript(button);
                        System.out.println("Окно с куками закрыто");
                        break;
                    }
                }

                // Ждем исчезновения окна
                wait.until(ExpectedConditions.invisibilityOf(cookieBanner));
            }

        } catch (Exception e) {
            // Если окна нет или произошла ошибка, просто продолжаем
            System.out.println("Окно с куками не найдено или уже закрыто: " + e.getMessage());
        }
    }

    // Вспомогательный метод для клика через JavaScript
    private void clickWithJavaScript(WebElement element) {
        js.executeScript("arguments[0].click();", element);
    }

    // Вспомогательный метод для проверки видимости ответа
    private boolean isAnswerVisible(int index) {
        try {
            WebElement answer = driver.findElement(By.id("accordion__panel-" + index));
            return answer.isDisplayed() && !answer.getText().trim().isEmpty();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    // Вспомогательный метод для прокрутки
    private void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        try {
            Thread.sleep(500); // Небольшая пауза для завершения прокрутки
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}