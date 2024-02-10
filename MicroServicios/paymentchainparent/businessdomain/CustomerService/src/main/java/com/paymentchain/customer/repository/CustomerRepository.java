package com.paymentchain.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.paymentchain.customer.entities.Customer;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query("SELECT c from Customer c where c.code =?1")
    Customer findByCode(String code);

    @Query("SELECT c from Customer c where c.Iban =?1")
    Customer findByIban(String iban);

}
