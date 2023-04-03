package com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests;

public record UssdRequest(
        String sessionId,
        String phoneNumber,
        String networkCode,
        String serviceCode,
        String text
) {
}
