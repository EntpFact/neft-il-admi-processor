package com.hdfcbank.neftiladmiproccessor.utils;

import com.hdfcbank.neftiladmiproccessor.model.MsgEventTracker;
import com.hdfcbank.neftiladmiproccessor.repository.NilRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;

@Slf4j
@Component
public class NILRouterCommonUtility {

    @Autowired
    private NilRepository nilRepository;

    public static String getBizMsgIdr(Document originalDoc) throws XPathExpressionException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        Node msgIdNode = (Node) xpath.evaluate("//*[local-name()='AppHdr']/*[local-name()='BizMsgIdr']", originalDoc, XPathConstants.NODE);
        return msgIdNode != null ? msgIdNode.getTextContent().trim() : null;
    }

    public static String getMsgDefIdr(Document originalDoc) throws XPathExpressionException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        Node msgTypeNode = (Node) xpath.evaluate("//*[local-name()='AppHdr']/*[local-name()='MsgDefIdr']", originalDoc, XPathConstants.NODE);
        return msgTypeNode != null ? msgTypeNode.getTextContent().trim() : null;

    }

    public static String getBatchCreationDate(Document originalDoc) throws XPathExpressionException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        Node batchCreationDateNode = (Node) xpath.evaluate("//*[local-name()='AppHdr']/*[local-name()='CreDt']", originalDoc, XPathConstants.NODE);
        return batchCreationDateNode != null ? batchCreationDateNode.getTextContent().trim() : null;

    }


    public static BigDecimal getTotalAmount(Document originalDoc) throws XPathExpressionException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        String totalAmountString = (String) xpath.evaluate("//*[local-name()='GrpHdr']/*[local-name()='TtlIntrBkSttlmAmt']", originalDoc, XPathConstants.STRING);
        BigDecimal totalAmount = new BigDecimal(totalAmountString);
        return totalAmount;

    }





    /**
     * Parses an XML string into a Document object with namespace awareness.
     */
    public static Document parseXmlStringToDocument(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xml)));
    }

    /**
     * Converts a Document object to its XML string representation.
     */
    public static String documentToXmlString(Document doc) throws Exception {
        StringWriter writer = new StringWriter();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.toString();
    }

    public  boolean duplicateExists(String msgId) {
        MsgEventTracker duplicateEntry = nilRepository.findByMsgId(msgId);
        if (duplicateEntry != null) {
            nilRepository.saveDuplicateEntry(duplicateEntry);
            return true; // Skip processing if duplicate found
        }
        return false;
    }

    public static String evaluateText(Object item, String expression) {
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            Node result = (Node) xpath.evaluate(expression, item, XPathConstants.NODE);
            return result != null ? result.getTextContent().trim() : "";
        } catch (Exception e) {
            return "";
        }
    }






}
