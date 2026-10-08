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
}
