package com.paymentchain.customer.controller;

import com.fasterxml.jackson.databind.JsonNode;
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
import com.paymentchain.customer.service.IService.ICustomerService;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;

@RestController
@RequestMapping("/api/")
public class CustomerRestController {

    //Variables estaticas
    private final static String URL_PRODUCT = "http://localhost:8083/api/product";

    //Inyectamos por Constructor
    private ICustomerService customerRepository;

    private final WebClient.Builder webClientBuilder;

    public CustomerRestController(ICustomerService customerRepository, WebClient.Builder webClientBuilder) {
        this.customerRepository = customerRepository;
        this.webClientBuilder = webClientBuilder;
    }

    //Creamos objeto de HttpClient ya que webClient requiere este objeto
    HttpClient client = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.SO_TIMEOUT, 300)
            .option(ChannelOption.SO_SNDBUF, 60)
            .responseTimeout(Duration.ofSeconds(1))
            .doOnConnected(connection -> {
                connection.addHandlerFirst(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                connection.addHandlerFirst(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });

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
    public ResponseEntity<?> post(@RequestBody Customer customer) {
        Map<String, Object> response = new HashMap<String, Object>();
        try {
            customer.getProduct().forEach(product -> product.setCustomer(customer));
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
    public Customer getFullDataByCode(@RequestParam(name = "code") String code) {
        Customer customer = customerRepository.findByCode(code);
        customer.getProduct().forEach(product -> {
            String nombreProducto = getProductNameById(product.getProductId());
            product.setProductName(nombreProducto);
        });
        return customer;
    }

    //Creamos método para obtener el nombre de los productos mediante el microservicio de Product
    public String getProductNameById(Integer id) {
        //Creamos el WebClient para realizar la peticion
        WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl(URL_PRODUCT)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", URL_PRODUCT))
                .build();
        //Obtenemos el dato 
        JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                .retrieve().bodyToMono(JsonNode.class).block();
        return block.get("product").get("name").asText();
    }

}
