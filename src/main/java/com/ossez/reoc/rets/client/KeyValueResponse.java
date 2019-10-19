package com.ossez.reoc.rets.client;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.ossez.reoc.rets.client.CapabilityUrls.LOGIN_URL;

/**
 * Process key and Value
 *
 * @author YuCheng Hu
 */
abstract public class KeyValueResponse {
    protected static final String CRLF = "\r\n";
    private static final Logger logger = LoggerFactory.getLogger(KeyValueResponse.class);

    protected Document mDoc;
    protected int mReplyCode;
    protected boolean mStrict;

    public KeyValueResponse() {
        this.mStrict = false;
    }

    public void parse(InputStream stream, RetsVersion mVersion) throws RetsException {
        try {
            SAXReader builder = new SAXReader();
            this.mDoc = builder.read(stream);
            Element retsElement = this.mDoc.getRootElement();
            if (!retsElement.getName().equals("RETS")) {
                throw new RetsException("Expecting RETS");
            }

            int replyCode = NumberUtils.toInt(retsElement.attributeValue("ReplyCode"));
            this.mReplyCode = replyCode;
            if (!isValidReplyCode(replyCode)) {
                throw new InvalidReplyCodeException(replyCode);
            }
            Element capabilityContainer;
            if (RetsVersion.RETS_10.equals(mVersion)) {
                capabilityContainer = retsElement;
            } else {
                List children = retsElement.elements();
                if (children.size() != 1) {
                    throw new RetsException("Invalid number of children: " + children.size());
                }

                capabilityContainer = (Element) children.get(0);

                if (!capabilityContainer.getName().equals("RETS-RESPONSE")) {
                    throw new RetsException("Expecting RETS-RESPONSE");
                }
            }
            this.handleRetsResponse(capabilityContainer);
        } catch (DocumentException e) {
            throw new RetsException(e);
        }
    }

    protected boolean isValidReplyCode(int replyCode) {
        return (ReplyCode.SUCCESS.equals(replyCode));

    }

    /**
     * handleRetsResponse
     *
     * @param retsResponse
     * @throws RetsException
     */
    private void handleRetsResponse(Element retsResponse) throws RetsException {
        List<String> tokenizeList = Arrays.asList(StringUtils.split(retsResponse.getText(), CRLF));
        HashMap<String, String> retsResponseMap = new HashMap<String, String>();

        for (String keyValueStr : tokenizeList) {

            String[] splits = StringUtils.split(keyValueStr, "=");
            if (!ArrayUtils.isEmpty(splits) && splits.length > 1) {
                String key = StringUtils.trimToNull(splits[0]);
                String value = StringUtils.trimToEmpty(splits[1]);

                // PROCESS LOGIN_URL
                if (StringUtils.equalsIgnoreCase(LOGIN_URL, key)) {
                    retsResponseMap.put(LOGIN_URL, value);
                    this.handleKeyValue(LOGIN_URL, value);
                } else
                    retsResponseMap.put(key, value);
            }

        }

        retsResponseMap.entrySet().parallelStream().forEach(entry -> {
            try {
                this.handleKeyValue(entry.getKey(), entry.getValue());
            } catch (RetsException ex) {
                logger.warn("Unable process rests login response value", ex);
            }
        });


    }

    protected abstract void handleKeyValue(String key, String value) throws RetsException;

    public void setStrict(boolean strict) {
        this.mStrict = strict;
    }

    public boolean isStrict() {
        return this.mStrict;
    }

    protected boolean matchKey(String key, String value) {
        if (this.mStrict)
            return key.equals(value);

        return key.equalsIgnoreCase(value);
    }

    protected void assertStrictWarning(Log log, String message) throws RetsException {
        if (this.mStrict)
            throw new RetsException(message);

        log.warn(message);
    }

}
