package com.paymentchain.product.controller;

import com.paymentchain.product.entity.Product;
import com.paymentchain.product.service.Iservice.IProductService;
import java.util.HashMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/")
public class ProductRestController {

    //Inyectamos por constructor
    private IProductService productService;

    public ProductRestController(IProductService productService) {
        this.productService = productService;
    }

    @Value("${user.alias}")
    private String alias;

    @GetMapping("product")
    public List<Product> listAll() {
        System.out.println("user " + alias);
        return productService.findAll();
    }

    @GetMapping("product/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Integer id) {

        Map<String, Object> response = new HashMap<String, Object>();
        Product product = productService.findById(id);
        try {
            if (product != null) {
                response.put("message", "El Product fue encontrado con éxito");
                response.put("product", product);
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

            } else {
                response.put("message", "El Product no existe en la base de datos");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            response.put("Error", e.getMessage().toString());
            response.put("Message", "Hubo un error al acceder a la base de datos: " + e.getLocalizedMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("product/{id}")
    public ResponseEntity<?> editProduct(@PathVariable(name = "id") Integer id, @RequestBody Product product) {
        //Capturamos el antiguo Product
        Product oldProduct = productService.findById(id);
        oldProduct.setCode(product.getCode());
        oldProduct.setName(product.getName());
        Map<String, Object> response = new HashMap<String, Object>();
        try {
            if (oldProduct != null) {
                Product newProduct = productService.save(oldProduct);
                response.put("message", "El Product fue actualizado con éxito!");
                response.put("product", newProduct);
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

            } else {
                response.put("message", "El Product no existe en la base de datos");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            response.put("Error", e.getMessage().toString());
            response.put("Message", "Hubo un error al acceder a la base de datos: " + e.getLocalizedMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("product")
    public ResponseEntity<?> saveProduct(@RequestBody Product product) {
        Map<String, Object> response = new HashMap<String, Object>();
        try {
            Product customerCreated = productService.save(product);
            response.put("Message", "El Product fue creado con éxito!");
            response.put("Product", customerCreated);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            response.put("Error", e.getMessage().toString());
            response.put("Message", "Hubo un error al acceder a la base de datos: " + e.getLocalizedMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable(name = "id") Integer id) {
        Map<String, Object> response = new HashMap<String, Object>();

        Product product = productService.findById(id);
        try {
            if (product != null) {
                productService.delete(product);
                response.put("message", "El Product fue borrado con éxito!");
                response.put("product", product);
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

            } else {
                response.put("message", "El Product no existe en la base de datos");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            response.put("Error", e.getMessage().toString());
            response.put("Message", "Hubo un error al acceder a la base de datos: " + e.getLocalizedMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
