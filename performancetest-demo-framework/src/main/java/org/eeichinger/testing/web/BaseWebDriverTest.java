package org.eeichinger.testing.web;

import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convenience base class for webdriver test cases
 *
 * @author Erich Eichinger/OpenCredo
 * @author Neale Upstone/OpenCredo
 */
public class BaseWebDriverTest {
    // note that rules are executed bottom up - hence DriverLifecycle must be last!
	@Rule public final TestTracerRule loggerRule = new TestTracerRule();
    @Rule public final MethodRule screenshotRule = new ScreenshotCaptureRule();
    @Rule public final MethodRule driverLifecycle = new DriverLifecycleRule();

    protected final Logger log = LoggerFactory.getLogger(getClass());
}
