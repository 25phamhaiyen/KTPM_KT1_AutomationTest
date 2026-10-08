package e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class LoginPage extends BasePage {

    public static final String URL = "https://vanphongdientu.utc.edu.vn/Login";

    // Exact locators verified on https://vanphongdientu.utc.edu.vn/Login
    private final By usernameField = By.name("username");
    private final By passwordField = By.name("userpwd");
    private final By loginButton = By.cssSelector("input.submit_login");
    private final By rememberMeCheckbox = By.id("persistent");
    private final By rememberMeLabel = By.cssSelector("label[for='persistent']");
    private final By forgotPasswordLink = By.cssSelector("div.helps a[href='/Login/GetPass']");
    private final By googleLoginButton = By.cssSelector("a.button");
    private final By errorMessage = By.cssSelector("div.error");
    private final By passwordVisibilityToggle = By.cssSelector(".show-password, .toggle-password, .eye-icon");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(URL);
        return this;
    }

    public LoginPage enterUsername(String username) {
        type(usernameField, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(passwordField, password);
        return this;
    }

    public DashboardPage loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        click(loginButton);
        return new DashboardPage(driver);
    }

    public void clickLogin() {
        click(loginButton);
    }

    public void submitViaEnterKey() {
        WebElement passEl = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(passwordField));
        passEl.sendKeys(Keys.ENTER);
    }

    public LoginPage toggleRememberMe() {
        try {
            click(rememberMeLabel);
        } catch (Exception e) {
            click(rememberMeCheckbox);
        }
        return this;
    }

    public boolean isRememberMeChecked() {
        return driver.findElement(rememberMeCheckbox).isSelected();
    }

    public void clickForgotPassword() {
        click(forgotPasswordLink);
    }

    public void clickGoogleLogin() {
        click(googleLoginButton);
    }

    public boolean isOnLoginPage() {
        return driver.getCurrentUrl().contains("/Login");
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }

    public String getErrorMessageText() {
        return getText(errorMessage).trim();
    }

    public String getPasswordInputType() {
        return getAttribute(passwordField, "type");
    }

    public String getUsernamePlaceholder() {
        return getAttribute(usernameField, "placeholder");
    }

    public String getPasswordPlaceholder() {
        return getAttribute(passwordField, "placeholder");
    }

    public boolean isPasswordVisibilityTogglePresent() {
        List<WebElement> toggles = driver.findElements(passwordVisibilityToggle);
        return !toggles.isEmpty();
    }

    public boolean isLoginButtonEnabled() {
        try {
            return driver.findElement(loginButton).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
}
