package com.yandex.scooter.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HomePage {
    private WebDriver driver;

    // Заголовок страницы
    @FindBy(className = "Home_Header__iJKdX")
    private WebElement pageTitle;

    // Верхняя кнопка "Заказать"
    @FindBy(xpath = "//div[@class='Header_Nav__AGCXC']/button[text()='Заказать']")
    private WebElement headerOrderButton;

    // Нижняя кнопка "Заказать"
    @FindBy(xpath = "//div[@class='Home_FinishButton__1_cWm']/button")
    private WebElement bottomOrderButton;

    // Секция FAQ
    @FindBy(className = "Home_FAQ__3uVm4")
    private WebElement faqSection;

    // Все вопросы FAQ
    @FindBy(css = "[data-accordion-component='AccordionItemButton']")
    private List<WebElement> faqQuestions;

    // Все ответы FAQ
    @FindBy(css = "[data-accordion-component='AccordionItemPanel']")
    private List<WebElement> faqAnswers;

    // Баннер с куками
    @FindBy(className = "App_CookieConsent__1yUIN")
    private WebElement cookieBanner;

    // Кнопка закрытия баннера куки
    @FindBy(xpath = "//button[contains(text(), 'да все привыкли')]")
    private WebElement cookieAcceptButton;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void waitForPageLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(pageTitle));
    }

    public void closeCookieBannerIfPresent() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            if (wait.until(ExpectedConditions.visibilityOf(cookieBanner)).isDisplayed()) {
                cookieAcceptButton.click();
                System.out.println("Баннер с куками закрыт");
            }
        } catch (Exception e) {
            // Баннер не найден, это нормально
        }
    }

    public void clickHeaderOrderButton() {
        headerOrderButton.click();
    }

    public void clickBottomOrderButton() {
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", bottomOrderButton);
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(bottomOrderButton));
        bottomOrderButton.click();
    }

    // Методы для работы с FAQ

    public void scrollToFaqSection() {
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", faqSection);
    }

    public void clickFaqQuestion(int index) {
        if (index >= 0 && index < faqQuestions.size()) {
            WebElement question = faqQuestions.get(index);
            ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView(true);", question);
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(question));
            question.click();
        }
    }

    public String getFaqQuestionText(int index) {
        if (index >= 0 && index < faqQuestions.size()) {
            return faqQuestions.get(index).getText();
        }
        return "";
    }

    public String getFaqAnswerText(int index) {
        if (index >= 0 && index < faqAnswers.size()) {
            try {
                // Ждем, пока ответ станет видимым
                new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.visibilityOf(faqAnswers.get(index)));
                return faqAnswers.get(index).getText();
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }

    public boolean isFaqAnswerDisplayed(int index) {
        if (index >= 0 && index < faqAnswers.size()) {
            try {
                return faqAnswers.get(index).isDisplayed();
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public int getFaqQuestionsCount() {
        return faqQuestions.size();
    }
}