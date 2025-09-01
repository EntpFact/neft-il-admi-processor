package com.hdfcbank.neftiladmiproccessor.utils;

import com.hdfcbank.neftiladmiproccessor.model.MsgEventTracker;
import com.hdfcbank.neftiladmiproccessor.repository.NilRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Document;
import javax.xml.xpath.XPathExpressionException;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NILRouterCommonUtilityTest {
    @Mock
    NilRepository nilRepository;

    @InjectMocks
    NILRouterCommonUtility utility;

    String xml = """
        <AppHdr><BizMsgIdr>id123</BizMsgIdr><MsgDefIdr>def456</MsgDefIdr><CreDt>2025-08-29</CreDt></AppHdr>
        <GrpHdr><TtlIntrBkSttlmAmt>123.45</TtlIntrBkSttlmAmt></GrpHdr>
    """;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void testGetBizMsgIdr() throws Exception {
        Document doc = NILRouterCommonUtility.parseXmlStringToDocument("<AppHdr><BizMsgIdr>id</BizMsgIdr></AppHdr>");
        assertEquals("id", NILRouterCommonUtility.getBizMsgIdr(doc));
    }

    @Test
    void testGetMsgDefIdr() throws Exception {
        Document doc = NILRouterCommonUtility.parseXmlStringToDocument("<AppHdr><MsgDefIdr>def</MsgDefIdr></AppHdr>");
        assertEquals("def", NILRouterCommonUtility.getMsgDefIdr(doc));
    }

    @Test
    void testGetBatchCreationDate() throws Exception {
        Document doc = NILRouterCommonUtility.parseXmlStringToDocument("<AppHdr><CreDt>date</CreDt></AppHdr>");
        assertEquals("date", NILRouterCommonUtility.getBatchCreationDate(doc));
    }

    @Test
    void testGetTotalAmount() throws Exception {
        Document doc = NILRouterCommonUtility.parseXmlStringToDocument("<GrpHdr><TtlIntrBkSttlmAmt>100.50</TtlIntrBkSttlmAmt></GrpHdr>");
        assertEquals(new BigDecimal("100.50"), NILRouterCommonUtility.getTotalAmount(doc));
    }

    @Test
    void testParseXmlStringToDocumentAndDocumentToXmlString() throws Exception {
        Document doc = NILRouterCommonUtility.parseXmlStringToDocument("<root><a>1</a></root>");
        String xml = NILRouterCommonUtility.documentToXmlString(doc);
        assertTrue(xml.contains("<a>1</a>"));
    }

    @Test
    void testDuplicateExistsTrue() {
        MsgEventTracker tracker = new MsgEventTracker();
        when(nilRepository.findByMsgId("id")).thenReturn(tracker);
        boolean result = utility.duplicateExists("id");
        assertTrue(result);
        verify(nilRepository).saveDuplicateEntry(tracker);
    }

    @Test
    void testDuplicateExistsFalse() {
        when(nilRepository.findByMsgId("id")).thenReturn(null);
        boolean result = utility.duplicateExists("id");
        assertFalse(result);
    }

    @Test
    void testEvaluateTextNormal() throws Exception {
        Document doc = NILRouterCommonUtility.parseXmlStringToDocument("<root><a>val</a></root>");
        String result = NILRouterCommonUtility.evaluateText(doc, "/root/a");
        assertEquals("val", result);
    }

    @Test
    void testEvaluateTextMissing() throws Exception {
        Document doc = NILRouterCommonUtility.parseXmlStringToDocument("<root></root>");
        String result = NILRouterCommonUtility.evaluateText(doc, "/root/a");
        assertEquals("", result);
    }

    @Test
    void testEvaluateTextException() {
        String result = NILRouterCommonUtility.evaluateText(null, "bad[");
        assertEquals("", result);
    }
}

