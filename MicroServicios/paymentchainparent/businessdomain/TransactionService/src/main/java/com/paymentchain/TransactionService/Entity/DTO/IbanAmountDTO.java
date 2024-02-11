package com.paymentchain.TransactionService.Entity.DTO;

import lombok.Data;

@Data
public class IbanAmountDTO {

    private String iban;
    private Double saldo;
}
