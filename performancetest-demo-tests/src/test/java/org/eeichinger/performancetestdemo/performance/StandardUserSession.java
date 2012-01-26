package org.eeichinger.performancetestdemo.performance;

import org.eeichinger.performancetestdemo.UserUIDriver;

/**
 * A "standard" user logs in, performs 5 searches. Only 30% perform a proper log out
 *
 * @author Erich Eichinger/OpenCredo
 * @since 25/01/12
 */
public class StandardUserSession implements Runnable {

    @Override
    public void run() {
        final UserUIDriver userUI = new UserUIDriver();

        userUI.login("admin", "123456");

        long numberOfSearches = Math.round(Math.random()*3.0) + 3;

        for(long i=0; i<numberOfSearches; i++) {
            userUI.navigateToWelcome();
            // THEN an account list table will appear with at least 10 results
            userUI.assertContainsText("Welcome");
        }

        if (Math.random() <= 0.3) {
            userUI.logout();
        }
    }
}
