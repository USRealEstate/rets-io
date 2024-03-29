/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package com.ossez.usreio.tests.common.metadata.attrib;

import com.ossez.usreio.tests.common.metadata.AttrType;
import com.ossez.usreio.tests.common.metadata.MetaParseException;

public class AttrNumeric implements AttrType<Integer> {
	public Integer parse(String value, boolean strict) throws MetaParseException {
		try {
			return new Integer(value);
		} catch (NumberFormatException e) {
			if( strict ) 
				throw new MetaParseException(e);
			return 0;
		}
	}

	public String render(Integer value) {
		return value.toString();
	}

	public Class<Integer> getType() {
		return Integer.class;
	}
}
