package com.hdfcbank.neftiladmiproccessor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdfcbank.neftiladmiproccessor.kafkaproducer.KafkaUtils;
import com.hdfcbank.neftiladmiproccessor.model.Admi004Tracker;
import com.hdfcbank.neftiladmiproccessor.model.ReqPayload;
import com.hdfcbank.neftiladmiproccessor.repository.NilRepository;
import com.hdfcbank.neftiladmiproccessor.utils.NILRouterCommonUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.OffsetDateTime;
import org.mockito.MockedStatic;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdmiXmlProcessorTest {
    @Mock
    NILRouterCommonUtility nilRouterCommonUtility;
    @Mock
    NilRepository nilRepository;
    @Mock
    KafkaUtils kafkaUtils;
    @Mock
    ObjectMapper objectMapper;
    @InjectMocks
    AdmiXmlProcessor admiXmlProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(admiXmlProcessor, "dispatchertopic", "test-topic");
    }

    @Test
    void testParseXml_evtCdF95() throws Exception {
        String xml = "<Document><EvtCd>F95</EvtCd><BizMsgIdr>1234567890123456</BizMsgIdr><CreDtTm>2024-08-29T10:15:30+05:30</CreDtTm><MsgDefIdr>admi004</MsgDefIdr></Document>";
        ReqPayload reqPayload = TestUtils.createReqPayload(xml);
        try (MockedStatic<NILRouterCommonUtility> utilities = mockStatic(NILRouterCommonUtility.class)) {
            mockStaticUtils(utilities, "F95", "1234567890123456", "2024-08-29T10:15:30+05:30", "admi004");
            admiXmlProcessor.parseXml(reqPayload);
        }
    }

    @Test
    void testParseXml_evtCdOther_FC() throws Exception {
        String xml = "<Document><EvtCd>F99</EvtCd><BizMsgIdr>1234567890123400</BizMsgIdr><CreDtTm>2024-08-29T10:15:30+05:30</CreDtTm><MsgDefIdr>admi004</MsgDefIdr></Document>";
        ReqPayload reqPayload = TestUtils.createReqPayload(xml);
        try (MockedStatic<NILRouterCommonUtility> utilities = mockStatic(NILRouterCommonUtility.class)) {
            mockStaticUtils(utilities, "F99", "1234567890123400", "2024-08-29T10:15:30+05:30", "admi004");
            admiXmlProcessor.parseXml(reqPayload);
        }
    }

    @Test
    void testParseXml_evtCdOther_EPH() throws Exception {
        String xml = "<Document><EvtCd>F99</EvtCd><BizMsgIdr>1234567890123499</BizMsgIdr><CreDtTm>2024-08-29T10:15:30+05:30</CreDtTm><MsgDefIdr>admi004</MsgDefIdr></Document>";
        ReqPayload reqPayload = TestUtils.createReqPayload(xml);
        try (MockedStatic<NILRouterCommonUtility> utilities = mockStatic(NILRouterCommonUtility.class)) {
            mockStaticUtils(utilities, "F99", "1234567890123499", "2024-08-29T10:15:30+05:30", "admi004");
            admiXmlProcessor.parseXml(reqPayload);
        }
    }

    @Test
    void testParseXml_evtCdOther_EPH_branch() throws Exception {
        // BizMsgIdr 13th char is '5' (index 13, 0-based)
        String xml = "<Document><EvtCd>F99</EvtCd><BizMsgIdr>1234567890123555</BizMsgIdr><CreDtTm>2024-08-29T10:15:30+05:30</CreDtTm><MsgDefIdr>admi004</MsgDefIdr></Document>";
        ReqPayload reqPayload = TestUtils.createReqPayload(xml);
        try (MockedStatic<NILRouterCommonUtility> utilities = mockStatic(NILRouterCommonUtility.class)) {
            mockStaticUtils(utilities, "F99", "1234567890123555", "2024-08-29T10:15:30+05:30", "admi004");
            // duplicateExists should return false to allow tracker update
            when(nilRouterCommonUtility.duplicateExists(any())).thenReturn(false);
            when(objectMapper.writeValueAsString(any())).thenReturn("payload");
            admiXmlProcessor.parseXml(reqPayload);
            // Optionally verify EPH branch
            verify(nilRepository).updateAdmiTracker(any(Admi004Tracker.class));
            verify(kafkaUtils).publishToResponseTopic(anyString(), anyString());
        }
    }

    @Test
    void testParseXml_invalidXml_throwsException() {
        String xml = "<Document><EvtCd>F95</EvtCd></Document"; // malformed
        ReqPayload reqPayload = TestUtils.createReqPayload(xml);
        try (MockedStatic<NILRouterCommonUtility> utilities = mockStatic(NILRouterCommonUtility.class)) {
            // Let parseXmlStringToDocument throw exception
            utilities.when(() -> NILRouterCommonUtility.parseXmlStringToDocument(any())).thenThrow(new RuntimeException("Malformed XML"));
            try {
                admiXmlProcessor.parseXml(reqPayload);
                assert false;
            } catch (Exception e) {
                assert true;
            }
        }
    }

    private void mockStaticUtils(MockedStatic<NILRouterCommonUtility> utilities, String evtCd, String msgId, String creDtTm, String msgDefIdr) throws Exception {
        utilities.when(() -> NILRouterCommonUtility.parseXmlStringToDocument(any())).thenCallRealMethod();
        utilities.when(() -> NILRouterCommonUtility.getBizMsgIdr(any())).thenReturn(msgId);
        utilities.when(() -> NILRouterCommonUtility.getBatchCreationDate(any())).thenReturn(creDtTm);
        utilities.when(() -> NILRouterCommonUtility.evaluateText(any(), any())).thenReturn(evtCd);
        utilities.when(() -> NILRouterCommonUtility.getMsgDefIdr(any())).thenReturn(msgDefIdr);
    }

    // TestUtils is a helper class to create ReqPayload with XML body
    static class TestUtils {
        static ReqPayload createReqPayload(String xml) {
            ReqPayload reqPayload = new ReqPayload();
            com.hdfcbank.neftiladmiproccessor.model.Body body = new com.hdfcbank.neftiladmiproccessor.model.Body();
            body.setPayload(xml);
            reqPayload.setBody(body);
            com.hdfcbank.neftiladmiproccessor.model.Header header = new com.hdfcbank.neftiladmiproccessor.model.Header();
            reqPayload.setHeader(header);
            return reqPayload;
        }
    }
}
