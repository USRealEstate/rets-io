package com.ossez.usreio.tests.client;

import com.ossez.usreio.client.InvalidReplyCodeException;
import com.ossez.usreio.client.InvalidReplyCodeHandler;

final class TestInvalidReplyCodeHandler implements InvalidReplyCodeHandler {
    private int replyCode;
    
    public void invalidRetsReplyCode(int code) throws InvalidReplyCodeException {
        throw new InvalidReplyCodeException(code);
    }
    
    public void invalidRetsStatusReplyCode(int code) throws InvalidReplyCodeException {
        this.replyCode = code;
    }

    public int getReplyCode() {
        return this.replyCode;
    }
}
