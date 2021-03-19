package com.example.employeetest.service;

import com.example.employeetest.exception.BadFileException;
import com.example.employeetest.model.Employee;
import com.example.employeetest.model.enums.Department;
import com.example.employeetest.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

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

    public List<Employee> getAllEmployeesSortedBySalaryPerDepartment() {
        return employeeRepository.getAllEmployeesSortedBySalaryPerDepartment();
    }
}
