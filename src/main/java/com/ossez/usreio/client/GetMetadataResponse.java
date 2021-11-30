package com.ossez.usreio.client;

import java.io.InputStream;
import java.util.List;

import com.ossez.usreio.tests.common.metadata.JDomCompactBuilder;
import com.ossez.usreio.tests.common.metadata.JDomStandardBuilder;
import com.ossez.usreio.tests.common.metadata.MetaObject;
import com.ossez.usreio.tests.common.metadata.MetadataException;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class GetMetadataResponse {
	private MetaObject[] mMetadataObjs;

	public GetMetadataResponse(InputStream stream, boolean compact, boolean isStrict) throws RetsException {
		try {
			SAXReader builder = new SAXReader();
			Document document = builder.read(stream);
			Element retsElement = document.getRootElement();
			if (!retsElement.getName().equals("RETS")) {
				throw new RetsException("Expecting RETS");
			}
			int replyCode = NumberUtils.toInt(retsElement.attributeValue("ReplyCode"));
			if (ReplyCode.SUCCESS.equals(replyCode)) {
				if (compact) {
					handleCompactMetadata(document, isStrict);
				} else {
					handleStandardMetadata(document, isStrict);
				}
			} else if (ReplyCode.NO_METADATA_FOUND.equals(replyCode)) {
				// No metadata is not an exceptional case
				handleNoMetadataFound(retsElement);
			} else {
				InvalidReplyCodeException e = new InvalidReplyCodeException(replyCode);
				e.setRemoteMessage(retsElement.attributeValue(retsElement.attributeValue("ReplyText")));
				throw e;
			}
		} catch (DocumentException e) {
			throw new RetsException(e);
		}
	}

	private void handleNoMetadataFound(Element retsElement) throws RetsException {
		List children = retsElement.elements();
		if (children.size() != 0) {
			throw new RetsException("Expecting 0 children when results");
		}
		this.mMetadataObjs = new MetaObject[0];
	}

	private void handleCompactMetadata(Document document, boolean isStrict) throws RetsException {
		try {
			JDomCompactBuilder builder = new JDomCompactBuilder();
			builder.setStrict(isStrict);
			this.mMetadataObjs = builder.parse(document);
		} catch (MetadataException e) {
			throw new RetsException(e);
		}
	}

	private void handleStandardMetadata(Document document, boolean isStrict) throws RetsException {
		try {
			JDomStandardBuilder builder = new JDomStandardBuilder();
			builder.setStrict(isStrict);
			this.mMetadataObjs = builder.parse(document);
		} catch (MetadataException e) {
			throw new RetsException(e);
		}
	}

	public MetaObject[] getMetadata() {
		return this.mMetadataObjs;
	}

}
