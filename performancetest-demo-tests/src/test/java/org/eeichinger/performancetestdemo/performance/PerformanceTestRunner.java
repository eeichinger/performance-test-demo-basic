package org.eeichinger.performancetestdemo.performance;

import org.eeichinger.testing.web.AbstractPerformanceTestRunner;
import org.eeichinger.testing.web.BrowserSessionFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Erich Eichinger/OpenCredo
 * @date: 26/01/12
 */
public class PerformanceTestRunner extends AbstractPerformanceTestRunner {

    public static void main(String[] args) throws Exception {
        new PerformanceTestRunner().run(args);
    }

    @Override
    protected List<BrowserSessionFactory> createBrowserProfiles() {
        List<BrowserSessionFactory> browserProfiles = new ArrayList<BrowserSessionFactory>();
        browserProfiles.add(new BrowserSessionFactory<StandardUserSession>(StandardUserSession.class, 100, 20000));
        return browserProfiles;
    }
}
