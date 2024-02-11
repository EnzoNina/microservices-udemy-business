package com.paymentchain.TransactionService.Entity;

public record Status(String estado) {

    public static final Status PENDIENTE = new Status("PENDIENTE");
    public static final Status LIQUIDADO = new Status("LIQUIDADO");
    public static final Status RECHAZADA = new Status("RECHAZADA");
    public static final Status CANCELADA = new Status("CANCELADA");

}
