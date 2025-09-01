package com.hdfcbank.neftiladmiproccessor.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class Header {

    private String msgId;
    private String source;
    private String target;
    private String msgType;
    private String flowType;
    private boolean replayInd;
    private boolean invalidPayload;
    private String prefix;

}
