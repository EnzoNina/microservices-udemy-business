package com.paymentchain.TransactionService.Service.Impl;

import com.paymentchain.TransactionService.Entity.DTO.IbanAmountDTO;
import com.paymentchain.TransactionService.Entity.Status;
import com.paymentchain.TransactionService.Entity.Transaction;
import com.paymentchain.TransactionService.Repository.TransactionRepository;
import com.paymentchain.TransactionService.Service.IService.ITransactionService;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ITransactionServiceImpl implements ITransactionService {

    private final TransactionRepository transactionRepository;

    public ITransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction findByAccountIban(String accountIban) {
        return transactionRepository.findByAccountIban(accountIban);
    }

    @Override
    public Transaction save(Transaction transaction) {

        //Comision > 0 
        if (transaction.getFee() > 0) {
            Double newAmout = (transaction.getAmount() - transaction.getFee());
            transaction.setAmount(newAmout);
        }
        //Fechas
        // Establecimiento del estado de la transacción
        Date currentDate = new Date();
        if (transaction.getDate().after(currentDate)) {
            transaction.setStatus(Status.PENDIENTE);
        } else {
            transaction.setStatus(Status.LIQUIDADO);
        }

        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction update(Integer id, Transaction transaction) {
        Transaction originalTransaction = transactionRepository.findById(id).orElse(null);

        if (originalTransaction != null) {
            originalTransaction.setDate(transaction.getDate());
            originalTransaction.setAmount(transaction.getAmount());
            originalTransaction.setFee(transaction.getFee());
            originalTransaction.setDescription(transaction.getDescription());
            originalTransaction.setStatus(transaction.getStatus());
            originalTransaction.setChannel(transaction.getChannel());
        }
        return transactionRepository.save(originalTransaction);
    }

    @Override
    public Transaction findById(Integer id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    public Double getAmountByAccountIban(String accountIban) {
        return transactionRepository.getAmountByAccountIban(accountIban);
    }

    @Override
    public List<IbanAmountDTO> getAmountTotalByAccountIban() {
        return transactionRepository.getAmountTotalGroupByAccountIban();
    }

    @Override
    public List<Transaction> getTransactionByAccountIban(String accountIban) {
        return transactionRepository.getTransactionByAccountIban(accountIban);
    }

}
