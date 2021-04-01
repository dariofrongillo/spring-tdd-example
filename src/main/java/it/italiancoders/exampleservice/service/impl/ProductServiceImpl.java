package it.italiancoders.exampleservice.service.impl;

import it.italiancoders.exampleservice.dao.ProductRepository;
import it.italiancoders.exampleservice.exception.ProductAlreadyExistException;
import it.italiancoders.exampleservice.model.Product;
import it.italiancoders.exampleservice.model.Stock;
import it.italiancoders.exampleservice.model.StockProduct;
import it.italiancoders.exampleservice.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${stock.service.url}")
    private String stockServiceUrl;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository  = productRepository;
    }

    private static void throwAlreadyExistProduct(Product pr) {
        throw new ProductAlreadyExistException();
    }

    @Override
    public Optional<Product> find(Integer id) {
        return this.productRepository.findById(id);
    }

    @Override
    public Product insertProduct(Product p) throws ProductAlreadyExistException {
        this.productRepository.findByDescription(p.getDescription())
                .ifPresent(ProductServiceImpl::throwAlreadyExistProduct);
        return this.productRepository.save(p);
    }

    @Override
    public StockProduct getStock(Product p) {
        Stock stock = null;
        try {
            stock = restTemplate.getForObject(stockServiceUrl + "/" + p.getId(), Stock.class);
        } catch (Exception exc) {
            return new StockProduct(p, null);
        }
        return new StockProduct(p, stock);
    }
}
