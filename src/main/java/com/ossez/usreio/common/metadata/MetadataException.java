/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package com.ossez.usreio.common.metadata;


public class MetadataException extends Exception {
	public MetadataException() {
		super();
	}

	public MetadataException(String msg) {
		super(msg);
	}

	public MetadataException(Throwable cause) {
		super(cause);
	}

	public MetadataException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
