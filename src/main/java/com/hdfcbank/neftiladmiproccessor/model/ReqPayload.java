package com.hdfcbank.neftiladmiproccessor.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReqPayload {

    private Header header;
    private Body body;

    public ReqPayload(ReqPayload reqPayload) {
        this.header = reqPayload.header;
        this.body = reqPayload.body;
    }
}
