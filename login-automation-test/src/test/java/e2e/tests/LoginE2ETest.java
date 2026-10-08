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

}
