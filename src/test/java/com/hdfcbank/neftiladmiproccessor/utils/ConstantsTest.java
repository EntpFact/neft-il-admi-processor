package com.hdfcbank.neftiladmiproccessor.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConstantsTest {

    @Test
    void testKafkaResponseTopicDaprBinding() {
        assertEquals("kafka-admi-processor-pubsub-component", Constants.KAFKA_RESPONSE_TOPIC_DAPR_BINDING);
    }

    @Test
    void testDispatchedFc() {
        assertEquals("FC_DISPATCHER", Constants.FC_DISPATCHER);
    }

    @Test
    void testDispatchedEph() {
        assertEquals("EPH_DISPATCHER", Constants.EPH_DISPATCHER);
    }

    @Test
    void testInProgress() {
        assertEquals("SENT_TO_DISPATCHER", Constants.SENT_TO_DISPATCHER);
    }
}

