package com.paymentchain.TransactionService.Repository;

import com.paymentchain.TransactionService.Entity.DTO.IbanAmountDTO;
import com.paymentchain.TransactionService.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT t FROM Transaction t where t.accountIban =?1")
    Transaction findByAccountIban(String accountIban);

    @Query("SELECT SUM(t.amount) FROM Transaction t where t.accountIban =?1")
    Double getAmountByAccountIban(String accountIban);

    @Query("SELECT t.accountIban,SUM(t.amount) FROM Transaction t GROUP BY t.accountIban")
    List<IbanAmountDTO> getAmountTotalGroupByAccountIban();

    @Query("SELECT t FROM Transaction t where t.accountIban =?1")
    List<Transaction> getTransactionByAccountIban(String accountIban);

}
