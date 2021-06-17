package com.mynotes.reactive.demo.webfluxinservlet.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = RestTemplateCustomException.class)
    public ResponseEntity<?> handleException(RestTemplateCustomException exception) {
        return ResponseEntity.status(exception.getStatus()).body(exception.getMsg());
    }

    @ResponseBody
    @ExceptionHandler(value = WebClientCustomException.class)
    public ResponseEntity<?> handleException(WebClientCustomException exception) {
        return ResponseEntity.status(exception.getStatus()).body(exception.getMsg());
    }

}
