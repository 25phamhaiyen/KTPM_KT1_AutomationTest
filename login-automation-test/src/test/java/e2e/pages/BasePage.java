package e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
            driver,
            Duration.ofSeconds(10)
        );
    }

    protected void click(By locator) {
        wait.until(
            ExpectedConditions.elementToBeClickable(locator)
        ).click();
    }

    protected void type(By locator, String text) {
        WebElement el = wait.until(
            ExpectedConditions.visibilityOfElementLocated(locator)
        );
        el.clear();
        if (text != null) {
            el.sendKeys(text);
        }
    }

    protected String getText(By locator) {
        return wait.until(
            ExpectedConditions.visibilityOfElementLocated(locator)
        ).getText();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
            ).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected String getAttribute(By locator, String attributeName) {
        return wait.until(
            ExpectedConditions.visibilityOfElementLocated(locator)
        ).getAttribute(attributeName);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public boolean waitForUrlContains(String partialUrl) {
        try {
            return wait.until(ExpectedConditions.urlContains(partialUrl));
        } catch (Exception e) {
            return false;
        }
    }
}
