package com.paymentchain.TransactionService.Controller;

import com.paymentchain.TransactionService.Entity.Transaction;
import com.paymentchain.TransactionService.Service.IService.ITransactionService;
import com.paymentchain.TransactionService.business.TransactionBusiness;
import com.paymentchain.TransactionService.exception.BusinessRuleException;
import java.util.HashMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/")
public class TransactionRestController {

          private final ITransactionService transactionService;

          private TransactionBusiness tb;

          public TransactionRestController(ITransactionService transcationService, TransactionBusiness tb) {
                    this.transactionService = transcationService;
                    this.tb = tb;
          }

          @GetMapping("transaction")
          public ResponseEntity<?> list() {
                    Map<String, Object> response = new HashMap<>();
                    List<Transaction> lstTransaction = transactionService.findAll();
                    response.put("Transacciones", lstTransaction);
                    return new ResponseEntity<>(response, HttpStatus.OK);
          }

          @GetMapping("transaction/{id}")
          public ResponseEntity<?> getById(@PathVariable(name = "id") Integer id) {
                    Map<String, Object> response = new HashMap<>();
                    try {
                              Transaction transaction = transactionService.findById(id);
                              if (transaction != null) {
                                        response.put("Message", "La transacción se encontro con éxito");
                                        response.put("transaction", transaction);
                                        return new ResponseEntity<>(response, HttpStatus.OK);
                              } else {
                                        response.put("Message", "No se pudo encontrar la transaction");
                                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                              }

                    } catch (DataAccessException e) {
                              response.put("Message", "Hubo un error");
                              response.put("Error", e.getMessage());
                              return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                    }
          }

          @GetMapping("transaction/customer/transactions")
          public List<?> getAllByAccountIban(@RequestParam(name = "iban") String iban) {
                    List<Transaction> lstTransactions = transactionService.getTransactionByAccountIban(iban);
                    return lstTransactions;
          }

          @PutMapping("transaction/{id}")
          public ResponseEntity<?> put(@PathVariable(name = "id") Integer id, @RequestBody Transaction newTransaction) {
                    Map<String, Object> response = new HashMap<>();
                    try {
                              if (id != null) {
                                        response.put("Message", "La transacción se actualizó con éxito");
                                        Transaction transaction = transactionService.update(id, newTransaction);
                                        response.put("transaction", transaction);
                                        return new ResponseEntity<>(response, HttpStatus.OK);
                              } else {
                                        response.put("Message", "No se pudo encontrar la transaction");
                                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                              }

                    } catch (DataAccessException e) {
                              response.put("Message", "Hubo un error");
                              response.put("Error", e.getMessage());
                              return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                    }
          }

          @PostMapping("transaction")
          public ResponseEntity<?> post(@RequestBody Transaction input) throws BusinessRuleException {
                    Transaction transaction = tb.save(input);
                    return new ResponseEntity<>(transaction, HttpStatus.CREATED);
          }

          @GetMapping("transaction/amount/{iban}")
          public Double getTotalAmountByIbanAccount(@PathVariable(name = "iban") String iban) {
                    return transactionService.getAmountByAccountIban(iban);
          }

}
