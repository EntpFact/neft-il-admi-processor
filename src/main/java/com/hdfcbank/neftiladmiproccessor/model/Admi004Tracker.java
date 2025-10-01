package com.hdfcbank.neftiladmiproccessor.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class Admi004Tracker {

    private String msgId;
    private String msgType;
    private String originalReq;
    private String target;
    private BigDecimal version;
    private Integer replayCount;
    private String transformedJsonReq;
    private Boolean invalidMsg;
    private LocalDate batchCreationDate;
    private LocalDateTime batchTimestamp;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTimestamp;
    private String status;
}