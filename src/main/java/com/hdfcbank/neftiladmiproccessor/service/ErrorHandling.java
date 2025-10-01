package com.hdfcbank.neftiladmiproccessor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdfcbank.neftiladmiproccessor.kafkaproducer.KafkaUtils;
import com.hdfcbank.neftiladmiproccessor.model.Admi004Tracker;
import com.hdfcbank.neftiladmiproccessor.model.ReqPayload;
import com.hdfcbank.neftiladmiproccessor.repository.NilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static com.hdfcbank.neftiladmiproccessor.utils.Constants.*;

@Service
public class ErrorHandling {


    @Autowired
    KafkaUtils kafkaUtils;

    @Value("${topic.errortopic}")
    private String errortopic;

    @Value("${topic.dispatchertopic}")
    private String dispatchertopic;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NilRepository nilRepository;

    public void handleInvalidPayload(ReqPayload reqPayload) throws JsonProcessingException {

        ReqPayload fcReqPayload = new ReqPayload(reqPayload);
        fcReqPayload.getHeader().setTarget(FC_DISPATCHER);
        String fcreqPayloadString = objectMapper.writeValueAsString(reqPayload);

        ReqPayload ephReqPayload = new ReqPayload(reqPayload);
        ephReqPayload.getHeader().setTarget(EPH_DISPATCHER);
        String ephReqPayloadString = objectMapper.writeValueAsString(reqPayload);

        Admi004Tracker fcAdmi004Tracker = null;
        Admi004Tracker ephAdmi004Tracker = null;

        fcAdmi004Tracker=buildAdmiTracker(reqPayload.getHeader().getMsgId(), reqPayload.getHeader().getMsgType(), reqPayload.getBody().getPayload(), FC_DISPATCHER,fcreqPayloadString);
        ephAdmi004Tracker=buildAdmiTracker(reqPayload.getHeader().getMsgId(), reqPayload.getHeader().getMsgType(), reqPayload.getBody().getPayload(), EPH_DISPATCHER,ephReqPayloadString);

        nilRepository.updateAdmiTracker(fcAdmi004Tracker);
        nilRepository.insertAdmi004Tracker(ephAdmi004Tracker);

        kafkaUtils.publishToResponseTopic(fcreqPayloadString, dispatchertopic, fcReqPayload.getHeader().getMsgId());
        kafkaUtils.publishToResponseTopic(ephReqPayloadString, dispatchertopic, ephReqPayload.getHeader().getMsgId());


    }

    private Admi004Tracker buildAdmiTracker(String msgId, String msgType,
                                            String xmlString, String target,String reqPayloadString) {
        return Admi004Tracker.builder()
                .msgId(msgId)
                .msgType(msgType)
                .status(SENT_TO_DISPATCHER)
                .originalReq(xmlString)
                .target(target)
                .version(BigDecimal.ONE)
                .replayCount(0)
                .invalidMsg(true)
                .transformedJsonReq(reqPayloadString)
                .build();
    }
}
