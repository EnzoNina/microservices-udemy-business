package com.paymentchain.TransactionService.Service.IService;

import com.paymentchain.TransactionService.Entity.DTO.IbanAmountDTO;
import com.paymentchain.TransactionService.Entity.Transaction;
import java.util.List;

public interface ITransactionService {

    List<Transaction> findAll();

    Transaction findById(Integer id);

    Transaction findByAccountIban(String accountIban);

    Transaction save(Transaction transaction);

    Transaction update(Integer id, Transaction transaction);

    Double getAmountByAccountIban(String accountIban);
    
    List<Transaction> getTransactionByAccountIban(String accountIban);

    List<IbanAmountDTO> getAmountTotalByAccountIban();

}
