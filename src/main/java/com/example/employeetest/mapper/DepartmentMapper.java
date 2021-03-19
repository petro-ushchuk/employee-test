package com.example.employeetest.mapper;

import com.example.employeetest.dto.DepartmentDto;
import com.example.employeetest.model.Employee;
import com.example.employeetest.model.enums.Department;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DepartmentMapper {

    public DepartmentDto employeesToDepartmentDto(Department department, List<Employee> employeeList){
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setEmployees(employeeList);
        departmentDto.setDepartment(department);
        return departmentDto;
    }
}
