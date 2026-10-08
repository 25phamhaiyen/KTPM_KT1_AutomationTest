package e2e.base;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;

/** Attaches a browser screenshot to the Allure result when a test fails. */
public class FailedTestScreenshotExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (context.getExecutionException().isEmpty()) {
            return;
        }

        context.getTestInstance()
            .filter(BaseTest.class::isInstance)
            .map(BaseTest.class::cast)
            .map(test -> test.driver)
            .filter(TakesScreenshot.class::isInstance)
            .map(TakesScreenshot.class::cast)
            .ifPresent(driver -> {
                try {
                    byte[] screenshot = driver.getScreenshotAs(OutputType.BYTES);
                    Allure.addAttachment("Failure screenshot", "image/png",
                        new ByteArrayInputStream(screenshot), ".png");
                } catch (RuntimeException ignored) {
                    // Keep the original test failure when the browser cannot provide a screenshot.
                }
            });
    }
}
