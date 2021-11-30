package com.ossez.usreio.client;

import com.ossez.usreio.common.rets.RetsVersion;

public abstract class VersionInsensitiveRequest extends RetsHttpRequest {
	/**
	 * Abstract class of subclasses where the Version of RETS is not needed (Password Request, Login Request, etc.)
	 */
	public VersionInsensitiveRequest() {
		super();
	}

	@Override
	public void setVersion(RetsVersion version) {
		//noop - I don't care about version
	}
}
