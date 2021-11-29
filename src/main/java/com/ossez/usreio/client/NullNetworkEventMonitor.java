package com.ossez.usreio.client;

public class NullNetworkEventMonitor implements NetworkEventMonitor {
	
	public Object eventStart(String message) {
		return null;
	}

	
	public void eventFinish(Object o) {
		//noop
	}
}
