package com.example.employeetest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class FileConfig {
    @Value("${spring.datasource.name}")
    private String path;

    @Bean(name = "data")
    public File getDataFile(){
        return new File(path);
    }
}
