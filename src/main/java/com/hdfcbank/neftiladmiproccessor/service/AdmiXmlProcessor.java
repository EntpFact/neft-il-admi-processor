package com.hdfcbank.neftiladmiproccessor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdfcbank.neftiladmiproccessor.kafkaproducer.KafkaUtils;
import com.hdfcbank.neftiladmiproccessor.model.Admi004Tracker;
import com.hdfcbank.neftiladmiproccessor.model.ReqPayload;
import com.hdfcbank.neftiladmiproccessor.repository.NilRepository;
import com.hdfcbank.neftiladmiproccessor.utils.NILRouterCommonUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static com.hdfcbank.neftiladmiproccessor.utils.Constants.*;

@Service
public class AdmiXmlProcessor {

    @Autowired
    NILRouterCommonUtility nilRouterCommonUtility;

    @Autowired
    private NilRepository nilRepository;

    @Autowired
    private KafkaUtils kafkaUtils;

    @Value("${topic.dispatchertopic}")
    private String dispatchertopic;

    @Autowired
    private ObjectMapper objectMapper;


    public void parseXml(ReqPayload reqPayload) throws Exception {
        String xmlString = reqPayload.getBody().getPayload();
        Document originalDoc = NILRouterCommonUtility.parseXmlStringToDocument(xmlString);
        originalDoc.getDocumentElement().normalize();

        String msgId = NILRouterCommonUtility.getBizMsgIdr(originalDoc);
        String batchCreationDate = NILRouterCommonUtility.getBatchCreationDate(originalDoc);
        String evtCd = NILRouterCommonUtility.evaluateText(originalDoc, "//*[local-name()='Document']//*[local-name()='EvtCd']");
        String msgType = NILRouterCommonUtility.getMsgDefIdr(originalDoc);

        Admi004Tracker admi004Tracker = null;
        Admi004Tracker fcAdmi004Tracker = null;
        Admi004Tracker ephAdmi004Tracker = null;
        ReqPayload fcReqPayload = new ReqPayload(reqPayload);
        ReqPayload ephReqPayload = new ReqPayload(reqPayload);

        if (evtCd.equalsIgnoreCase("F95")) {
            fcReqPayload.getHeader().setTarget(FC_DISPATCHER);
            ephReqPayload.getHeader().setTarget(EPH_DISPATCHER);

            String fcReqPayloadString = objectMapper.writeValueAsString(fcReqPayload);
            String ephReqPayloadString = objectMapper.writeValueAsString(ephReqPayload);

            fcAdmi004Tracker = buildAdmiTracker(msgId, msgType, batchCreationDate, reqPayload.getHeader().getPrefix() + xmlString, FC_DISPATCHER, fcReqPayloadString);
            ephAdmi004Tracker = buildAdmiTracker(msgId, msgType, batchCreationDate, reqPayload.getHeader().getPrefix() + xmlString, EPH_DISPATCHER, ephReqPayloadString);

        } else {

            char ch = msgId.charAt(13);
            if (ch >= '0' && ch <= '4') {
                reqPayload.getHeader().setTarget(FC_DISPATCHER);
                String reqPayloadString = objectMapper.writeValueAsString(reqPayload);
                admi004Tracker = buildAdmiTracker(msgId, msgType, batchCreationDate, reqPayload.getHeader().getPrefix() + xmlString, FC_DISPATCHER, reqPayloadString);

            } else if (ch >= '5' && ch <= '9') {
                reqPayload.getHeader().setTarget(EPH_DISPATCHER);
                String reqPayloadString = objectMapper.writeValueAsString(reqPayload);
                admi004Tracker = buildAdmiTracker(msgId, msgType, batchCreationDate, reqPayload.getHeader().getPrefix() + xmlString, EPH_DISPATCHER, reqPayloadString);


            }
        }


        if (!nilRouterCommonUtility.duplicateExists(msgId)) {
            if (evtCd.equalsIgnoreCase("F95")) {

                nilRepository.updateAdmiTracker(fcAdmi004Tracker);
                nilRepository.insertAdmi004Tracker(ephAdmi004Tracker);
                String fcReqPayloadString = objectMapper.writeValueAsString(fcReqPayload);
                String ephReqPayloadString = objectMapper.writeValueAsString(ephReqPayload);
                kafkaUtils.publishToResponseTopic(fcReqPayloadString, dispatchertopic,msgId);
                kafkaUtils.publishToResponseTopic(ephReqPayloadString, dispatchertopic,msgId);

            } else {
                nilRepository.updateAdmiTracker(admi004Tracker);
                String reqPayloadString = objectMapper.writeValueAsString(reqPayload);
                kafkaUtils.publishToResponseTopic(reqPayloadString, dispatchertopic,msgId);
            }

        }


    }

    private Admi004Tracker buildAdmiTracker(String msgId, String msgType, String batchCreationDate,
                                            String xmlString, String target,String reqPayloadString) {
        return Admi004Tracker.builder()
                .msgId(msgId)
                .msgType(msgType)
                .batchTimestamp(OffsetDateTime.parse(batchCreationDate).toLocalDateTime())
                .batchCreationDate(OffsetDateTime.parse(batchCreationDate).toLocalDate())
                .status(SENT_TO_DISPATCHER)
                .originalReq(xmlString)
                .target(target)
                .version(BigDecimal.ONE)
                .replayCount(0)
                .invalidMsg(false)
                .transformedJsonReq(reqPayloadString)
                .build();
    }
}
