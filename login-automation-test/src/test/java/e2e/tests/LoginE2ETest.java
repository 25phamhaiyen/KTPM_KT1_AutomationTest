package e2e.tests;

import e2e.base.BaseTest;
import e2e.pages.DashboardPage;
import e2e.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class LoginE2ETest extends BaseTest {

    // Common expected error messages
    private static final String ERR_EMPTY_USERNAME = "Bạn chưa nhập tên đăng nhập";
    private static final String ERR_EMPTY_PASSWORD = "Bạn chưa nhập mật khẩu";
    private static final String ERR_INVALID_CREDENTIALS = "Tài khoản hoặc mật khẩu không đúng.";

    @Test
    @DisplayName("TC_LOGIN_001: Bỏ trống Username -> Hiển thị lỗi 'Bạn chưa nhập tên đăng nhập'")
    void login_whenEmptyUsername_showsEmptyUsernameError() {
        LoginPage loginPage = new LoginPage(driver).open();

        loginPage.loginAs("", "123456");

        assertThat(loginPage.isOnLoginPage()).isTrue();
        assertThat(loginPage.isErrorMessageDisplayed()).isTrue();
        assertThat(loginPage.getErrorMessageText()).isEqualTo(ERR_EMPTY_USERNAME);
    }

    @Test
    @DisplayName("TC_LOGIN_002: Bỏ trống Password -> Hiển thị lỗi 'Bạn chưa nhập mật khẩu'")
    void login_whenEmptyPassword_showsEmptyPasswordError() {
        LoginPage loginPage = new LoginPage(driver).open();

        loginPage.loginAs("testuser", "");

        assertThat(loginPage.isOnLoginPage()).isTrue();
        assertThat(loginPage.isErrorMessageDisplayed()).isTrue();
        assertThat(loginPage.getErrorMessageText()).isEqualTo(ERR_EMPTY_PASSWORD);
    }

    @Test
    @DisplayName("TC_LOGIN_003: Bỏ trống cả Username và Password -> Hiển thị lỗi 'Bạn chưa nhập tên đăng nhập'")
    void login_whenBothEmpty_showsEmptyUsernameError() {
        LoginPage loginPage = new LoginPage(driver).open();

        loginPage.loginAs("", "");

        assertThat(loginPage.isOnLoginPage()).isTrue();
        assertThat(loginPage.isErrorMessageDisplayed()).isTrue();
        assertThat(loginPage.getErrorMessageText()).isEqualTo(ERR_EMPTY_USERNAME);
    }

    @Test
    @DisplayName("TC_LOGIN_004: Username không tồn tại -> Báo 'Tài khoản hoặc mật khẩu không đúng.'")
    void login_whenNonExistentUsername_showsInvalidCredentialsError() {
        LoginPage loginPage = new LoginPage(driver).open();

        loginPage.loginAs("nonexistent_user_9999", "ValidPass123!");

        assertThat(loginPage.isOnLoginPage()).isTrue();
        assertThat(loginPage.isErrorMessageDisplayed()).isTrue();
        assertThat(loginPage.getErrorMessageText()).isEqualTo(ERR_INVALID_CREDENTIALS);
    }

    @Test
    @DisplayName("TC_LOGIN_005: Sai mật khẩu -> vẫn ở lại trang Login và báo lỗi")
    void login_whenWrongPassword_staysOnLoginPage() {
        LoginPage loginPage = new LoginPage(driver).open();

        loginPage.loginAs("sinhvien01", "SaiMatKhau");

        assertThat(loginPage.isOnLoginPage()).isTrue();
        assertThat(loginPage.isErrorMessageDisplayed()).isTrue();
        assertThat(loginPage.getErrorMessageText()).isEqualTo(ERR_INVALID_CREDENTIALS);
    }

    @Test
    @DisplayName("TC_LOGIN_006: Cả Username và Password đều sai -> Báo lỗi không đúng")
    void login_whenBothWrong_showsInvalidCredentialsError() {
        LoginPage loginPage = new LoginPage(driver).open();

        loginPage.loginAs("invalid_user_utc", "invalid_pwd_utc");

        assertThat(loginPage.isOnLoginPage()).isTrue();
        assertThat(loginPage.isErrorMessageDisplayed()).isTrue();
        assertThat(loginPage.getErrorMessageText()).isEqualTo(ERR_INVALID_CREDENTIALS);
    }

    @Test
    @DisplayName("TC_LOGIN_007: Username có khoảng trắng -> Báo lỗi hoặc xử lý an toàn")
    void login_whenUsernameHasSpaces_showsError() {
        LoginPage loginPage = new LoginPage(driver).open();

        loginPage.loginAs("  validuser  ", "password123");

        assertThat(loginPage.isOnLoginPage()).isTrue();
        assertThat(loginPage.isErrorMessageDisplayed()).isTrue();
        assertThat(loginPage.getErrorMessageText()).isEqualTo(ERR_INVALID_CREDENTIALS);
    }

    @Test
    @DisplayName("TC_LOGIN_008: Password chứa khoảng trắng -> Giữ nguyên chuỗi và báo lỗi")
    void login_whenPasswordHasSpaces_showsError() {
        LoginPage loginPage = new LoginPage(driver).open();

        loginPage.loginAs("validuser", "pass word 123");

        assertThat(loginPage.isOnLoginPage()).isTrue();
        assertThat(loginPage.isErrorMessageDisplayed()).isTrue();
        assertThat(loginPage.getErrorMessageText()).isEqualTo(ERR_INVALID_CREDENTIALS);
    }

    @Test
    @DisplayName("TC_LOGIN_009: Username chứa ký tự đặc biệt -> Hệ thống không crash")
    void login_whenUsernameSpecialChars_handlesSafely() {
        LoginPage loginPage = new LoginPage(driver).open();

        loginPage.loginAs("user_#$!@%*", "123456");

        assertThat(loginPage.isOnLoginPage()).isTrue();
        assertThat(loginPage.isErrorMessageDisplayed()).isTrue();
        assertThat(loginPage.getErrorMessageText()).isEqualTo(ERR_INVALID_CREDENTIALS);
    }

    @Test
    @DisplayName("TC_LOGIN_010: Password 1 ký tự -> Báo lỗi an toàn")
    void login_whenShortPassword_handlesSafely() {
        LoginPage loginPage = new LoginPage(driver).open();

        loginPage.loginAs("testuser", "1");

        assertThat(loginPage.isOnLoginPage()).isTrue();
        assertThat(loginPage.isErrorMessageDisplayed()).isTrue();
        assertThat(loginPage.getErrorMessageText()).isEqualTo(ERR_INVALID_CREDENTIALS);
    }

    @Test
    @DisplayName("TC_LOGIN_011: Đăng nhập thành công -> rời khỏi trang Login")
    void login_whenValidCredentials_leavesLoginPage() {
        String utcUser = System.getenv("UTC_USER");
        String utcPass = System.getenv("UTC_PASS");

        LoginPage loginPage = new LoginPage(driver).open();

        if (utcUser == null || utcPass == null || utcUser.trim().isEmpty() || utcPass.trim().isEmpty()) {
            // Attempt to login with null/placeholder and fail with explicit message
            loginPage.loginAs("INVALID_PLACEHOLDER", "INVALID_PLACEHOLDER");
            fail("Valid UTC credentials are required for this test.");
            return;
        }

        DashboardPage dashboardPage = loginPage.loginAs(utcUser, utcPass);

        assertThat(loginPage.isOnLoginPage()).isFalse();
        assertThat(dashboardPage.isLoaded()).isTrue();
    }

    @Test
    @DisplayName("TC_LOGIN_012: Đăng nhập bằng phím Enter tại ô Password")
    void login_whenPressEnter_submitsForm() {
        LoginPage loginPage = new LoginPage(driver).open();

        loginPage.enterUsername("testuser");
        loginPage.enterPassword("somepassword");
        loginPage.submitViaEnterKey();

        assertThat(loginPage.isOnLoginPage()).isTrue();
        assertThat(loginPage.isErrorMessageDisplayed()).isTrue();
        assertThat(loginPage.getErrorMessageText()).isEqualTo(ERR_INVALID_CREDENTIALS);
    }

    @Test
    @DisplayName("TC_LOGIN_015: Click 'Đăng nhập bằng e-mail UTC' -> Chuyển hướng Google OAuth")
    void login_whenClickGoogleOAuth_redirectsToGoogleAccounts() {
        LoginPage loginPage = new LoginPage(driver).open();

        loginPage.clickGoogleLogin();

        assertThat(driver.getCurrentUrl()).contains("accounts.google.com");
    }

    @Test
    @DisplayName("TC_LOGIN_016: Password được che (type='password')")
    void login_passwordField_isMasked() {
        LoginPage loginPage = new LoginPage(driver).open();

        assertThat(loginPage.getPasswordInputType()).isEqualTo("password");
    }

    @Test
    @DisplayName("TC_LOGIN_017: Website không có nút hiện/ẩn password (đúng theo thiết kế giao diện)")
    void login_passwordVisibilityToggle_matchesDesign() {
        LoginPage loginPage = new LoginPage(driver).open();

        assertThat(loginPage.isPasswordVisibilityTogglePresent()).isFalse();
    }

    @Test
    @DisplayName("TC_LOGIN_018: Nút Đăng nhập hiển thị và có thể click")
    void login_button_isClickable() {
        LoginPage loginPage = new LoginPage(driver).open();

        assertThat(loginPage.isLoginButtonEnabled()).isTrue();
    }

    @Test
    @DisplayName("TC_LOGIN_019: Click 'Bạn quên mật khẩu đăng nhập ?' -> Chuyển hướng /Login/GetPass")
    void login_whenClickForgotPassword_navigatesToGetPass() {
        LoginPage loginPage = new LoginPage(driver).open();

        loginPage.clickForgotPassword();

        assertThat(driver.getCurrentUrl()).contains("/Login/GetPass");
    }

    @Test
    @DisplayName("TC_LOGIN_020: Placeholder ô Username và Password đúng chuẩn")
    void login_placeholders_areCorrect() {
        LoginPage loginPage = new LoginPage(driver).open();

        assertThat(loginPage.getUsernamePlaceholder()).isEqualTo("Tên đăng nhập");
        assertThat(loginPage.getPasswordPlaceholder()).isEqualTo("Mật khẩu");
    }

    @Test
    @DisplayName("TC_LOGIN_021: Trạng thái Checkbox 'Giữ tôi luôn đăng nhập' (Remember Me)")
    void login_rememberMeCheckbox_canToggle() {
        LoginPage loginPage = new LoginPage(driver).open();

        assertThat(loginPage.isRememberMeChecked()).isFalse();

        loginPage.toggleRememberMe();
        assertThat(loginPage.isRememberMeChecked()).isTrue();

        loginPage.toggleRememberMe();
        assertThat(loginPage.isRememberMeChecked()).isFalse();
    }
}
