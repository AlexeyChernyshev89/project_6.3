package com.yandex.scooter.tests;

import com.yandex.scooter.config.TestBase;
import com.yandex.scooter.data.FaqData;
import com.yandex.scooter.pages.HomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class FaqTest extends TestBase {

    static Stream<Arguments> faqDataProvider() {
        return Stream.of(FaqData.getAllFaqQuestions())
                .map(faqData -> Arguments.of(faqData));
    }

    @ParameterizedTest(name = "Проверка FAQ: {0}")
    @MethodSource("faqDataProvider")
    @DisplayName("Проверка выпадающего списка в разделе 'Вопросы о важном'")
    public void testFaqQuestion(FaqData faqData) {
        HomePage homePage = new HomePage(driver);
        homePage.waitForPageLoad();

        System.out.println("Проверяем вопрос: " + faqData.getQuestionText());

        // Прокручиваем до секции FAQ
        homePage.scrollToFaqSection();

        // Нажимаем на вопрос
        homePage.clickFaqQuestion(faqData.getQuestionIndex());

        // Проверяем, что ответ отображается
        assertTrue(homePage.isFaqAnswerDisplayed(faqData.getQuestionIndex()),
                "Ответ на вопрос должен отображаться");

        // Получаем текст ответа
        String actualAnswer = homePage.getFaqAnswerText(faqData.getQuestionIndex());
        assertNotNull(actualAnswer, "Текст ответа не должен быть null");
        assertFalse(actualAnswer.isEmpty(), "Текст ответа не должен быть пустым");

        // Проверяем, что текст ответа соответствует ожидаемому
        assertTrue(actualAnswer.contains(faqData.getExpectedAnswer()),
                "Ответ должен содержать текст: " + faqData.getExpectedAnswer() +
                        "\nАктуальный ответ: " + actualAnswer);

        // Нажимаем еще раз, чтобы закрыть (проверяем интерактивность)
        homePage.clickFaqQuestion(faqData.getQuestionIndex());

        System.out.println("✓ Вопрос проверен успешно: " + faqData.getQuestionText());
    }

    @ParameterizedTest(name = "Проверка текста вопроса: {0}")
    @MethodSource("faqDataProvider")
    @DisplayName("Проверка текста вопросов в FAQ")
    public void testFaqQuestionText(FaqData faqData) {
        HomePage homePage = new HomePage(driver);
        homePage.waitForPageLoad();

        // Прокручиваем до секции FAQ
        homePage.scrollToFaqSection();

        // Проверяем текст вопроса
        String actualQuestionText = homePage.getFaqQuestionText(faqData.getQuestionIndex());
        assertEquals(faqData.getQuestionText(), actualQuestionText,
                "Текст вопроса должен соответствовать ожидаемому");

        System.out.println("✓ Текст вопроса проверен: " + faqData.getQuestionText());
    }
}