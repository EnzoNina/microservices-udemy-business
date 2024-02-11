package com.paymentchain.customer.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Entity
@Data
@Table(name = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_customer")
    private Integer id;

    @Column(name = "code_customer")
    private String code;

    @Column(name = "nombre_customer")
    private String nombre;

    @Column(name = "surnames_customer")
    private String surnames;

    @Column(name = "phone_customer")
    private String phone;

    @Column(name = "address_customer")
    private String address;

    @Column(name = "iban_customer")
    private String Iban;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerProduct> product;

    //Transient indica que este dato no se guardara en la base de datos
    @Transient
    private List<?> transactions;

}
