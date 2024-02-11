package com.paymentchain.TransactionService.Service.IService;

import com.paymentchain.TransactionService.Entity.Transaction;
import java.util.List;

public interface ITransactionService {

    List<Transaction> findAll();

    Transaction findById(Integer id);

    Transaction findByAccountIban(String accountIban);

    Transaction save(Transaction transaction);

    Transaction update(Integer id, Transaction transaction);

}
