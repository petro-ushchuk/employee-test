package com.example.employeetest.dto;

import com.example.employeetest.model.Employee;
import com.example.employeetest.model.enums.Department;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DepartmentDto {
    Department department;
    List<Employee> employees;
}
