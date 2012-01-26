package org.eeichinger.testing.web;

import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;

/**
 * Manages the WebDriver lifecycle for plain unit tests
 *
 * @author Erich Eichinger
 * @since 26/01/12
 */
public class DriverLifecycleRule extends TestWatchman {

    @Override
    public void starting(FrameworkMethod method) {
        WebDriverFactory.getInstance().initializeWebDriver();
    }

    @Override
    public void finished(FrameworkMethod method) {
        WebDriverFactory.getInstance().getCurrentWebDriver().quit();
    }
}
