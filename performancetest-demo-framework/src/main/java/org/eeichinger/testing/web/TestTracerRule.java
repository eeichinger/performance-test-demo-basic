package org.eeichinger.testing.web;

import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs test method execution
 *
 * @author Neale Upstone/OpenCredo
 */
public class TestTracerRule extends TestWatchman {
    
    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    @Override
    public void starting(FrameworkMethod method) {
        log.info("Starting: {}", method.getName());
    }

    @Override
    public void failed(Throwable e, FrameworkMethod method) {
        log.error("Failed: {}", method.getName(), e);
    }

    @Override
    public void succeeded(FrameworkMethod method) {
        log.info("Success: {}", method.getName());
    }
}
