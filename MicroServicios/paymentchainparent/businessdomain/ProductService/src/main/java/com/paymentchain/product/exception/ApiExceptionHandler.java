package com.paymentchain.product.exception;

import com.paymentchain.product.common.StandarizedApiExceptionResponse;
import java.net.UnknownHostException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(UnknownHostException.class)
    public ResponseEntity<StandarizedApiExceptionResponse> handleUnknownHostException(UnknownHostException ex) {
        StandarizedApiExceptionResponse response = new StandarizedApiExceptionResponse("Error general", "P-001", ex.getMessage());
        return new ResponseEntity(response, HttpStatus.PARTIAL_CONTENT);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<StandarizedApiExceptionResponse> handleBusinessCostumerException(BusinessRuleException ex) {
        StandarizedApiExceptionResponse response = new StandarizedApiExceptionResponse("Error de validaciòn", ex.getCode(), ex.getMessage());
        return new ResponseEntity(response, HttpStatus.PARTIAL_CONTENT);
    }
}
