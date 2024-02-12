package com.paymentchain.TransactionService.Controller;

import com.paymentchain.TransactionService.Entity.DTO.IbanAmountDTO;
import com.paymentchain.TransactionService.Entity.Transaction;
import com.paymentchain.TransactionService.Service.IService.ITransactionService;
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

    private final ITransactionService transcationService;

    public TransactionRestController(ITransactionService transcationService) {
        this.transcationService = transcationService;
    }

    @GetMapping("transaction")
    public ResponseEntity<?> list() {
        Map<String, Object> response = new HashMap<>();
        List<Transaction> lstTransaction = transcationService.findAll();
        response.put("Transacciones", lstTransaction);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("transaction/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Transaction transaction = transcationService.findById(id);
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
        List<Transaction> lstTransactions = transcationService.getTransactionByAccountIban(iban);
        return lstTransactions;
    }

    @PutMapping("transaction/{id}")
    public ResponseEntity<?> put(@PathVariable(name = "id") Integer id, @RequestBody Transaction newTransaction) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (id != null) {
                response.put("Message", "La transacción se actualizó con éxito");
                Transaction transaction = transcationService.update(id, newTransaction);
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
    public ResponseEntity<?> post(@RequestBody Transaction input) {
        Map<String, Object> response = new HashMap<>();

        try {
            Transaction transaction = transcationService.save(input);
            if (transaction != null) {
                response.put("Message", "La transacción se creo con éxito");
                response.put("transaction", transaction);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (DataAccessException e) {
            response.put("Message", "Hubo un error");
            response.put("Error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return null;
    }

    @GetMapping("transaction/amount/{iban}")
    public Double getTotalAmountByIbanAccount(@PathVariable(name = "iban") String iban) {
        return transcationService.getAmountByAccountIban(iban);
    }

}
