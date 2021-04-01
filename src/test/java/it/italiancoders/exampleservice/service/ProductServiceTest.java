package it.italiancoders.exampleservice.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import it.italiancoders.exampleservice.dao.ProductRepository;
import it.italiancoders.exampleservice.exception.ProductAlreadyExistException;
import it.italiancoders.exampleservice.model.Product;
import it.italiancoders.exampleservice.model.StockProduct;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.Mockito.doReturn;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource( locations = "classpath:test.properties")
public class ProductServiceTest {
    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    private WireMockServer wireMockServer;

    @BeforeEach
    void beforeEach() {
        // Start the WireMock Server
        wireMockServer = new WireMockServer(9999);
        wireMockServer.start();


    }

    @AfterEach
    void afterEach() {
        wireMockServer.stop();
    }

    @Test
    @DisplayName("find Product Id Found")
    void testFindProductByIdFound() {

        Product mockProduct = Product.builder()
                .id(1)
                .description("pippo")
                .build();
        doReturn(Optional.of(mockProduct)).when(productRepository).findById(1);

        Optional<Product> optionalProduct = productService.find(1);

        Assertions.assertTrue(optionalProduct.isPresent(), "Product was not found");
        Assertions.assertSame(optionalProduct.get(), mockProduct, "Product Should be the same");

    }

    @Test
    @DisplayName("Find Product Id not Found")
    void testFindProductByIdNotFound() {


        doReturn(Optional.empty()).when(productRepository).findById(1);

        Optional<Product> optionalProduct = productService.find(1);

        Assertions.assertTrue(!optionalProduct.isPresent(), "Product was not found");
    }

    @Test()
    @DisplayName("Insert Product Already exist")
    void testInsertProductAlreadyExist() {

        Product mockProduct = Product.builder()
                .id(1)
                .description("pippo")
                .build();

        doReturn(Optional.of(mockProduct)).when(productRepository).findByDescription("pippo");
        Assertions.assertThrows(ProductAlreadyExistException.class, () -> {
            productService.insertProduct(Product.builder().description("pippo").build());
        });

    }

    @Test()
    @DisplayName("Insert Product OK")
    void testInsertProduct() {

        Product request = Product.builder().description("pippo").build();
        Product mockProduct = Product.builder().id(1).description("pippo").build();

        doReturn(Optional.empty()).when(productRepository).findByDescription("pippo");
        doReturn(mockProduct).when(productRepository).save(request);
        Product newProduct = productService.insertProduct(request);

        Assertions.assertNotNull(newProduct, "Product should not be null");
        Assertions.assertEquals(newProduct, mockProduct, "Product Should be the same");
    }

    @Test()
    @DisplayName("Get Stock OK")
    void testGetStock() {
        wireMockServer.stubFor(get(urlEqualTo("/stock/1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBodyFile("json/stock-response.json")
                )
        );

        Product mockProduct = Product.builder().id(1).description("pippo").build();
        StockProduct stockProduct = productService.getStock(mockProduct);
        Assertions.assertNotNull(stockProduct, "Product should not be null");

    }

    @Test()
    @DisplayName("Get Stock internal error")
    void testGetStockInternalError() {
        wireMockServer.stubFor(get(urlEqualTo("/stock/2"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withBody("Internal Error Stock Service")
                )
        );

        Product mockProduct = Product.builder().id(2).description("pippo").build();
        StockProduct stockProduct = productService.getStock(mockProduct);
        Assertions.assertNotNull(stockProduct, "Product should not be null");

    }
}
