package com.paymentchain.product.business.transaction;

import com.paymentchain.product.entity.Product;
import com.paymentchain.product.exception.BusinessRuleException;
import com.paymentchain.product.service.Iservice.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProductTransaction {

          @Autowired
          private IProductService productService;

          public Product saveProduct(Product product) throws BusinessRuleException {
                    Product customerCreated = null;
                    BusinessRuleException exception = new BusinessRuleException();
                    if (product.getName() == null || product.getName().isBlank()) {
                              exception = new BusinessRuleException("P-E002", HttpStatus.NO_CONTENT,
                                      "El nombre del producto no se puede ser vacio o nulo");
                              throw exception;
                    }
                    if (product.getCode() == null || product.getCode().isBlank()) {
                              exception = new BusinessRuleException("P-E003", HttpStatus.NO_CONTENT,
                                      "El còdigo del producto no se puede ser vacio o nulo");
                              throw exception;
                    } else {
                              customerCreated = productService.save(product);
                    }
                    return customerCreated;
          }

}
