package com.hdfcbank.neftiladmiproccessor.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConstantsTest {

    @Test
    void testKafkaResponseTopicDaprBinding() {
        assertEquals("kafka-nilrouter-pubsub-component", Constants.KAFKA_RESPONSE_TOPIC_DAPR_BINDING);
    }

    @Test
    void testDispatchedFc() {
        assertEquals("DISPATCHED_FC", Constants.DISPATCHED_FC);
    }

    @Test
    void testDispatchedEph() {
        assertEquals("DISPATCHED_EPH", Constants.DISPATCHED_EPH);
    }

    @Test
    void testInProgress() {
        assertEquals("INPROGRESS", Constants.INPROGRESS);
    }
}

