package com.hdfcbank.neftiladmiproccessor.kafkaproducer;

import com.hdfcbank.messageconnect.config.PubSubOptions;
import com.hdfcbank.messageconnect.dapr.producer.DaprProducer;
import com.hdfcbank.neftiladmiproccessor.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class KafkaUtilsTest {
    @Mock
    DaprProducer daprProducer;

    @InjectMocks
    KafkaUtils kafkaUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPublishToResponseTopicSuccess() {
        when(daprProducer.invokeDaprPublishEvent(any(PubSubOptions.class))).thenReturn(Mono.just("ok"));
        kafkaUtils.publishToResponseTopic("msg", "topic","123");
        verify(daprProducer, times(1)).invokeDaprPublishEvent(any(PubSubOptions.class));
    }

    @Test
    void testPublishToResponseTopicError() {
        when(daprProducer.invokeDaprPublishEvent(any(PubSubOptions.class))).thenReturn(Mono.error(new RuntimeException("fail")));
        kafkaUtils.publishToResponseTopic("msg", "topic","123");
        verify(daprProducer, times(1)).invokeDaprPublishEvent(any(PubSubOptions.class));
    }
}

