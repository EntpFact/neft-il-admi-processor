package com.hdfcbank.neftiladmiproccessor.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HeaderTest {
    @Test
    void testNoArgsConstructorAndSettersGetters() {
        Header h = new Header();
        h.setMsgId("msg");
        h.setTarget("target");
        h.setSource("source");
        h.setMsgType("type");
        h.setFlowType("flow");
        h.setReplayInd(true);
        h.setInvalidPayload(true);
        h.setPrefix("pre");
        assertEquals("msg", h.getMsgId());
        assertEquals("target", h.getTarget());
        assertEquals("source", h.getSource());
        assertEquals("type", h.getMsgType());
        assertEquals("flow", h.getFlowType());
        assertTrue(h.isReplayInd());
        assertTrue(h.isInvalidPayload());
        assertEquals("pre", h.getPrefix());
    }

    @Test
    void testToString() {
        Header h = new Header();
        h.setMsgId("msg");
        String str = h.toString();
        assertTrue(str.contains("msg"));
    }

    @Test
    void testEqualsAndHashCode() {
        Header h1 = new Header();
        h1.setMsgId("msg");
        Header h2 = new Header();
        h2.setMsgId("msg");
        assertEquals(h1, h2);
        assertEquals(h1.hashCode(), h2.hashCode());
    }
}

