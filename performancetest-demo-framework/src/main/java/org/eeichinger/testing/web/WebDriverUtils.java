package org.eeichinger.testing.web;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.fail;

/**
 * @author Erich Eichinger/OpenCredo
 * @author Neale Upstone/OpenCredo
 * @since 26/01/12
 */
public class WebDriverUtils {

    public static void waitForPageLoaded(WebDriver webDriver, int maxPageTimeoutSeconds) {
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript(
                        "return document.readyState").equals("complete");
            }
        };

        Wait<WebDriver> wait = new WebDriverWait(webDriver, maxPageTimeoutSeconds);
        try {
            wait.until(expectation);
        } catch (Throwable error) {
            fail("Timeout waiting for Page Load Request to complete.");
        }
    }
}
