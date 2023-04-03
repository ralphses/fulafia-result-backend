package com.clicks.fulafiaresultcheckingverificationsystem.enums;

import lombok.Getter;

@Getter
public enum Level {
    FRESHMAN(100),
    DIRECT_ENTRY(200);

    private final int value;

    Level(int value) {
        this.value = value;
    }
}
