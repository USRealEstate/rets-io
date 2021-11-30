package com.ossez.usreio.client;

public abstract class Main extends RetsHttpRequest {
	/**
	 * Abstract class of subclasses where the Version of RETS is not needed (Password Request, Login Request, etc.)
	 */
	public Main() {
		super();
	}

	@Override
	public void setVersion(RetsVersion version) {
		//noop - I don't care about version
	}
}
