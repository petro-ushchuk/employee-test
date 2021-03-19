package com.example.employeetest.controller;

import com.example.employeetest.exception.BadFileException;
import com.example.employeetest.exception.NotFoundException;
import com.example.employeetest.model.enums.ErrorCode;
import com.example.employeetest.model.enums.ErrorType;
import com.example.employeetest.model.Error;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandlingController {

  @ExceptionHandler(BadFileException.class)
  @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  public Error handleMethodArgumentNotValidException(BadFileException ex) {
    return new Error(ex.getMessage(), ErrorCode.VALIDATION_ERROR_CODE, ErrorType.PROCESSING_ERROR_TYPE, LocalDateTime.now());

  }

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Error handleAbstractException(NotFoundException ex) {
    return new Error(ex.getMessage(), ErrorCode.APPLICATION_ERROR_CODE, ErrorType.PROCESSING_ERROR_TYPE, LocalDateTime.now());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Error handleAllException(Exception ex) {
    return new Error(ex.getMessage(), ErrorCode.APPLICATION_ERROR_CODE, ErrorType.FATAL_ERROR_TYPE,
        LocalDateTime.now());
  }

}
