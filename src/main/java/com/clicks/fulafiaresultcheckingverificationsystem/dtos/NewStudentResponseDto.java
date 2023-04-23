package com.clicks.fulafiaresultcheckingverificationsystem.dtos;

public record NewStudentResponseDto(
        String name,
        String matric,
        String resultCode,
        String passCode
) {
}
