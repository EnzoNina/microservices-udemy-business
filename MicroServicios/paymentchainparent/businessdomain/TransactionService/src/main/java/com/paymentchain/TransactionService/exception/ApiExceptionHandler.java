package com.paymentchain.TransactionService.exception;

import com.paymentchain.TransactionService.common.StandarizedApiExceptionResponse;
import java.net.UnknownHostException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

          @ExceptionHandler(UnknownHostException.class)
          public ResponseEntity<StandarizedApiExceptionResponse> handleUnknownHostException(UnknownHostException ex) {
                    StandarizedApiExceptionResponse response = new StandarizedApiExceptionResponse("Error Host", "T-001", ex.getMessage());
                    return new ResponseEntity(response, HttpStatus.PARTIAL_CONTENT);
          }

          @ExceptionHandler(BusinessRuleException.class)
          public ResponseEntity<StandarizedApiExceptionResponse> handleBusinessCostumerException(BusinessRuleException ex) {
                    StandarizedApiExceptionResponse response = new StandarizedApiExceptionResponse("Error de validaciòn", ex.getCode(), ex.getMessage());
                    return new ResponseEntity(response, ex.getHttpStatus());
          }
}
