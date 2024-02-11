package com.paymentchain.TransactionService.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.Data;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String reference; //Identificador unico de la transacción para el negocio

    private String accountIban; //(Número de cuenta bancaria del cliente)

    private Date date; //(Fecha en que se realizazo la transacción)

    private Double amount; //(Monto de la transacción, si el valor es negativo sera un debito(disminuye el saldo), si el valor es positivo sera un credito (aumenta el saldo))

    private Double fee; //(Cómision de la transacción)

    private String description; // (Descripción breve de la transacción)

    private Status status; // (Guarda el estado de la transacción y podrá ser uno de los siguientes códigos numéricos que representa los estados descritos:

    private Channel channel; //Canal por donde se realizo una transacción

}
