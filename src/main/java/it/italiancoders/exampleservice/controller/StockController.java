package it.italiancoders.exampleservice.controller;

import it.italiancoders.exampleservice.model.StockProduct;
import it.italiancoders.exampleservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class StockController {

    private final ProductService productService;

    public StockController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/stock/{id}")
    ResponseEntity<?> getStock(@PathVariable Integer id) {

        StockProduct productStock =  this.productService.find(id)
                .map(productService::getStock)
                .orElse(null);

        return productStock == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(productStock);
    }
}
