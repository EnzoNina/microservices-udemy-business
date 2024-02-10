package com.paymentchain.customer.service.Impl;

import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.repository.CustomerRepository;
import com.paymentchain.customer.service.IService.ICustomerService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ICustomerImpl implements ICustomerService {

    private final CustomerRepository customerRepository;

    public ICustomerImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findById(Integer id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    @Override
    public Customer findByCode(String code) {
        return customerRepository.findByCode(code);
    }

    @Override
    public Customer findByIban(String iban) {
        return customerRepository.findByIban(iban);
    }

}
