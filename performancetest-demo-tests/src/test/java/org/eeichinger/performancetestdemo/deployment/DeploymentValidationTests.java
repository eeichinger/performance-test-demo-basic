package org.eeichinger.performancetestdemo.deployment;

import org.eeichinger.performancetestdemo.UserUIDriver;
import org.eeichinger.testing.web.BaseWebDriverTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Some basic smoke tests that are executed against a deployed app instance
 *
 * @author Erich Eichinger/OpenCredo
 * @since 25/01/12
 */
public class DeploymentValidationTests extends BaseWebDriverTest {

    private UserUIDriver userUI;

    @Before
    public void beforeTest() {
        userUI = new UserUIDriver();
    }

    @Test
    public void home_is_public() {
        userUI.navigateToHome();
        userUI.assertContainsText("Home");
    }

	@Test
	public void can_login() throws Exception {
		userUI.login("admin", "123456");
        userUI.navigateToWelcome();
		userUI.assertContainsText("Hello World");
	}

	@Test
	public void welcome_is_restricted() throws Exception {
        userUI.navigateToWelcome();
        userUI.assertContainsText("Login");
	}
}
