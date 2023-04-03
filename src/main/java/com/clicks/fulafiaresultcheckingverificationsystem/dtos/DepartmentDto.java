package com.clicks.fulafiaresultcheckingverificationsystem.dtos;

import java.util.List;

public record DepartmentDto(
        String name,
        String code,
        List<DepartmentCourseDto> courses
) {
}
