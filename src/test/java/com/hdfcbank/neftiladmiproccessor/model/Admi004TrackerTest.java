package com.hdfcbank.neftiladmiproccessor.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class Admi004TrackerTest {
    @Test
    void testBuilderAndGettersSetters() {
        LocalDate date = LocalDate.now();
        LocalDateTime ldt = LocalDateTime.now();
        Admi004Tracker tracker = Admi004Tracker.builder()
                .msgId("msg")
                .msgType("type")
                .originalReq("req")
                .target("target")
                .version(BigDecimal.ONE)
                .replayCount(2)
                .batchCreationDate(date)
                .batchTimestamp(ldt)
                .createdTime(ldt)
                .modifiedTimestamp(ldt)
                .status("status")
                .build();
        assertEquals("msg", tracker.getMsgId());
        assertEquals("type", tracker.getMsgType());
        assertEquals("req", tracker.getOriginalReq());
        assertEquals("target", tracker.getTarget());
        assertEquals(BigDecimal.ONE, tracker.getVersion());
        assertEquals(2, tracker.getReplayCount());
        assertEquals(date, tracker.getBatchCreationDate());
        assertEquals(ldt, tracker.getBatchTimestamp());
        assertEquals(ldt, tracker.getCreatedTime());
        assertEquals(ldt, tracker.getModifiedTimestamp());
        assertEquals("status", tracker.getStatus());
    }

    @Test
    void testSettersAndEqualsHashCodeToString() {
        Admi004Tracker t1 = Admi004Tracker.builder().build();
        Admi004Tracker t2 = Admi004Tracker.builder().build();
        t1.setMsgId("msg");
        t2.setMsgId("msg");
        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
        assertTrue(t1.toString().contains("msg"));
    }
}

