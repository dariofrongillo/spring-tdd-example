package it.italiancoders.exampleservice.controller;

import it.italiancoders.exampleservice.exception.ProductAlreadyExistException;
import it.italiancoders.exampleservice.model.Product;
import it.italiancoders.exampleservice.model.RestError;
import it.italiancoders.exampleservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Integer id) {
        return productService.find(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/products/{id}")
    public ResponseEntity<?> postProduct(@RequestBody Product product) {

        try {
            return ResponseEntity.ok(productService.insertProduct(product));
        } catch (ProductAlreadyExistException e) {
            return ResponseEntity.badRequest()
                    .body(RestError.builder()
                            .message("Product " + product.getDescription() + " already exist")
                            .build()
                    );
        }
    }
}
