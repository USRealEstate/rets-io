package com.ossez.usreio.client;

import com.ossez.usreio.client.interfaces.INetworkEventMonitor;

public class NullINetworkEventMonitor implements INetworkEventMonitor {
	
	public Object eventStart(String message) {
		return null;
	}

	
	public void eventFinish(Object o) {
		//noop
	}
}
