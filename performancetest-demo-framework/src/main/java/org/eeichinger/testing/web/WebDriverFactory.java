package org.eeichinger.testing.web;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ClasspathExtension;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory to create webdriver instances
 *
 * @author Neale Upstone/OpenCredo
 * @author Erich Eichinger/OpenCredo
 */
public class WebDriverFactory {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private static WebDriverFactory instance = new WebDriverFactory();
	
	/**
	 * To avoid creating unnecessary UI driver instances, we cache them per thread and clean up 
	 * after class.<p>
	 * TODO: If we get to running multiple suites in parallel, then this collection will be being shared
	 * by multiple subclasses having @AfterClass called.  We'll need to remove UserUI entries from the
	 * map when that happens, or look at providing a pool.
	 */
	private Map<Thread,WebDriver> webDriverPerThread = new HashMap<Thread,WebDriver>();

    /**
     * If required set your own WebDriverFactory implementation
     *
     * @param instance
     */
    public static void setInstance(WebDriverFactory instance) {
        if (instance == null) {
            throw new IllegalArgumentException("instance must not be null");
        }
        WebDriverFactory.instance = instance;
    }

    public static WebDriverFactory getInstance() {
		return instance;
	}

    public String getBaseUrl() {
        return Settings.APPLICATION_URL;
    }

	public void closeAllInstances() {
		log.debug("Closing WebDriver instances...");
		synchronized (webDriverPerThread) {
			for (Entry<Thread, WebDriver> entry : webDriverPerThread.entrySet()) {
				entry.getValue().quit();
				webDriverPerThread.remove(entry.getKey());
			}
		}
		log.debug("Closed WebDriver instances");
	}

    /**
     * Force (re-)initialization of the current thread's webdriver instance.
     * Any previously existing webdriver instance will be quit()ed
     *
     * @return the newly initialized webdriver instance
     */
    public WebDriver initializeWebDriver() {
        synchronized (webDriverPerThread) {
            final Thread instanceKey = Thread.currentThread();
            WebDriver webDriver = webDriverPerThread.get(instanceKey);
            if (webDriver != null) {
                webDriver.quit();
            }

            log.debug("Creating WebDriver instance for thread {}", instanceKey.getName());
            webDriver = createNewWebDriver(instanceKey);
            webDriverPerThread.put(instanceKey, webDriver);

            return webDriver;
        }
    }

	public WebDriver getCurrentWebDriver() {
		synchronized (webDriverPerThread) {
			final Thread instanceKey = Thread.currentThread();
            WebDriver webDriver = webDriverPerThread.get(instanceKey);
            if (webDriver == null) {
                throw new UnsupportedOperationException("must initialize driver first");
			}
            return webDriver;
		}
	}

    protected WebDriver createNewWebDriver(final Thread instanceKey) {
        WebDriver webDriver = Settings.RUN_HEADLESS ? new HtmlUnitDriver(true) : createFirefoxDriver();
        // make sure the driver is removed from the map once quit() or close() are called
        EventFiringWebDriver eventFiringWebDriver = new EventFiringWebDriver(webDriver) {
            @Override
            public void close() {
                this.quit();
            }

            @Override
            public void quit() {
                synchronized (webDriverPerThread) {
                    webDriverPerThread.remove(instanceKey);
                }
                super.quit();
                log.debug("{} WebDriver instance quited on thread {}", this.getClass().getSimpleName(), instanceKey.getName());
            }
        };
        return eventFiringWebDriver;
    }

	private FirefoxDriver createFirefoxDriver() {
		
		// Create template directory if doesn't exist, and unzip extension once
		File dir = new File("target/firefox-template");
		if (!dir.exists()) {
			dir.mkdir();
		    ClasspathExtension extension = new ClasspathExtension(FirefoxProfile.class,
		            "/" + FirefoxProfile.class.getPackage().getName().replace(".", "/") + "/webdriver.xpi");
		    try {
				extension.writeTo(new File(dir, "extensions"));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		FirefoxProfile profile = new FirefoxProfile(dir){
			@Override
			protected void addWebDriverExtensionIfNeeded() {
				// already in model profile, so doesn't need extracting as part of FirefoxProfile.installExtensions()
			}
			
		};
		FirefoxDriver firefoxDriver = new FirefoxDriver(profile);
		return firefoxDriver;
	}

}
