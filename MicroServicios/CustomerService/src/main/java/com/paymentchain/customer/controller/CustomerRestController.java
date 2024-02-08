package com.paymentchain.customer.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.repository.CustomerRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/")
public class CustomerRestController {

    //Inyectamos por Constructor
    private CustomerRepository customerRepository;

    public CustomerRestController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("customer")
    public List<Customer> list() {
        return customerRepository.findAll();
    }

    @GetMapping("customer/{id}")
    public ResponseEntity<?> get(@PathVariable int id) {

        Map<String, Object> response = new HashMap<String, Object>();
        Customer customer = customerRepository.findById(id).orElse(null);
        try {
            if (customer != null) {
                response.put("message", "El Customer fue encontrado con éxito");
                response.put("customer", customer);
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

            } else {
                response.put("message", "El customer no existe en la base de datos");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            response.put("Error", e.getMessage().toString());
            response.put("Message", "Hubo un error al acceder a la base de datos: " + e.getLocalizedMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("customer/{id}")
    public ResponseEntity<?> put(@PathVariable int id, @RequestBody Customer customer) {
        //Capturamos el antiguo Customer
        Customer oldCustomer = customerRepository.findById(id).orElse(null);
        oldCustomer.setNombre(customer.getNombre());
        oldCustomer.setPhone(customer.getPhone());
        Map<String, Object> response = new HashMap<String, Object>();
        try {
            if (oldCustomer != null) {
                Customer newCustomer = customerRepository.save(oldCustomer);
                response.put("message", "El Customer fue actualizado con éxito!");
                response.put("customer", newCustomer);
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

            } else {
                response.put("message", "El customer no existe en la base de datos");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            response.put("Error", e.getMessage().toString());
            response.put("Message", "Hubo un error al acceder a la base de datos: " + e.getLocalizedMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("customer")
    public ResponseEntity<?> post(@RequestBody Customer customer) {
        Map<String, Object> response = new HashMap<String, Object>();
        try {
            Customer customerCreated = customerRepository.save(customer);
            response.put("Message", "El cliente fue creado con éxito!");
            response.put("Customer", customerCreated);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            response.put("Error", e.getMessage().toString());
            response.put("Message", "Hubo un error al acceder a la base de datos: " + e.getLocalizedMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("customer/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        Map<String, Object> response = new HashMap<String, Object>();

        Customer customer = customerRepository.findById(id).orElse(null);
        try {
            if (customer != null) {
                customerRepository.delete(customer);
                response.put("message", "El Customer fue borrado con éxito!");
                response.put("customer", customer);
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

            } else {
                response.put("message", "El customer no existe en la base de datos");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            response.put("Error", e.getMessage().toString());
            response.put("Message", "Hubo un error al acceder a la base de datos: " + e.getLocalizedMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
