package com.clicks.fulafiaresultcheckingverificationsystem.dtos;

public record ResultAnalysisDto(
        String totalCreditLoad,
        String totalCreditEarned,
        String totalGradePoint,
        String gradePointAverage
) {
}
