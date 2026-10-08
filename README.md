# UTC Office Login Automation Test

Dự án kiểm thử tự động (Automation Testing) cho chức năng **ĐĂNG NHẬP** của website:
👉 **URL mục tiêu:** [https://vanphongdientu.utc.edu.vn/Login](https://vanphongdientu.utc.edu.vn/Login)

Dự án được xây dựng theo mô hình **Page Object Model (POM)** chuẩn, sử dụng **Java**, **Selenium WebDriver 4**, **JUnit 5 Jupiter** và thư viện fluent assertion **AssertJ**.

---

## 1. Yêu cầu môi trường (Prerequisites)

- **Java Development Kit (JDK):** JDK 17 trở lên (Hỗ trợ tốt JDK 17 - JDK 25).
- **Apache Maven:** Phiên bản 3.9+ (Đã tích hợp sẵn Maven portable trong thư mục `tools/apache-maven-3.9.16` hoặc cài sẵn trên máy).
- **Trình duyệt Web:** Google Chrome (hoặc Microsoft Edge). Selenium 4 tự động quản lý browser driver tương thích via Selenium Manager.

---

## 2. Cấu trúc dự án (Project Architecture)

Dự án tuân thủ nghiêm ngặt nguyên tắc phân tách trách nhiệm trong Page Object Model:

```text
login-automation-test/
├── pom.xml                                      # Quản lý dependencies (Selenium 4, JUnit 5 Jupiter, AssertJ, Surefire)
├── README.md                                    # Hướng dẫn chi tiết dự án
│
└── src/
    └── test/
        └── java/
            └── e2e/
                ├── base/
                │   └── BaseTest.java            # Khởi tạo ChromeDriver, quản lý vòng đời @BeforeEach / @AfterEach
                │
                ├── pages/
                │   ├── BasePage.java            # Lớp cơ sở trừu tượng: WebDriver, WebDriverWait, helper methods (click, type, getText)
                │   ├── LoginPage.java           # Page Object cho trang Login: quản lý locators và business actions (loginAs, open, ...)
                │   └── DashboardPage.java       # Page Object đại diện cho trang sau khi đăng nhập thành công
                │
                └── tests/
                    └── LoginE2ETest.java        # Lớp kiểm thử E2E: chứa kịch bản, test data, assertion (tuyệt đối không chứa selector)
```

### Sơ đồ luồng tương tác:

```text
BaseTest
   │ (kế thừa)
   ▼
LoginE2ETest
   │ (gọi hành vi)
   ▼
LoginPage ────────── loginAs(...) [thành công] ──────────> DashboardPage
   │ (kế thừa)                                                    │ (kế thừa)
   ▼                                                             ▼
BasePage                                                      BasePage
```

---

## 3. Cài đặt và Chạy kiểm thử (Execution)

### 3.1. Cài đặt dependencies

Maven sẽ tự động tải các dependencies khi biên dịch lần đầu:

```powershell
cd login-automation-test
mvn clean test-compile
```

### 3.2. Chạy toàn bộ test suite

**Cách 1: Sử dụng Maven đã cài trong hệ thống**

```powershell
cd login-automation-test
mvn clean test
```

**Cách 2: Sử dụng bộ Maven portable đi kèm dự án**
Từ thư mục gốc `selniumWebDriver`:

```powershell
.\tools\apache-maven-3.9.16\bin\mvn.cmd clean test -f .\login-automation-test\pom.xml
```

### 3.3. Chạy ở chế độ Headless (không mở cửa sổ trình duyệt)

```powershell
mvn clean test -Dheadless=true
```

### 3.4. Chạy một test case cụ thể

```powershell
mvn test -Dtest=LoginE2ETest#login_whenEmptyUsername_showsEmptyUsernameError
```

---

## 4. Quản lý Tài khoản kiểm thử (Test Account Configuration)

Test case kiểm tra đăng nhập thành công (`TC_LOGIN_011: login_whenValidCredentials_leavesLoginPage`) sử dụng biến môi trường:

- `UTC_USER`: Tên đăng nhập tài khoản UTC hợp lệ.
- `UTC_PASS`: Mật khẩu tài khoản UTC hợp lệ.

> [!IMPORTANT]
> **Tình trạng kiểm thử hiện tại:**
> **Login success test requires a valid test account. Currently expected to FAIL when valid credentials are unavailable.**
>
> - Khi biến môi trường `UTC_USER` và `UTC_PASS` chưa được cấu hình, test case sẽ thực hiện tương tác nhập dữ liệu và assert thất bại một cách minh bạch với thông báo:
>   `"Valid UTC credentials are required for this test."`
> - Dự án **tuyệt đối không giả lập login success, không skip test và không fake PASS**.

### Cách thiết lập tài khoản khi kiểm thử thực tế:

- **Trên Windows PowerShell:**
  ```powershell
  $env:UTC_USER="ten_tai_khoan_that"
  $env:UTC_PASS="mat_khau_that"
  mvn clean test
  ```
- **Trên Windows Command Prompt (CMD):**
  ```cmd
  set UTC_USER=ten_tai_khoan_that
  set UTC_PASS=mat_khau_that
  mvn clean test
  ```

---

## 5. Danh mục Test Cases (Test Suite Coverage)

Toàn bộ test methods trong `LoginE2ETest.java` được ánh xạ trực tiếp từ file tài liệu `Login_Test_Cases.xlsx`:

| STT | Mã Test Case   | Tên Kịch Bản / Mô Tả                                              |    Nhóm    |       Kết quả hiện tại       |
| :-: | :------------- | :---------------------------------------------------------------- | :--------: | :--------------------------: |
|  1  | `TC_LOGIN_001` | Bỏ trống ô Username                                               | Validation |           **PASS**           |
|  2  | `TC_LOGIN_002` | Bỏ trống ô Password                                               | Validation |           **PASS**           |
|  3  | `TC_LOGIN_003` | Bỏ trống cả Username và Password                                  | Validation |           **PASS**           |
|  4  | `TC_LOGIN_004` | Đăng nhập với Username không tồn tại                              | Validation |           **PASS**           |
|  5  | `TC_LOGIN_005` | Đăng nhập sai mật khẩu (giữ nguyên trang Login)                   | Validation |           **PASS**           |
|  6  | `TC_LOGIN_006` | Cả Username và Password đều sai                                   | Validation |           **PASS**           |
|  7  | `TC_LOGIN_007` | Username chứa khoảng trắng ở đầu/cuối                             | Validation |           **PASS**           |
|  8  | `TC_LOGIN_008` | Password chứa ký tự khoảng trắng                                  | Validation |           **PASS**           |
|  9  | `TC_LOGIN_009` | Username chứa ký tự đặc biệt                                      | Validation |           **PASS**           |
| 10  | `TC_LOGIN_010` | Password độ dài bất thường (1 ký tự)                              | Validation |           **PASS**           |
| 11  | `TC_LOGIN_011` | Đăng nhập thành công -> rời khỏi trang Login                      | Functional | **FAIL (Chưa có tài khoản)** |
| 12  | `TC_LOGIN_012` | Đăng nhập bằng cách nhấn phím Enter tại ô Password                | Functional |           **PASS**           |
| 13  | `TC_LOGIN_015` | Kiểm tra liên kết "Đăng nhập bằng e-mail UTC" (Google OAuth)      | Functional |           **PASS**           |
| 14  | `TC_LOGIN_016` | Mật khẩu được che mặc định (`type="password"`)                    |     UI     |           **PASS**           |
| 15  | `TC_LOGIN_017` | Kiểm tra nút hiện/ẩn mật khẩu (đúng với thiết kế giao diện)       |     UI     |           **PASS**           |
| 16  | `TC_LOGIN_018` | Nút Đăng nhập hiển thị và có thể click                            |     UI     |           **PASS**           |
| 17  | `TC_LOGIN_019` | Link "Bạn quên mật khẩu đăng nhập ?" điều hướng `/Login/GetPass`  |     UI     |           **PASS**           |
| 18  | `TC_LOGIN_020` | Kiểm tra nội dung thuộc tính placeholder của Username và Password |     UI     |           **PASS**           |
| 19  | `TC_LOGIN_021` | Trạng thái chuyển đổi của checkbox "Giữ tôi luôn đăng nhập"       |     UI     |           **PASS**           |
| 20  | `TC_LOGIN_023` | Nhập chuỗi 500 ký tự vào ô Username                               |  Security  |           **PASS**           |
| 21  | `TC_LOGIN_024` | Nhập chuỗi 500 ký tự vào ô Password                               |  Security  |           **PASS**           |
| 22  | `TC_LOGIN_025` | Nhập chuỗi SQL Injection cơ bản (`' OR 1=1 --`)                   |  Security  |           **PASS**           |
| 23  | `TC_LOGIN_026` | Nhập payload XSS cơ bản (`<script>alert(1)</script>`)             |  Security  |           **PASS**           |
| 24  | `TC_LOGIN_027` | Đăng nhập sai nhiều lần liên tiếp                                 |  Security  |           **PASS**           |

---

## 6. Xem Báo cáo kiểm thử (Test Reports)

Sau khi chạy xong lệnh `mvn clean test`, báo cáo chi tiết được sinh tự động tại:

- **XML Reports:** `login-automation-test/target/surefire-reports/TEST-e2e.tests.LoginE2ETest.xml`
- **Text Summary:** `login-automation-test/target/surefire-reports/e2e.tests.LoginE2ETest.txt`

---

## 7. Hướng dẫn bổ sung Test Case mới

Khi cần mở rộng thêm kịch bản kiểm thử:

1. **Bổ sung locator/hành vi trong Page Object:**
   Mở file [`LoginPage.java`](file:///d:/newcode/Nam4/KTPM/selniumWebDriver/login-automation-test/src/test/java/e2e/pages/LoginPage.java) để khai báo thêm locator (dạng `private`) và method hành vi tương ứng (nếu chưa có).
2. **Viết test scenario trong test class:**
   Mở file [`LoginE2ETest.java`](file:///d:/newcode/Nam4/KTPM/selniumWebDriver/login-automation-test/src/test/java/e2e/tests/LoginE2ETest.java), viết method mới có annotation `@Test` và `@DisplayName("...")`:
   ```java
   @Test
   @DisplayName("TC_NEW_001: Mô tả kịch bản kiểm thử mới")
   void login_newScenario_shouldBehaveProperly() {
       LoginPage loginPage = new LoginPage(driver).open();
       // Gọi các action methods từ loginPage và thực hiện assert
   }
   ```
3. **Cập nhật tài liệu Excel:**
   Bổ sung dòng thông tin tương ứng vào file `Login_Test_Cases.xlsx`.

4. **Allure Report sau khi chạy 25 test case:**
   <img src="./login-automation-test/target/site/allure-maven-plugin/report.png">
