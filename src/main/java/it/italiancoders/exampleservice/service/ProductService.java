package it.italiancoders.exampleservice.service;


import it.italiancoders.exampleservice.exception.ProductAlreadyExistException;
import it.italiancoders.exampleservice.model.Product;
import it.italiancoders.exampleservice.model.StockProduct;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

public interface ProductService {
    Optional<Product> find(Integer id);
    Product insertProduct(Product p) throws ProductAlreadyExistException;
    StockProduct getStock(Product p);
}
