package com.adm.product.infrastructure.product;

import com.adm.product.domain.product.Product;
import com.adm.product.infrastructure.MySQLGatewayTest;
import com.adm.product.infrastructure.product.persistence.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class ProductMySQLGatewayTest {

    @Autowired
    public ProductMySQLGateway productGateway;

    @Autowired
    public ProductRepository productRepository;

    @Test
    public void testInjectedDependencies() {

        Assertions.assertNotNull(productGateway);
        Assertions.assertNotNull(productRepository);

    }

    @Test
    public void givenAValidProduct_whenCallsCreate_shouldReturnANewProduct() {

        final var expectedName = "Iphone 13";
        final var expectedBrand = "Apple";
        final var expectedDescription = "Um lancamento Apple 2025";
        final var expectedPrice = 10.000;

        final var aProduct =
                Product.newProduct(expectedName, expectedBrand, expectedDescription, expectedPrice);

        Assertions.assertEquals(0, productRepository.count());

        final var actualProduct = productGateway.create(aProduct);

        Assertions.assertEquals(1, productRepository.count());

        Assertions.assertEquals(aProduct.getId().getValue(), actualProduct.getId().getValue());
        Assertions.assertEquals(expectedName, actualProduct.getName());
        Assertions.assertEquals(expectedBrand, actualProduct.getBrand());
        Assertions.assertEquals(expectedDescription, actualProduct.getDescription());
        Assertions.assertEquals(expectedPrice, actualProduct.getPrice());

        final var actualEntity = productRepository.findById(aProduct.getId().getValue()).get();

        Assertions.assertEquals(aProduct.getId().getValue(), actualProduct.getId().getValue());
        Assertions.assertEquals(expectedName, actualProduct.getName());
        Assertions.assertEquals(expectedBrand, actualProduct.getBrand());
        Assertions.assertEquals(expectedDescription, actualProduct.getDescription());
        Assertions.assertEquals(expectedPrice, actualProduct.getPrice());

    }
}


