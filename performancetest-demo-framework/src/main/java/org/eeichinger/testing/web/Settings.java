package org.eeichinger.testing.web;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * Settings for webdriver tests
 *
 * @author Erich Eichinger/OpenCredo
 * @author Neale Upstone/OpenCredo
 */
public class Settings {
    
    public static final String APPLICATION_URL;
    public static final boolean RUN_HEADLESS;
	public static final boolean TAKE_ERROR_SCREENSHOTS;
    
    static {
    	// Read properties file.
        Properties properties = new Properties();
        try {
            URL propUrl = Thread.currentThread().getContextClassLoader().getResource("test-environment.properties");
            properties.load(propUrl.openStream());
            properties.putAll(System.getProperties()); // merge -Dname=val in
        } catch (IOException e) {
        	throw new RuntimeException(e); // can't do much else
        }
        
        APPLICATION_URL = trimTrailingSlash(properties.getProperty("target.host.url"));
        RUN_HEADLESS = Boolean.parseBoolean(properties.getProperty("tests.headless", "true"));
        TAKE_ERROR_SCREENSHOTS = Boolean.parseBoolean(properties.getProperty("tests.takeerrorscreenshots", "true"));
    }

    
	private static String trimTrailingSlash(String url) {
		if (url.endsWith("/")){
        	url = url.substring(0, url.length() - 1); 
        }
		return url;
	}
}
