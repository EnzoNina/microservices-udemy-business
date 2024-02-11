package com.paymentchain.TransactionService.Repository;

import com.paymentchain.TransactionService.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT t FROM Transaction t where t.accountIban =?1")
    Transaction findByAccountIban(String accountIban);
    
    @Query("SELECT SUM(t.amount) FROM Transaction t where t.accountIban =?1")
    Double getAmountByAccountIban(String accountIban);

}
