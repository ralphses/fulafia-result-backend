package com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests;

import com.clicks.fulafiaresultcheckingverificationsystem.dtos.DepartmentCourseDto;

import java.util.List;

public record NewDepartmentRequest(
        String name,
        String code,
        List<DepartmentCourseDto> courses
) {
}
