package org.eeichinger.performancetestdemo;

import org.eeichinger.testing.web.WebDriverFactory;
import org.eeichinger.testing.web.WebDriverUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Exposes the application-specific API that test cases interact with.
 * It encapsulates low-level webdriver interaction.
 *
 * @author Erich Eichinger
 * @since 26/01/12
 */
public class UserUIDriver {

    private static final String URL_HOME = "/";
    private static final String URL_LOGIN = "/spring_security_login";
    private static final String URL_WELCOME = "/welcome";
    private static final String BUTTON_LOGIN = "//input[@type='submit']";
    private static final String FIELD_USERNAME = "j_username";
    private static final String FIELD_PASSWORD = "j_password";

    private final int maxPageTimeoutSeconds = 10;

    protected String getBaseUrl() {
        return WebDriverFactory.getInstance().getBaseUrl();
    }

    protected WebDriver getWebDriver() {
        return WebDriverFactory.getInstance().getCurrentWebDriver();
    }

    public void navigateToWelcome() {
        getLocal(URL_WELCOME);
        waitForPageLoaded();
    }

    public void navigateToHome() {
        getLocal(URL_HOME);
        waitForPageLoaded();
    }

    public void navigateToLogin() {
        getLocal(URL_LOGIN);
        waitForPageLoaded();
    }

    public void logout() {
        getWebDriver().manage().deleteAllCookies();
    }

    public void login(String username, String password) {
        navigateToLogin();
        setField(FIELD_USERNAME, username);
        setField(FIELD_PASSWORD, password);
        clickButtonXpath(BUTTON_LOGIN);
        waitForPageLoaded();
    }

    public void assertContainsText(String text) {
        String pageSource = getWebDriver().getPageSource();
        Assert.assertTrue(String.format("Missing expected text '%s'", text), pageSource.contains(text));
    }

    protected void getLocal(String relativeUrl) {
        getWebDriver().get(getBaseUrl() + relativeUrl);
    }

    protected void waitForPageLoaded() {
        WebDriverUtils.waitForPageLoaded(getWebDriver(), maxPageTimeoutSeconds);
    }

    protected void setField(String fieldName, String text) {
        getWebDriver().findElement(By.name(fieldName)).sendKeys(text);
    }

    /**
     * Finds an element (such as a button or submit) using xpath, then clicks it
     */
    protected void clickButtonXpath(String buttonXpath) {
        getWebDriver().findElement(By.xpath(buttonXpath)).click();
    }
}
