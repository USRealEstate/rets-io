package com.ossez.reoc.rets.client;

/**
 * @author YuCheng Hu
 */
public class RetsException extends Exception {
	public RetsException() {
		super();
	}

	public RetsException(String message) {
		super(message);
	}

	public RetsException(String message, Throwable cause) {
		super(message, cause);
	}

	public RetsException(Throwable cause) {
		super(cause);
	}
}
