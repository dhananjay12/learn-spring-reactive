package com.mynotes.reactive.demo.webfluxinservlet.controller.exceptions;

import org.springframework.http.HttpStatus;

public class RestTemplateCustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String msg;
    private HttpStatus status;

    public RestTemplateCustomException(String msg, HttpStatus status) {
        this.msg = msg;
        this.status = status;

    }

    public String getMsg() {
        return msg;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
