package org.eeichinger.testing.web;

/**
 * @author Erich Eichinger/OpenCredo
 * @since 25/01/12
 */
public class TimeoutThresholdException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TimeoutThresholdException(String msg) {
        super(msg);
    }
}
