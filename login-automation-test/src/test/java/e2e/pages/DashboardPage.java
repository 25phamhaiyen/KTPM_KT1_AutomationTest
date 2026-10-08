package e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends BasePage {

    // Actual elements that may appear after login in UTC Office (Desk / Main workspace)
    // When credentials are not available, isDisplayed() relies on verifying the user navigated away from /Login
    private final By mainContent = By.cssSelector("div.main, div.container, #content, .workspace");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return !getCurrentUrl().contains("/Login") && isDisplayed(mainContent);
    }

    public boolean isLoaded() {
        return !getCurrentUrl().contains("/Login");
    }
}
