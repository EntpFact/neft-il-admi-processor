package com.hdfcbank.neftiladmiproccessor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MsgEventTracker {

    private String msgId;
    private String source;
    private String target;
    private String batchId;
    private String flowType;
    private String msgType;
    private String originalReq;
    private Boolean invalidMsg;
    private Integer replayCount;
    private BigDecimal originalReqCount;
    private BigDecimal consolidateAmt;
    private String intermediateReq;
    private BigDecimal intemdiateCount;
    private String status;
    private LocalDate batchCreationDate;
    private LocalDateTime batchTimestamp;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTimestamp;
    private BigDecimal version;

    }