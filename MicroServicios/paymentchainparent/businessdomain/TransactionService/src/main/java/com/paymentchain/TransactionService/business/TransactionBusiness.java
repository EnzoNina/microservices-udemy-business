package com.paymentchain.TransactionService.business;

import com.paymentchain.TransactionService.Entity.Transaction;
import com.paymentchain.TransactionService.Service.IService.ITransactionService;
import com.paymentchain.TransactionService.exception.BusinessRuleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TransactionBusiness {

          private static final Logger log = LoggerFactory.getLogger(TransactionBusiness.class);

          @Autowired
          private ITransactionService transactionService;

          public Transaction save(Transaction input) throws BusinessRuleException {
                    Transaction transaction;

                    //Verificamos si es un retiro
                    if (input.getAmount() < 0) {
                              //Verificar el dinero disponible de la cuenta
                              double money = returnAccountTotalMoney(input.getAccountIban());
                              log.info("money" + money);
                              //Calculamos la comision
                              double comision = Math.abs(input.getAmount()) * 0.0098; // 0.98%
                              log.info("comision" + comision);
                              //Calculamos el monto de la transacciòn total junto con la comisiòn
                              double amountTransactionTotal = input.getAmount() + comision;
                              log.info("amountTransactionTotal" + amountTransactionTotal);
                              double moneyPostTransaction = money +  amountTransactionTotal;
                              log.info("moneyPostTransaction" + moneyPostTransaction);
                              if (moneyPostTransaction <= 0) {
                                        log.error("EXCEPTION!!");
                                        BusinessRuleException exception = new BusinessRuleException("T-E050", HttpStatus.NOT_ACCEPTABLE, "Saldo insuficiente");
                                        throw exception;
                              }
                              input.setAmount(amountTransactionTotal);
                              input.setFee(comision);
                    }
                    log.info(input.toString());
                    transaction = transactionService.save(input);
                    return transaction;
          }

          public double returnAccountTotalMoney(String ibanAccount) {
                    return transactionService.getAmountByAccountIban(ibanAccount);
          }

}
