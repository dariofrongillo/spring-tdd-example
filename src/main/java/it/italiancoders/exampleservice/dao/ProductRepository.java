package it.italiancoders.exampleservice.dao;

import it.italiancoders.exampleservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository  extends JpaRepository<Product, Integer> {
    Optional<Product> findByDescription(String description);
}
