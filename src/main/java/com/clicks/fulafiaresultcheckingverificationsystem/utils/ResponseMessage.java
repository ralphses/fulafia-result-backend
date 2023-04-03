package com.clicks.fulafiaresultcheckingverificationsystem.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@AllArgsConstructor
@Data
public class ResponseMessage {

    private String message;
    private int code;

    private Map<String, Object> responseBody;

}
