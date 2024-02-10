package com.paymentchain.customer.service.IService;

import com.paymentchain.customer.entities.Customer;
import java.util.List;

public interface ICustomerService {

    List<Customer> findAll();

    Customer findById(Integer id);

    Customer findByCode(String code);

    Customer findByIban(String iban);

    Customer save(Customer customer);

    void delete(Customer customer);

}
