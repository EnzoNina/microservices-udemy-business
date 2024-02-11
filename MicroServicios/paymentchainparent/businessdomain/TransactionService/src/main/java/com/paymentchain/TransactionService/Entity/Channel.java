package com.paymentchain.TransactionService.Entity;

public record Channel(String channel) {

    private static final Channel WEB = new Channel("WEB");
    private static final Channel CAJERO = new Channel("CAJERO");
    private static final Channel OFICINA = new Channel("OFICINA");

}
