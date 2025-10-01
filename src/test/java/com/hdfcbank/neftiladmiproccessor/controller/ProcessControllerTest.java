package com.hdfcbank.neftiladmiproccessor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdfcbank.neftiladmiproccessor.model.Body;
import com.hdfcbank.neftiladmiproccessor.model.Header;
import com.hdfcbank.neftiladmiproccessor.model.ReqPayload;
import com.hdfcbank.neftiladmiproccessor.model.Response;
import com.hdfcbank.neftiladmiproccessor.service.AdmiXmlProcessor;
import com.hdfcbank.neftiladmiproccessor.service.ErrorHandling;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import java.util.Base64;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProcessControllerTest {
    @Mock
    AdmiXmlProcessor admiXmlProcessor;
    @Mock
    ErrorHandling errorHandling;
    @InjectMocks
    ProcessController controller;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void testHealthz() {
        ResponseEntity<?> resp = controller.healthz();
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("Success", resp.getBody());
    }

    @Test
    void testReady() {
        ResponseEntity<?> resp = controller.ready();
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("Success", resp.getBody());
    }

//    @Test
//    void testProcess_validPayload() throws Exception {
//        Header header = new Header();
//        header.setInvalidPayload(false);
//        Body body = new Body();
//        ReqPayload reqPayload = new ReqPayload(header, body);
//        String reqPayloadString = objectMapper.writeValueAsString(reqPayload);
//        String base64 = Base64.getEncoder().encodeToString(reqPayloadString.getBytes());
//        String json = "{\"data_base64\":\"" + base64 + "\"}";
//        doNothing().when(admiXmlProcessor).parseXml(any());
//        Mono<ResponseEntity<Response>> result = controller.process(json);
//        ResponseEntity<Response> resp = result.block();
//        assertEquals(HttpStatus.OK, resp.getStatusCode());
//        assertEquals("SUCCESS", resp.getBody().getStatus());
//    }
//
//    @Test
//    void testProcess_invalidPayload() throws Exception {
//        Header header = new Header();
//        header.setInvalidPayload(true);
//        Body body = new Body();
//        ReqPayload reqPayload = new ReqPayload(header, body);
//        String reqPayloadString = objectMapper.writeValueAsString(reqPayload);
//        String base64 = Base64.getEncoder().encodeToString(reqPayloadString.getBytes());
//        String json = "{\"data_base64\":\"" + base64 + "\"}";
//        doNothing().when(errorHandling).handleInvalidPayload(any());
//        Mono<ResponseEntity<Response>> result = controller.process(json);
//        ResponseEntity<Response> resp = result.block();
//        assertEquals(HttpStatus.OK, resp.getStatusCode());
//        assertEquals("SUCCESS", resp.getBody().getStatus());
//    }

    @Test
    void testProcess_exception() throws Exception {
        Header header = new Header();
        header.setInvalidPayload(false);
        Body body = new Body();
        ReqPayload reqPayload = new ReqPayload(header, body);
        String reqPayloadString = objectMapper.writeValueAsString(reqPayload);
        String base64 = Base64.getEncoder().encodeToString(reqPayloadString.getBytes());
        String json = "{\"data_base64\":\"" + base64 + "\"}";
        doThrow(new RuntimeException("fail")).when(admiXmlProcessor).parseXml(any());
        Mono<ResponseEntity<Response>> result = controller.process(json);
        ResponseEntity<Response> resp = result.block();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
        assertEquals("ERROR", resp.getBody().getStatus());
    }

//    @Test
//    void testTestProcess_validPayload() throws Exception {
//        Header header = new Header();
//        header.setInvalidPayload(false);
//        Body body = new Body();
//        ReqPayload reqPayload = new ReqPayload(header, body);
//        String reqPayloadString = objectMapper.writeValueAsString(reqPayload);
//        String base64 = Base64.getEncoder().encodeToString(reqPayloadString.getBytes());
//        String json = "{\"data_base64\":\"" + base64 + "\"}";
//        doNothing().when(admiXmlProcessor).parseXml(any());
//        Mono<ResponseEntity<Response>> result = controller.testProcess(json);
//        ResponseEntity<Response> resp = result.block();
//        assertEquals(HttpStatus.OK, resp.getStatusCode());
//        assertEquals("SUCCESS", resp.getBody().getStatus());
//    }

    @Test
    void testTestProcess_exception() throws Exception {
        Header header = new Header();
        header.setInvalidPayload(false);
        Body body = new Body();
        ReqPayload reqPayload = new ReqPayload(header, body);
        String reqPayloadString = objectMapper.writeValueAsString(reqPayload);
        String base64 = Base64.getEncoder().encodeToString(reqPayloadString.getBytes());
        String json = "{\"data_base64\":\"" + base64 + "\"}";
        doThrow(new RuntimeException("fail")).when(admiXmlProcessor).parseXml(any());
        Mono<ResponseEntity<Response>> result = controller.testProcess(json);
        ResponseEntity<Response> resp = result.block();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
        assertEquals("ERROR", resp.getBody().getStatus());
    }
}

