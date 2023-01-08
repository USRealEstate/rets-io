package com.ossez.usreio.tests.common.metadata.attrib;

import com.ossez.usreio.tests.common.metadata.AttrType;

public class AttrGenericTextTest extends AttrTypeTest {
	public void testAttrGeneric() throws Exception {
		AttrType parser = new AttrGenericText(0, 10, "abcdefg");

		assertEquals("aaaaa", parser.parse("aaaaa",true));
		assertEquals("abcdefg", parser.parse("abcdefg",true));
		assertEquals("", parser.parse("",true));

		assertParseException(parser, "abcdefG");
		assertParseException(parser, "A");
		assertParseException(parser, "abcdefgabcd");
	}
}
