package com.ossez.reoc.rets.client;

public class LogoutRequest extends VersionInsensitiveRequest {

	@Override
	public void setUrl(CapabilityUrls urls) {
		setUrl(urls.getLogoutUrl());
	}
}
