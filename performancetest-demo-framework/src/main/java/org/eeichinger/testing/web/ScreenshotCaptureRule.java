package org.eeichinger.testing.web;

import org.apache.commons.io.FileUtils;
import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Capture screenshots on test failure and save them to 
 * System.getProperty("user.dir") + "/target/surefire-reports"
 * 
 * @author Neale Upstone/OpenCredo
 * @author Erich Eichinger/OpenCredo
 */
public final class ScreenshotCaptureRule extends TestWatchman {

    protected final Logger log = LoggerFactory.getLogger(getClass());

	private final String screenshotPath;

    public ScreenshotCaptureRule() {
        this(System.getProperty("user.dir") + "/target/surefire-reports");
    }

    public ScreenshotCaptureRule(String screenshotPath) {
        this.screenshotPath = screenshotPath;
    }

    public void failed(Throwable e, FrameworkMethod method) {
		String methodName = method.getMethod().getName();
		log.error(String.format("Failure in %s.%s", method.getMethod().getDeclaringClass().getName(), methodName), e);
		if (Settings.TAKE_ERROR_SCREENSHOTS) {
			captureScreen(screenshotPath, methodName);
		}
	}

	public void captureScreen(String screenshotPath, String methodName) {
		captureEntirePageScreenshot(screenshotPath + "/" + methodName + "_" + "top.png");
	}

	public void captureEntirePageScreenshot(String fileName) {
        if (!Settings.TAKE_ERROR_SCREENSHOTS)
            return;

        WebDriver driver = WebDriverFactory.getInstance().getCurrentWebDriver();
		File screenshotAs;
        try {
            screenshotAs = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        } catch (UnsupportedOperationException e) {
            log.warn("Driver {} doesn't support screenshots - page source was:\n {}", driver.getClass().getName(), driver.getPageSource());
            return;
        }
        try {
			FileUtils.copyFile(screenshotAs, new File(fileName));
			log.info("Screenshot saved to {}", fileName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
