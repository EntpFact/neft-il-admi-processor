package com.hdfcbank.neftiladmiproccessor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionAudit {

    private String msgId;
    private String txnId;
    private String endToEndId;
    private String batchId;
    private String returnId;
    private String source;
    private String target;
    private String flowType;
    private String msgType;
    private BigDecimal amount;
    private String status;
    private BigDecimal version;
    private LocalDate batchCreationDate;
    private LocalDateTime batchTimestamp;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTimestamp;
}
