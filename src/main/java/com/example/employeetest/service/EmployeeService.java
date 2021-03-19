package com.example.employeetest.service;

import com.example.employeetest.dto.DepartmentDto;
import com.example.employeetest.exception.BadFileException;
import com.example.employeetest.mapper.DepartmentMapper;
import com.example.employeetest.model.Employee;
import com.example.employeetest.model.enums.Department;
import com.example.employeetest.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentMapper departmentMapper;

    public void store(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new BadFileException("File is empty");
        }
        if (!"text/csv".equals(multipartFile.getContentType())) {
            throw new BadFileException("File must have text/csv format but was " + multipartFile.getContentType());
        }
        employeeRepository.save(multipartFile);
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee getWithHighestSalaryPerDepartment(Department department) {
        return employeeRepository.getWithHighestSalaryPerDepartment(department);
    }

    public List<DepartmentDto> getAllEmployeesSortedBySalaryPerDepartment() {
        List<Employee> employees = employeeRepository.getAllEmployeesSortedBySalaryPerDepartment();
        List<DepartmentDto> departments = new LinkedList<>();
        Arrays.stream(Department.values())
                .forEach(department ->
                        departments.add(
                                departmentMapper.employeesToDepartmentDto(department,
                                        employees.stream()
                                                .filter(employee -> employee.getDepartment().equals(department))
                                                .collect(Collectors.toList())
                                )));
        return departments;
    }
}
