package com.hdfcbank.neftiladmiproccessor.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class MsgEventTrackerTest {
    @Test
    void testBuilderAndGettersSetters() {
        LocalDate date = LocalDate.now();
        LocalDateTime ldt = LocalDateTime.now();
        MsgEventTracker tracker = MsgEventTracker.builder()
                .msgId("msg")
                .source("src")
                .target("tgt")
                .batchId("batch")
                .flowType("flow")
                .msgType("type")
                .originalReq("req")
                .invalidMsg(true)
                .replayCount(1)
                .originalReqCount(BigDecimal.ONE)
                .consolidateAmt(BigDecimal.TEN)
                .intermediateReq("intReq")
                .intemdiateCount(BigDecimal.ZERO)
                .status("status")
                .batchCreationDate(date)
                .batchTimestamp(ldt)
                .createdTime(ldt)
                .modifiedTimestamp(ldt)
                .version(BigDecimal.TEN)
                .build();
        assertEquals("msg", tracker.getMsgId());
        assertEquals("src", tracker.getSource());
        assertEquals("tgt", tracker.getTarget());
        assertEquals("batch", tracker.getBatchId());
        assertEquals("flow", tracker.getFlowType());
        assertEquals("type", tracker.getMsgType());
        assertEquals("req", tracker.getOriginalReq());
        assertTrue(tracker.getInvalidMsg());
        assertEquals(1, tracker.getReplayCount());
        assertEquals(BigDecimal.ONE, tracker.getOriginalReqCount());
        assertEquals(BigDecimal.TEN, tracker.getConsolidateAmt());
        assertEquals("intReq", tracker.getIntermediateReq());
        assertEquals(BigDecimal.ZERO, tracker.getIntemdiateCount());
        assertEquals("status", tracker.getStatus());
        assertEquals(date, tracker.getBatchCreationDate());
        assertEquals(ldt, tracker.getBatchTimestamp());
        assertEquals(ldt, tracker.getCreatedTime());
        assertEquals(ldt, tracker.getModifiedTimestamp());
        assertEquals(BigDecimal.TEN, tracker.getVersion());
    }

    @Test
    void testSettersAndEqualsHashCodeToString() {
        MsgEventTracker t1 = new MsgEventTracker();
        MsgEventTracker t2 = new MsgEventTracker();
        t1.setMsgId("msg");
        t2.setMsgId("msg");
        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
        assertTrue(t1.toString().contains("msg"));
    }
}

