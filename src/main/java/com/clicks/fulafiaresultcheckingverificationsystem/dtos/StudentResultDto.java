package com.clicks.fulafiaresultcheckingverificationsystem.dtos;


import java.util.List;

public record StudentResultDto(

        String name,
        String level,
        String session,
        String semester,
        String remarks,

        ResultAnalysisDto current,

        List<CourseGradeDto> courseGrades


) {
}
