package com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests;

import com.clicks.fulafiaresultcheckingverificationsystem.dtos.DepartmentCourseDto;

import java.util.List;

public record AddDepartmentCoursesRequest(
        String department,
        List<DepartmentCourseDto> courses
) {
}
