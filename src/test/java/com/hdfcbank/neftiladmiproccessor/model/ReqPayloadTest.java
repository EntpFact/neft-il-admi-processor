package com.hdfcbank.neftiladmiproccessor.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReqPayloadTest {
    @Test
    void testNoArgsConstructorAndSettersGetters() {
        ReqPayload req = new ReqPayload();
        Header header = new Header();
        Body body = new Body();
        req.setHeader(header);
        req.setBody(body);
        assertEquals(header, req.getHeader());
        assertEquals(body, req.getBody());
    }

    @Test
    void testAllArgsConstructor() {
        Header header = new Header();
        Body body = new Body();
        ReqPayload req = new ReqPayload(header, body);
        assertEquals(header, req.getHeader());
        assertEquals(body, req.getBody());
    }

    @Test
    void testCopyConstructor() {
        Header header = new Header();
        Body body = new Body();
        ReqPayload original = new ReqPayload(header, body);
        ReqPayload copy = new ReqPayload(original);
        assertEquals(header, copy.getHeader());
        assertEquals(body, copy.getBody());
    }

    @Test
    void testToString() {
        ReqPayload req = new ReqPayload();
        String str = req.toString();
        assertTrue(str.contains("header"));
    }

    @Test
    void testEqualsAndHashCode() {
        Header header = new Header();
        Body body = new Body();
        ReqPayload req1 = new ReqPayload(header, body);
        ReqPayload req2 = new ReqPayload(header, body);
        assertEquals(req1, req2);
        assertEquals(req1.hashCode(), req2.hashCode());
    }
}

