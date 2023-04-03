package com.clicks.fulafiaresultcheckingverificationsystem.service;

import com.clicks.fulafiaresultcheckingverificationsystem.utils.DtoMapper;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.DepartmentCourseDto;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.DepartmentDto;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests.AddDepartmentCoursesRequest;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests.NewDepartmentRequest;
import com.clicks.fulafiaresultcheckingverificationsystem.exceptions.ResourceNotFoundException;
import com.clicks.fulafiaresultcheckingverificationsystem.model.department.Department;
import com.clicks.fulafiaresultcheckingverificationsystem.model.department.DepartmentCourse;
import com.clicks.fulafiaresultcheckingverificationsystem.repository.DepartmentCourseRepository;
import com.clicks.fulafiaresultcheckingverificationsystem.repository.DepartmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentCourseRepository departmentCourseRepository;
    private final DtoMapper dtoMapper;

    public List<DepartmentDto> getDepartments(Integer page) {

        return departmentRepository.findAll(PageRequest.of((page <= 1) ? 0 : page, 10))
                .map(dtoMapper.departmentDtoConverter)
                .getContent();
    }

    public void addDepartment(NewDepartmentRequest newDepartmentRequest) {

        List<DepartmentCourse> departmentCourses = newDepartmentRequest.courses()
                .stream()
                .map(dtoMapper.departmentCourseDtoConverter)
                .toList();

        List<DepartmentCourse> savedDepartmentCourses = departmentCourseRepository.saveAllAndFlush(departmentCourses);

        Department department = Department.builder()
                .name(newDepartmentRequest.name())
                .code(newDepartmentRequest.code())
                .courses(savedDepartmentCourses)
                .build();

        departmentRepository.save(department);
    }

    public void addNewCourses(AddDepartmentCoursesRequest addDepartmentCoursesRequest) {

        Department department = findDepartmentByName(addDepartmentCoursesRequest.department());

        List<DepartmentCourse> departmentCourses = addDepartmentCoursesRequest.courses()
                .stream()
                .map(dtoMapper.departmentCourseDtoConverter)
                .toList();

        departmentCourseRepository.saveAllAndFlush(departmentCourses);

        department.getCourses().addAll(departmentCourses);
    }

    public Department findDepartmentByName(String departmentName) {
        return departmentRepository.findDepartmentByName(departmentName)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid department name " + departmentName));
    }

    public Department findDepartmentByCode(String code) {
        return departmentRepository.findDepartmentByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid department code " + code));
    }

    public List<DepartmentCourseDto> getCourses(String code) {

        return findDepartmentByCode(code).getCourses().stream()
                .map(dtoMapper.departmentCourseToDepartmentCourseDto)
                .toList();
    }

    public DepartmentDto findDepartment(String departmentName) {
        return Stream.of(findDepartmentByName(departmentName))
                .map(dtoMapper.departmentDtoConverter).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Invalid department name " + departmentName));

    }
}
