package com.example.employeetest.controller;


import com.example.employeetest.model.Employee;
import com.example.employeetest.model.enums.Department;
import com.example.employeetest.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "Employee management API")
@ApiResponses({
        @ApiResponse(code = 404, message = "Not found"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @ApiOperation("Upload the list of employees.")
    @PostMapping(value = "/upload-employees")
    @ApiResponse(code = 200, message = "Uploaded")
    public ResponseEntity<Void> handleFileUpload(@RequestParam("file") MultipartFile file) {
        employeeService.store(file);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation("Get list of all employees.")
    @ApiResponse(code = 200, message = "OK", response = List.class)
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> getAllEmployees() {
        return employeeService.findAll();
    }

    @ApiOperation("Get employees with the highest salary per department.")
    @ApiResponse(code = 200, message = "OK", response = Employee.class)
    @GetMapping("/highest-salary-in-{department}")
    @ResponseStatus(HttpStatus.OK)
    public Employee getEmployeesWithHighestSalaryPerDepartment(@PathVariable Department department) {
        return employeeService.getWithHighestSalaryPerDepartment(department);
    }

    @ApiOperation("Get all departments where each department contains employees sorted by salary descending.")
    @ApiResponse(code = 200, message = "OK", response = List.class)
    @GetMapping("/deparments-sorted-by-salary")
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> getAllEmployeesSortedBySalaryPerDepartment() {
        return employeeService.getAllEmployeesSortedBySalaryPerDepartment();
    }
}
