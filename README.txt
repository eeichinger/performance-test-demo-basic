
Performance Test Example
========================


Modules:
--------
"performancetest-demo-web"       : a simple web application under test
"performancetest-demo-framework" : a set of reusable classes for web ui testing
"performancetest-demo-tests"     : contains the actual tests for the sample web application


Running the tests:
------------------
to perform a full test run, use

    mvn clean verify -Ptomcat,deployment-tests,performance-tests -Dperformance.test.users=5 -Dperformance.test.sessions=10


Profiles:
---------
"tomcat"
        causes tomcat to launch on localhost:8080 during the "integration-test" phase
"deployment-tests"
        a set of smoke tests verifying that the application launched correctly
"performance-tests"
        invokes the PerformanceTestRunner class to run performance tests based on properties (see below)


Common Properties:
------------------
"target.url.host"
        the URL to run Web UI tests against. Defaults to http://localhost:8080/SpringMVC which the integrated tomcat listens to
"tests.headless"
        if true uses HtmlUnitDriver, otherwise FirefoxDriver
"tests.takeerrorscreenshots"
        takes browser screenshots in case of failures


Performance Test Properties:
----------------------------
"performance.test.users"
	    the number of simultanous users (=threads) targetting the webserver
"performance.test.sessions"
        the number of sessions (scenarios) each user executes
"performance.test.usersIncr"
        after each iteration increase the number of simultanous users by X
"performance.test.usersMax"
        the maximum number of simultanous users


Performance Test Examples
-------------------------
To run load tests against a remote server ensuring the webapplication can handle steady load with a 20 users executing 50 scenarios each:

	mvn clean install -Pperformance-tests -Dperformance.test.users=20 -Dperformance.test.sessions=50 -Dtarget.url.host=http://remote.org/myapp

To run capacity tests against a remote server to see where the system breaks down increment the users after each iteration:

	mvn clean install -Pperformance-tests -Dperformance.test.users=20 -Dperformance.test.sessions=50 -Dperformance.test.usersIncr=10 -Dperformance.test.usersMax=200 -Dtarget.url.host=http://remote.org/myapp
