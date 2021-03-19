package com.example.employeetest.repository;

import com.example.employeetest.exception.BadFileException;
import com.example.employeetest.exception.NotFoundException;
import com.example.employeetest.model.Employee;
import com.example.employeetest.model.enums.Department;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class EmployeeRepository {

    private final File data;

    public void save(MultipartFile multipartFile) {
        log.info("File uploading...");
        try (Reader reader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()));
             Writer writer = new OutputStreamWriter(new FileOutputStream(data), StandardCharsets.UTF_8)) {
            int c;
            if ((c = reader.read()) != 65279) {
                writer.write(c);
                log.info("DeBOMed...");
            }
            reader.transferTo(writer);
            reader.close();
            writer.close();
            validate(data);
        } catch (IOException e) {
            throw new RuntimeException("Can't save this file. Please, try again.");
        }
        log.info("Successfully uploaded");
    }

    private void validate(File file) {
        try (FileReader reader = new FileReader(file);
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                     .build()) {

            List<String> args = Arrays.stream(csvReader.readNext())
                    .map(a -> a.trim().toLowerCase())
                    .collect(Collectors.toList());

            List<String> fields = Arrays.stream(Employee.class.getDeclaredFields())
                    .map(Field::getName)
                    .collect(Collectors.toList());
            if (!args.equals(fields)) {
                throw new BadFileException("Invalid headers. Must be " + fields + " but was " + args);
            }
        } catch (IOException | CsvValidationException e) {
            throw new BadFileException(e.getCause().getMessage());
        }
        try (FileReader reader = new FileReader(file)) {
            List<Employee> parse = new CsvToBeanBuilder<Employee>(reader)
                    .withType(Employee.class)
                    .withSeparator(';')
                    .build()
                    .parse();
            log.info(parse.toString());
        } catch (IOException e) {
            throw new BadFileException(e.getCause().getMessage());
        }
    }

    public List<Employee> findAll() {
        List<Employee> employeeList;
        try (FileReader reader = new FileReader(data)) {
            employeeList = new CsvToBeanBuilder<Employee>(reader)
                    .withSeparator(';')
                    .withType(Employee.class)
                    .build()
                    .parse();
            log.info(String.valueOf(employeeList));
            if (employeeList == null || employeeList.isEmpty()) {
                throw new NotFoundException("There are no employees. Please, upload data first.");
            }
        } catch (IOException e) {
            throw new BadFileException("File is not valid. It must contain Employee information.");
        }
        return employeeList;
    }

    public Employee getWithHighestSalaryPerDepartment(Department department) {
        Optional<Employee> employee;
        try (FileReader reader = new FileReader(data)) {
            employee = new CsvToBeanBuilder<Employee>(reader)
                    .withSeparator(';')
                    .withType(Employee.class)
                    .build()
                    .parse()
                    .stream()
                    .filter(e ->
                            e.getDepartment().equals(department))
                    .max(Comparator.comparing(Employee::getSalary));
            log.info(String.valueOf(employee));
            if (employee.isEmpty()) {
                throw new NotFoundException("There are no employees. Please, upload data first.");
            }
        } catch (IOException e) {
            throw new BadFileException("File is not valid. It must contain Employee information.");
        }
        return employee.get();
    }

    public List<Employee> getAllEmployeesSortedBySalaryPerDepartment() {
        List<Employee> employee;
        try (FileReader reader = new FileReader(data)) {
            employee = new CsvToBeanBuilder<Employee>(reader)
                    .withSeparator(';')
                    .withType(Employee.class)
                    .build()
                    .parse()
                    .stream()
                    .sorted((e1, e2) -> Integer.compare(e2.getSalary(), e1.getSalary()))
                    .sorted(Comparator.comparing(Employee::getDepartment))
                    .collect(Collectors.toList());
            log.info(String.valueOf(employee));
            if (employee.isEmpty()) {
                throw new NotFoundException("There are no employees. Please, upload data first.");
            }
        } catch (IOException e) {
            throw new BadFileException("File is not valid. It must contain Employee information.");
        }
        return employee;
    }
}
