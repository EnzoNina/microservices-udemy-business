package com.paymentchain.product.service.Iservice;

import com.paymentchain.product.entity.Product;
import java.util.List;

public interface IProductService {

    List<Product> findAll();
    
    Product findById(Integer id);
    
    Product save(Product product);
    
    Product edit(Product product, Integer id);
    
    void delete(Product product);
    
}
