package com.paymentchain.customer.business.transaction;

import com.fasterxml.jackson.databind.JsonNode;
import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.entities.CustomerProduct;
import com.paymentchain.customer.exception.BusinessRuleException;
import com.paymentchain.customer.service.IService.ICustomerService;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;

@Service
public class BusinessTransaction {

          //Variables estaticas
          private final static String URL_PRODUCT = "http://businessdomain-product/api/product";
          private final static String URL_TRANSACTION = "http://businessdomain-transaction/api/transaction";

          //Inyectamos por Constructor
          @Autowired
          private ICustomerService customerRepository;

          @Autowired
          private WebClient.Builder webClientBuilder;

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

          public List<Customer> list() {
                    return customerRepository.findAll();
          }

          public Customer get(int id) {
                    Customer customer = customerRepository.findById(id);
                    return customer;
          }

          public Customer post(Customer customer) throws BusinessRuleException, UnknownHostException {
                    for (CustomerProduct product : customer.getProduct()) {
                              String nameProduct = getProductNameById(product.getProductId());
                              if (nameProduct.isBlank()) {
                                        // Lanzar la excepción directamente
                                        throw new BusinessRuleException("232", HttpStatus.PRECONDITION_FAILED,
                                                "El producto no existe");
                              } else {
                                        product.setCustomer(customer);
                              }
                    }

                    // Si no se lanzó ninguna excepción, guardar el cliente y retornarlo
                    Customer customerCreated = customerRepository.save(customer);
                    return customerCreated;
          }

          public Customer getFullDataByCode(String code) throws UnknownHostException {
                    //Obtenemos customer 
                    Customer customer = customerRepository.findByCode(code);
                    //Establecemos los nombres de los productos obtenidos por el servicio Product
                    for (CustomerProduct product : customer.getProduct()) {
                              String nombreProducto = getProductNameById(product.getProductId());
                              product.setProductName(nombreProducto);
                    }

                    //Obtenemos las transacciones del servicio Transaction
                    List<?> lstTransactions = getTransactionsByAcoountIban(customer.getIban());
                    customer.setTransactions(lstTransactions);

                    return customer;
          }

          //Creamos método para obtener el nombre de los productos mediante el microservicio de Product
          public String getProductNameById(Integer id) throws UnknownHostException {
                    String name = null;
                    try {
                              //Creamos el WebClient para realizar la peticion
                              WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                                      .baseUrl(URL_PRODUCT)
                                      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                      .defaultUriVariables(Collections.singletonMap("url", URL_PRODUCT))
                                      .build();
                              //Obtenemos el dato 
                              JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                                      .retrieve().bodyToMono(JsonNode.class
                                      ).block();
                              name = block.get("product").get("name").asText();
                    } catch (WebClientResponseException e) {
                              HttpStatusCode statusCode = e.getStatusCode();
                              if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                                        return "";
                              } else {
                                        throw new UnknownHostException(e.getMessage());
                              }
                    }
                    return name;
          }

          public List<?> getTransactionsByAcoountIban(String ibanAcount) {
                    //Creamos el WebClient para realizar la peticion
                    WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                            .baseUrl(URL_TRANSACTION)
                            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .defaultUriVariables(Collections.singletonMap("url", URL_TRANSACTION))
                            .build();

                    List<?> transactions = build.method(HttpMethod.GET).uri(uri -> uri
                            .path("/customer/transactions")
                            .queryParam("iban", ibanAcount)
                            .build())
                            .retrieve().bodyToFlux(Object.class
                            ).collectList().block();

                    return transactions;
          }

}
