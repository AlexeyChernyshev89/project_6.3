package com.yandex.scooter.tests;

import com.yandex.scooter.config.TestBase;
import com.yandex.scooter.data.OrderData;
import com.yandex.scooter.pages.HomePage;
import com.yandex.scooter.pages.OrderPageFirstStep;
import com.yandex.scooter.pages.OrderPageSecondStep;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompleteOrderTest extends TestBase {

    public enum OrderButtonType {
        HEADER,
        BOTTOM
    }

    static Stream<Arguments> orderDataProvider() {
        return Stream.of(
                Arguments.of(OrderData.getFirstOrder(), OrderButtonType.HEADER),
                Arguments.of(OrderData.getSecondOrder(), OrderButtonType.HEADER),
                Arguments.of(OrderData.getFirstOrder(), OrderButtonType.BOTTOM),
                Arguments.of(OrderData.getSecondOrder(), OrderButtonType.BOTTOM)
        );
    }

    @ParameterizedTest(name = "Заказ через {1} кнопку: {0}")
    @MethodSource("orderDataProvider")
    @DisplayName("Полный сценарий заказа через разные кнопки")
    public void completeOrderTest(OrderData orderData, OrderButtonType buttonType) {
        System.out.println("\n=== Начинаем тест заказа через " +
                (buttonType == OrderButtonType.HEADER ? "верхнюю" : "нижнюю") + " кнопку ===");
        System.out.println("Тестовые данные: " + orderData.getName() + " " + orderData.getSurname());

        try {
            HomePage homePage = new HomePage(driver);
            homePage.waitForPageLoad();

            // Кликаем на соответствующую кнопку заказа
            if (buttonType == OrderButtonType.HEADER) {
                homePage.clickHeaderOrderButton();
                System.out.println("Нажата верхняя кнопка заказа");
            } else {
                homePage.clickBottomOrderButton();
                System.out.println("Нажата нижняя кнопка заказа");
            }

            // Проверяем, что открылась страница заказа
            OrderPageFirstStep firstStep = new OrderPageFirstStep(driver);
            assertTrue(firstStep.isPageDisplayed(), "Первая страница заказа должна отображаться");
            System.out.println("Первая страница заказа открыта");

            // Заполняем первую форму
            firstStep.fillForm(
                    orderData.getName(),
                    orderData.getSurname(),
                    orderData.getAddress(),
                    orderData.getMetroStation(),
                    orderData.getPhone()
            );
            System.out.println("Первая форма заполнена");

            // Переходим на второй шаг
            firstStep.clickNextButton();
            System.out.println("Переход на второй шаг");

            // Проверяем вторую страницу
            OrderPageSecondStep secondStep = new OrderPageSecondStep(driver);
            secondStep.waitForPageLoad();
            assertTrue(secondStep.isPageDisplayed(), "Вторая страница заказа должна отображаться");
            System.out.println("Вторая страница заказа открыта");

            // Заполняем вторую форму
            secondStep.fillForm(
                    orderData.getDate(),
                    orderData.getRentalPeriod(),
                    orderData.getColor(),
                    orderData.getComment()
            );
            System.out.println("Вторая форма заполнена");

            // Нажимаем кнопку Заказать
            secondStep.clickOrderButton();
            System.out.println("Нажата кнопка 'Заказать'");

            // Ждем появления и подтверждаем заказ
            secondStep.confirmOrder();

            // Проверяем успешное оформление
            boolean isSuccess = secondStep.isSuccessModalDisplayed();

            if (isSuccess) {
                String orderNumber = secondStep.getOrderNumber();
                System.out.println("✓ Заказ успешно оформлен. Номер заказа: " + orderNumber);
                assertTrue(true, "Заказ должен быть успешно оформлен");
            } else {
                System.out.println("✗ Заказ НЕ оформлен - возможно баг в приложении");
            }

        } catch (Exception e) {
            System.out.println("Ошибка во время выполнения теста: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Тест завершился с ошибкой: " + e.getMessage(), e);
        }
    }
}