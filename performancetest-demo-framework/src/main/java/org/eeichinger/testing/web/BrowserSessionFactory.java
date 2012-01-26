package org.eeichinger.testing.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.openqa.selenium.support.events.EventFiringWebDriver;

/**
 * A BrowserSessionFactory produces on demand a runnable browser session.
 * The session duration is limited to a maximum of maxSessionDuration after which the session aborts
 * with a TimeoutThresholdException
 *
 * @author: Erich Eichinger
 * @date: 25/01/12
 */
public class BrowserSessionFactory<T extends Runnable> {
    private final Class<T> profileClass;
    private final int delayMillis;
    private final int maxSessionDuration;

    public BrowserSessionFactory(Class<T> profileClass, int delayMillis, int maxSessionDuration) {
        this.profileClass = profileClass;
        this.delayMillis = delayMillis;
        this.maxSessionDuration = maxSessionDuration;
    }

    public Class<T> getProfileClass() {
        return profileClass;
    }

    public Runnable newRunnable() {
        return SchedulingUtils.newRunnableWithTimeout(maxSessionDuration, new Runnable() {
            @Override
            public void run() {
                WebDriver webDriver = WebDriverFactory.getInstance().initializeWebDriver();
                if (webDriver instanceof EventFiringWebDriver) {
                    ((EventFiringWebDriver)webDriver).register(new AbstractWebDriverEventListener() {
                        @Override
                        public void afterClickOn(WebElement element, WebDriver driver) {
                            // put in some delay for performance/load tests
                            if (delayMillis > 0) {
                                try {
                                    Thread.sleep(Math.round(Math.random() * ((double) delayMillis)));
                                } catch (InterruptedException e) {
                                    // ignore interruptions
                                }
                            }
                        }
                    });

                }
                webDriver.manage().deleteAllCookies();
                try {
                    profileClass.newInstance().run();
                } catch (Exception e) {
                    throw new RuntimeException("failed to execute browser session " + profileClass.getName());
                } finally {
                    WebDriverFactory.getInstance().getCurrentWebDriver().quit();
                }
            }
        });
    }
}
