package com.paymentchain.product.service.impl;

import com.paymentchain.product.entity.Product;
import com.paymentchain.product.repository.ProductRepository;
import com.paymentchain.product.service.Iservice.IProductService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IProductImpl implements IProductService {
    
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product edit(Product product, Integer id) {
        
        Product originalProduct = productRepository.findById(id).orElse(null);
        
        originalProduct.setCode(product.getCode());
        originalProduct.setName(product.getName());
        
        
       return productRepository.save(product);
    }

    @Override
    public void delete(Product product) {
        productRepository.delete(product);
    }
    
}
