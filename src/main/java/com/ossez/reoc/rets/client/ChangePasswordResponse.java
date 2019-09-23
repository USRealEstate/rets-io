package com.ossez.reoc.rets.client;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;


/**
 * dbt is lame and hasn't overridden the default
 * javadoc string.
 */
public class ChangePasswordResponse {
	public ChangePasswordResponse(InputStream stream) throws RetsException {
		SAXReader builder = new SAXReader();
		Document document = null;
		try {
			document = builder.read(stream);
		} catch (Exception e) {
			throw new RetsException(e);
		}
		Element rets = document.getRootElement();
		if (!rets.getName().equals("RETS")) {
			throw new RetsException("Invalid Change Password Response");
		}

		int replyCode = Integer.parseInt(rets.attributeValue("ReplyCode"));
		if (replyCode != 0) {
			InvalidReplyCodeException exception;
			exception = new InvalidReplyCodeException(replyCode);
			exception.setRemoteMessage(rets.attributeValue("ReplyText"));
			throw exception;
		}
	}
}
