package com.clicks.fulafiaresultcheckingverificationsystem.enums;

import lombok.Getter;

@Getter
public enum Grade {

    FAIL("F", 0, "Fail"),
    PASS("E", 1, "Pass"),
    GOOD("D", 2, "Good"),
    CREDIT("C", 3, "Credit"),
    VERY_GOOD("B", 4, "Very Good"),
    DISTINCTION("A", 5, "Distinction");

    private final String grade;
    private final int gp;
    private final String remark;

    Grade(String grade, int gp, String remark) {
        this.grade = grade;
        this.remark = remark;
        this.gp = gp;
    }
}
