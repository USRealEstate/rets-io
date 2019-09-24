package com.ossez.reoc.rets.client;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.*;

import static com.ossez.reoc.rets.client.CapabilityUrls.LOGIN_URL;

/**
 *
 */
abstract public class KeyValueResponse {
    protected static final String CRLF = "\r\n";
    private static final Log LOG = LogFactory.getLog(KeyValueResponse.class);

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
                // guard against a missing value in a KeyValueResponse
                String value = StringUtils.trimToEmpty(splits[1]);

                if (StringUtils.equalsIgnoreCase(LOGIN_URL, key))
                    retsResponseMap.put(LOGIN_URL, value);
                else
                    retsResponseMap.put(key, value);
            }
//            // LOOP TO LOAD LOGIN URL
//            tokenizeList.parallelStream().forEach(keyValueString -> {
//
//                //parallelStream
//                try {
//                    String line = StringUtils.trimToEmpty(keyValueString);
//                    String splits[] = StringUtils.split(line, "=");
//                    if (!ArrayUtils.isEmpty(splits) && splits.length > 1) {
//                        String key = StringUtils.trimToNull(splits[0]);
//                        // guard against a missing value in a KeyValueResponse
//                        String value = StringUtils.trimToEmpty(splits[1]);
//
//                        if (StringUtils.equalsIgnoreCase(LOGIN_URL, key))
//                            retsResponseMap.put(LOGIN_URL, value);
//                        else
//                            retsResponseMap.put(key, value);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });

            // LOOP TO LOAD OTHERS
            this.handleKeyValue(LOGIN_URL, retsResponseMap.get(LOGIN_URL));

            for (String k : retsResponseMap.keySet()) {
                this.handleKeyValue(k, retsResponseMap.get(k));
            }
        }
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
