package com.ossez.usreio.client;

public class LogoutRequest extends VersionInsensitiveRequest {

	@Override
	public void setUrl(CapabilityUrls urls) {
		setUrl(urls.getLogoutUrl());
	}
}
