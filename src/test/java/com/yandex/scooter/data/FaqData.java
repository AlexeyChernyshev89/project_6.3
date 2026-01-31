package com.yandex.scooter.data;

public class FaqData {
    private final int questionIndex;
    private final String questionText;
    private final String expectedAnswer;

    public FaqData(int questionIndex, String questionText, String expectedAnswer) {
        this.questionIndex = questionIndex;
        this.questionText = questionText;
        this.expectedAnswer = expectedAnswer;
    }

    public int getQuestionIndex() {
        return questionIndex;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getExpectedAnswer() {
        return expectedAnswer;
    }

    @Override
    public String toString() {
        return "Вопрос " + (questionIndex + 1) + ": " + questionText;
    }

    // Фабричный метод для создания всех FAQ вопросов
    public static FaqData[] getAllFaqQuestions() {
        return new FaqData[]{
                new FaqData(0, "Сколько это стоит? И как оплатить?",
                        "Сутки — 400 рублей. Оплата курьеру — наличными или картой."),
                new FaqData(1, "Хочу сразу несколько самокатов! Так можно?",
                        "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим."),
                new FaqData(2, "Как рассчитывается время аренды?",
                        "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30."),
                new FaqData(3, "Можно ли заказать самокат прямо на сегодня?",
                        "Только начиная с завтрашнего дня. Но скоро станем расторопнее."),
                new FaqData(4, "Можно ли продлить заказ или вернуть самокат раньше?",
                        "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010."),
                new FaqData(5, "Вы привозите зарядку вместе с самокатом?",
                        "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится."),
                new FaqData(6, "Можно ли отменить заказ?",
                        "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои."),
                new FaqData(7, "Я жизу за МКАДом, привезёте?",
                        "Да, обязательно. Всем самокатов! И Москве, и Московской области.")
        };
    }
}