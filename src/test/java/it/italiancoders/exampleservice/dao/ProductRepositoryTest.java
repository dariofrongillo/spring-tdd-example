package it.italiancoders.exampleservice.dao;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import it.italiancoders.exampleservice.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@ExtendWith({DBUnitExtension.class, SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProductRepositoryTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private ProductRepository repository;

    public ConnectionHolder getConnectionHolder() {
        // Return a function that retrieves a connection from our data source
        return () -> dataSource.getConnection();
    }

    @Test
    @DataSet("products.yml")
    void testFindAll() {
        List<Product> products = repository.findAll();
        Assertions.assertEquals(2, products.size(), "We should have 2 products in our database");
    }


    @Test
    @DisplayName("find by Id OK")
    @DataSet("products.yml")
    void testFindById() {
        Optional<Product> product = repository.findById(1);
        Assertions.assertTrue(product.isPresent(), "Product should be found");
        Assertions.assertEquals(product.get().getId(), 1,  "Product Id should be 1");
    }

    @Test
    @DisplayName("delete product ok")
    @DataSet("products.yml")
    void testDeleteId() {
        repository.deleteById(1);
        Optional<Product> product = repository.findById(1);
        Assertions.assertFalse(product.isPresent(), "Product should be not found due deletion");
    }

    @Test
    @DisplayName("insert product ok")
    @DataSet("products.yml")
    void testInsertProduct() {
        Product newProduct = repository.save(Product.builder().description("pluto").build());
        Assertions.assertNotNull(newProduct.getId(), "Product Id should be not null");
        Optional<Product> product = repository.findById(newProduct.getId());
        Assertions.assertTrue(product.isPresent(), "Product should be not found due deletion");
    }
}
