package com.paymentchain.TransactionService.exception;

import org.springframework.http.HttpStatus;

//Expction general para el modelo de negocio
public class BusinessRuleException extends Exception {

    private long id;

    private String code;

    private HttpStatus httpStatus;

    public BusinessRuleException(long id, String code, HttpStatus httpStatus) {
        this.id = id;
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public BusinessRuleException(String code, HttpStatus httpStatus, String message) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessRuleException() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

}
