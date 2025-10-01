package com.hdfcbank.neftiladmiproccessor.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdfcbank.neftiladmiproccessor.exception.NILException;
import com.hdfcbank.neftiladmiproccessor.model.ReqPayload;
import com.hdfcbank.neftiladmiproccessor.model.Response;
import com.hdfcbank.neftiladmiproccessor.service.AdmiXmlProcessor;
import com.hdfcbank.neftiladmiproccessor.service.ErrorHandling;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@RestController
public class ProcessController {

    @Autowired
    AdmiXmlProcessor admiXmlProcessor;

    @Autowired
    ErrorHandling errorHandling;

    @Autowired
    ObjectMapper objectMapper;


    @CrossOrigin
    @GetMapping(path = "/healthz")
    public ResponseEntity<?> healthz() {
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(path = "/ready")
    public ResponseEntity<?> ready() {
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }


    @CrossOrigin
    @PostMapping("/process")
    public Mono<ResponseEntity<Response>> process(@RequestBody String request) throws JsonProcessingException {
        log.info("....Processing Started.... ");
        return Mono.fromCallable(() -> {
            try {
                // Get base64 encoded data
                ReqPayload reqpayload = validateXml(request);

                if (reqpayload != null) {
                    admiXmlProcessor.parseXml(reqpayload);
                }

                return ResponseEntity.ok(new Response("SUCCESS", "Message Processed."));
            } catch (Exception ex) {
                log.error("Failed in consuming the message: {}", ex);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("ERROR", "Message Processing Failed"));
            } finally {
                log.info("....Processing Completed.... ");
            }
        }).onErrorResume(ex -> {
            return Mono.just(new ResponseEntity<>(new Response("ERROR", "Message Processing Failed"), HttpStatus.INTERNAL_SERVER_ERROR));
        });
    }


    private ReqPayload validateXml(String request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(request);

        String base64Data = rootNode.get("data_base64").asText();
        String reqPayloadString = new String(Base64.getDecoder().decode(base64Data), StandardCharsets.UTF_8);
        reqPayloadString = objectMapper.readValue(reqPayloadString, String.class);
        ReqPayload reqpayload = objectMapper.readValue(reqPayloadString, ReqPayload.class);
        if (reqpayload.getHeader().isInvalidPayload()) {
            errorHandling.handleInvalidPayload(reqpayload);

            return null;
        }


        log.info("Decoded XML: {}", (Object) reqpayload);

        return reqpayload;
    }


    @CrossOrigin
    @PostMapping("/testProcess")
    public Mono<ResponseEntity<Response>> testProcess(@RequestBody String request) throws JsonProcessingException {

        log.info("....Processing Started.... ");

        return Mono.fromCallable(() -> {
            try {

                ReqPayload reqPayload = objectMapper.readValue(request, ReqPayload.class);


                admiXmlProcessor.parseXml(reqPayload);


                return ResponseEntity.ok(new Response("SUCCESS", "Message Processed."));
            } catch (Exception ex) {
                log.error("Failed in consuming the message: {}", ex);

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("ERROR", "Message Processing Failed"));
            } finally {
                log.info("....Processing Completed.... ");
            }
        }).onErrorResume(ex -> {
            return Mono.just(new ResponseEntity<>(new Response("ERROR", "Message Processing Failed"), HttpStatus.INTERNAL_SERVER_ERROR));
        });
    }
}