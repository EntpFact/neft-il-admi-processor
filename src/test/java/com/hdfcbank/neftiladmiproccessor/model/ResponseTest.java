package com.hdfcbank.neftiladmiproccessor.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {
    @Test
    void testAllArgsConstructorAndGettersSetters() {
        Response response = new Response("OK", "Success");
        assertEquals("OK", response.getStatus());
        assertEquals("Success", response.getMessage());
        response.setStatus("FAIL");
        response.setMessage("Error");
        assertEquals("FAIL", response.getStatus());
        assertEquals("Error", response.getMessage());
    }

    @Test
    void testEqualsHashCodeToString() {
        Response r1 = new Response("OK", "Success");
        Response r2 = new Response("OK", "Success");
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertTrue(r1.toString().contains("OK"));
    }
}

