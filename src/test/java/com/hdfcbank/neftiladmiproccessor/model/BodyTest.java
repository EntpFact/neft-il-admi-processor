package com.hdfcbank.neftiladmiproccessor.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BodyTest {
    @Test
    void testNoArgsConstructorAndSettersGetters() {
        Body body = new Body();
        body.setPayload("data");
        assertEquals("data", body.getPayload());
    }

    @Test
    void testEqualsHashCodeToString() {
        Body b1 = new Body();
        b1.setPayload("data");
        Body b2 = new Body();
        b2.setPayload("data");
        assertEquals(b1, b2);
        assertEquals(b1.hashCode(), b2.hashCode());
        assertTrue(b1.toString().contains("data"));
    }
}

