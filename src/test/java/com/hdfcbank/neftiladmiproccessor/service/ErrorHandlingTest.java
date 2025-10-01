package com.hdfcbank.neftiladmiproccessor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdfcbank.neftiladmiproccessor.kafkaproducer.KafkaUtils;
import com.hdfcbank.neftiladmiproccessor.model.Body;
import com.hdfcbank.neftiladmiproccessor.model.Header;
import com.hdfcbank.neftiladmiproccessor.model.ReqPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

class ErrorHandlingTest {
    @Mock
    KafkaUtils kafkaUtils;
    @Mock
    ObjectMapper objectMapper;
    @InjectMocks
    ErrorHandling errorHandling;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Set private fields for topic values
        try {
            java.lang.reflect.Field errorField = ErrorHandling.class.getDeclaredField("errortopic");
            errorField.setAccessible(true);
            errorField.set(errorHandling, "error-topic");
            java.lang.reflect.Field dispatcherField = ErrorHandling.class.getDeclaredField("dispatchertopic");
            dispatcherField.setAccessible(true);
            dispatcherField.set(errorHandling, "dispatcher-topic");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @Test
//    void testHandleInvalidPayload_success() throws Exception {
//        ReqPayload req = new ReqPayload();
//        Header header = new Header();
//        header.setMsgId("123");
//        req.setHeader(header);
//        req.setBody(new Body());
//        when(objectMapper.writeValueAsString(any())).thenReturn("json");
//        errorHandling.handleInvalidPayload(req);
//        verify(objectMapper).writeValueAsString(req);
//        verify(kafkaUtils).publishToResponseTopic(eq("json"), eq("dispatcher-topic"),eq("123"));
//    }

    @Test
    void testHandleInvalidPayload_jsonProcessingException() throws Exception {
        ReqPayload req = new ReqPayload();
        Header header = new Header();
        req.setHeader(header);
        req.setBody(new Body());
        when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("fail"){});
        try {
            errorHandling.handleInvalidPayload(req);
            assert false;
        } catch (JsonProcessingException e) {
            assert true;
        }
    }
}
