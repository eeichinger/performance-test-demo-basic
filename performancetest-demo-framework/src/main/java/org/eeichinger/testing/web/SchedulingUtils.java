package org.eeichinger.testing.web;

import java.util.concurrent.*;

/**
 * @author: Erich Eichinger
 * @date: 25/01/12
 */
public class SchedulingUtils {

    public static Runnable newRunnableWithTimeout(final int timeoutMillis, final Runnable runnable) {
        return new Runnable() {
            @Override
            public void run() {
                executeWithTimeout(timeoutMillis, runnable);
            }
        };
    }

    /**
     * Execute a runnable within the specified timeout constraint.
     *
     * @param timeoutMillis the timeout in milliseconds
     * @param runnable the runnable instance. must not be null.
     *
     * @throws TimeoutThresholdException in case the timeout threshold is exceeded
     */
    public static void executeWithTimeout(final int timeoutMillis, final Runnable runnable) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Callable<Object> callable = new Callable<Object>() {
            public Object call() throws Exception {
                runnable.run();
                return null;
            }
        };
        Future<Object> result = service.submit(callable);
        service.shutdown();
        try {
            boolean terminated = service.awaitTermination(timeoutMillis, TimeUnit.MILLISECONDS);
            if (!terminated) {
                service.shutdownNow();
            }
            result.get(0, TimeUnit.MILLISECONDS); // throws the exception if one occurred during the invocation
        } catch (TimeoutException e) {
            throw new TimeoutThresholdException(String.format("execution timed out after %d milliseconds", timeoutMillis));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
