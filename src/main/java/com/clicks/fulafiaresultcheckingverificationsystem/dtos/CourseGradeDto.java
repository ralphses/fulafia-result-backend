package com.clicks.fulafiaresultcheckingverificationsystem.dtos;

public record CourseGradeDto(
        String courseCode,
        String courseTitle,
        Integer courseUnit,
        String grade,
        Integer gradePoint,
        String remarks
) {
}
