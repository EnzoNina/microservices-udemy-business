package com.paymentchain.customer.controller;

import com.paymentchain.customer.business.transaction.BusinessTransaction;
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
import com.paymentchain.customer.exception.BusinessRuleException;
import com.paymentchain.customer.service.IService.ICustomerService;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/")
public class CustomerRestController {

          //Inyectamos por Constructor
          @Autowired
          private ICustomerService customerRepository;

          @Autowired
          private BusinessTransaction bt;

          @GetMapping("customer")
          public List<Customer> list() {
                    return customerRepository.findAll();
          }

          @GetMapping("customer/{id}")
          public ResponseEntity<?> get(@PathVariable(name = "id") int id) {

                    Map<String, Object> response = new HashMap<String, Object>();
                    Customer customer = customerRepository.findById(id);
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
          public ResponseEntity<?> put(@PathVariable(name = "id") int id, @RequestBody Customer customer) {
                    //Capturamos el antiguo Customer
                    Customer oldCustomer = customerRepository.findById(id);
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
          public ResponseEntity<?> post(@RequestBody Customer customer) throws BusinessRuleException, UnknownHostException {
                    Customer saveCustomer = bt.post(customer);
                    return new ResponseEntity<>(saveCustomer, HttpStatus.CREATED);
          }

          @DeleteMapping("customer/{id}")
          public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
                    Map<String, Object> response = new HashMap<String, Object>();

                    Customer customer = customerRepository.findById(id);
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

          @GetMapping("customer/full")
          public ResponseEntity<?> getFullDataByCode(@RequestParam(name = "code") String code) throws UnknownHostException {
                    Customer customer = bt.getFullDataByCode(code);

                    return new ResponseEntity<>(customer, HttpStatus.FOUND);
          }
}
