package com.clicks.fulafiaresultcheckingverificationsystem.utils;

public class SessionCompare {

    public static boolean compareSession(String first, String second) {

        String[] firstArray = first.split("/");
        String[] secondArray = second.split("/");

        int upperFirst = Integer.parseInt(firstArray[0]);
        int lowerFirst = Integer.parseInt(firstArray[1]);

        int upperSecond = Integer.parseInt(secondArray[0]);
        int lowerSecond = Integer.parseInt(secondArray[1]);

        return (upperFirst >= upperSecond && lowerFirst >= lowerSecond);

    }
}
