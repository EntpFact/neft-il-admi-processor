package com.hdfcbank.neftiladmiproccessor.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TransactionAuditTest {
    @Test
    void testNoArgsConstructorAndSettersGetters() {
        TransactionAudit ta = new TransactionAudit();
        ta.setMsgId("msg");
        ta.setTxnId("txn");
        ta.setEndToEndId("e2e");
        ta.setBatchId("batch");
        ta.setReturnId("ret");
        ta.setSource("src");
        ta.setTarget("tgt");
        ta.setFlowType("flow");
        ta.setMsgType("type");
        ta.setAmount(BigDecimal.TEN);
        ta.setStatus("status");
        ta.setVersion(BigDecimal.ONE);
        LocalDate date = LocalDate.now();
        LocalDateTime ldt = LocalDateTime.now();
        ta.setBatchCreationDate(date);
        ta.setBatchTimestamp(ldt);
        ta.setCreatedTime(ldt);
        ta.setModifiedTimestamp(ldt);
        assertEquals("msg", ta.getMsgId());
        assertEquals("txn", ta.getTxnId());
        assertEquals("e2e", ta.getEndToEndId());
        assertEquals("batch", ta.getBatchId());
        assertEquals("ret", ta.getReturnId());
        assertEquals("src", ta.getSource());
        assertEquals("tgt", ta.getTarget());
        assertEquals("flow", ta.getFlowType());
        assertEquals("type", ta.getMsgType());
        assertEquals(BigDecimal.TEN, ta.getAmount());
        assertEquals("status", ta.getStatus());
        assertEquals(BigDecimal.ONE, ta.getVersion());
        assertEquals(date, ta.getBatchCreationDate());
        assertEquals(ldt, ta.getBatchTimestamp());
        assertEquals(ldt, ta.getCreatedTime());
        assertEquals(ldt, ta.getModifiedTimestamp());
    }

    @Test
    void testAllArgsConstructorAndEqualsHashCodeToString() {
        LocalDate date = LocalDate.now();
        LocalDateTime ldt = LocalDateTime.now();
        TransactionAudit ta1 = new TransactionAudit("msg","txn","e2e","batch","ret","src","tgt","flow","type",BigDecimal.TEN,"status",BigDecimal.ONE,date,ldt,ldt,ldt);
        TransactionAudit ta2 = new TransactionAudit("msg","txn","e2e","batch","ret","src","tgt","flow","type",BigDecimal.TEN,"status",BigDecimal.ONE,date,ldt,ldt,ldt);
        assertEquals(ta1, ta2);
        assertEquals(ta1.hashCode(), ta2.hashCode());
        assertTrue(ta1.toString().contains("msg"));
    }
}

