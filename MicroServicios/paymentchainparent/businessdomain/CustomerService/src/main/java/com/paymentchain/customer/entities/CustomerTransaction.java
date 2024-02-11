package com.paymentchain.customer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Entity
@Data
@Table(name = "customer_transaction")
public class CustomerTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_customer_transaction")
    private Integer id;

    @Column(name = "id_transaction")
    private Integer transactiontId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Customer.class)
    @JoinColumn(name = "id_customer", nullable = true)
    private Customer customer;

    @Transient
    private String reference; //Identificador unico de la transacción para el negocio

    @Transient
    private String accountIban; //(Número de cuenta bancaria del cliente)

    @Transient
    private Date date; //(Fecha en que se realizazo la transacción)

    @Transient
    private Double amount; //(Monto de la transacción, si el valor es negativo sera un debito(disminuye el saldo), si el valor es positivo sera un credito (aumenta el saldo))

    @Transient
    private Double fee; //(Cómision de la transacción)

    @Transient
    private String description; //(Descripción breve de la transacción)

    @Transient
    private String status; //(Guarda el estado de la transacción y podrá ser uno de los siguientes códigos numéricos que representa los estados descritos:

    @Transient
    private String channel; //Canal por donde se realizo una transacción

}
