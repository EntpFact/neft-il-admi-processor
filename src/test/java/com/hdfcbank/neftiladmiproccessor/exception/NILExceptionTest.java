package com.hdfcbank.neftiladmiproccessor.exception;

import com.hdfcbank.neftiladmiproccessor.model.Fault;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class NILExceptionTest {
    @Test
    void testDefaultConstructor() {
        NILException ex = new NILException();
        assertNull(ex.getMessage());
        assertNull(ex.getErrors());
    }

    @Test
    void testConstructorWithMessage() {
        NILException ex = new NILException("error");
        assertEquals("error", ex.getMessage());
    }

    @Test
    void testConstructorWithMessageAndThrowable() {
        Throwable t = new RuntimeException("cause");
        NILException ex = new NILException("error", t);
        assertEquals("error", ex.getMessage());
        assertEquals(t, ex.getCause());
    }

    @Test
    void testConstructorWithMessageAndErrors() {
        Fault fault = Fault.builder().build();
        NILException ex = new NILException("error", List.of(fault));
        assertEquals("error", ex.getMessage());
        assertEquals(1, ex.getErrors().size());
    }

    @Test
    void testSetAndGetErrors() {
        Fault fault = Fault.builder().build();
        NILException ex = new NILException();
        ex.setErrors(List.of(fault));
        assertEquals(1, ex.getErrors().size());
    }
}

