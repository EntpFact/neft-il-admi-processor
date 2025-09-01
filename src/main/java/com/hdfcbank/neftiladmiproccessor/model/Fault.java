package com.hdfcbank.neftiladmiproccessor.model;

import lombok.*;

@Setter
@Getter
@Builder
@ToString
public class Fault {
    String errorType;
    String responseStatusCode;
    String errorCode;
    String errorDescription;

}

