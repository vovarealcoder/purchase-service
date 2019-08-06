package com.vova.purchaseservice.rest.controller;

import com.vova.purchaseservice.ex.InvalidCriteriaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleMissingPathVariable(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleMissingServletRequestParameter(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        return getValidationResponseEntity(headers, status, ex.getBindingResult());

    }

    private ResponseEntity<Object> getValidationResponseEntity(HttpHeaders headers, HttpStatus status, BindingResult bindingResult) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        List<String> errors = bindingResult
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        body.put("errors", errors);
        return new ResponseEntity<>(body, headers, status);
    }


    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return getValidationResponseEntity(headers, status, ex.getBindingResult());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleNoHandlerFoundException(ex, headers, status, request);
    }


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> bodyMap = new LinkedHashMap<>();
        bodyMap.put("timestamp", new Date());
        bodyMap.put("status", status.value());
        List<String> errors = Collections.singletonList(messageSource.getMessage("error.internal", new String[]{ex.getMessage()}, request.getLocale()));
        bodyMap.put("errors", errors);
        return new ResponseEntity<>(bodyMap, headers, status);
    }


    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidCriteriaException.class)
    public ResponseEntity<Object> handleConflict(InvalidCriteriaException ex, HttpServletRequest req) {
        Map<String, Object> bodyMap = new LinkedHashMap<>();
        bodyMap.put("timestamp", new Date());
        bodyMap.put("status", HttpStatus.BAD_REQUEST.value());
        List<String> errors = Collections.singletonList(messageSource.getMessage("error.invalid-criteria", new String[]{ex.getMessage()}, req.getLocale()));
        bodyMap.put("errors", errors);
        return new ResponseEntity<>(bodyMap, HttpStatus.BAD_REQUEST);
    }
}
