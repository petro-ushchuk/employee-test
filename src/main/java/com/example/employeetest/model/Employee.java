package com.example.employeetest.model;

import com.example.employeetest.model.enums.Department;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @CsvBindByName
    private String employee;
    @CsvBindByName
    private Department department;
    @CsvBindByName
    private Integer salary;
}
