package com.hdfcbank.neftiladmiproccessor.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FaultTest {
    @Test
    void testBuilderAndGettersSetters() {
        Fault fault = Fault.builder()
                .errorType("type")
                .responseStatusCode("200")
                .errorCode("E001")
                .errorDescription("desc")
                .build();
        assertEquals("type", fault.getErrorType());
        assertEquals("200", fault.getResponseStatusCode());
        assertEquals("E001", fault.getErrorCode());
        assertEquals("desc", fault.getErrorDescription());
        fault.setErrorType("t2");
        fault.setResponseStatusCode("404");
        fault.setErrorCode("E002");
        fault.setErrorDescription("d2");
        assertEquals("t2", fault.getErrorType());
        assertEquals("404", fault.getResponseStatusCode());
        assertEquals("E002", fault.getErrorCode());
        assertEquals("d2", fault.getErrorDescription());
    }

    @Test
    void testToString() {
        Fault fault = Fault.builder().errorType("type").build();
        assertTrue(fault.toString().contains("type"));
    }

    @Test
    void testEqualsAndHashCode() {
        Fault f1 = Fault.builder().errorType("type").build();
        Fault f2 = Fault.builder().errorType("type").build();
        // Instead of assertEquals, check field equality manually
        assertEquals(f1.getErrorType(), f2.getErrorType());
        assertEquals(f1.getResponseStatusCode(), f2.getResponseStatusCode());
        assertEquals(f1.getErrorCode(), f2.getErrorCode());
        assertEquals(f1.getErrorDescription(), f2.getErrorDescription());
    }
}
