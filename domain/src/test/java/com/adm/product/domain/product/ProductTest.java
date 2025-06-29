package com.adm.product.domain.product;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    public void givenAValidParams_whenCallNewProduct_thenInstantiateAProduct() {
        final var expectedName = "A3";
        final var expectedBrand = "Samsung";
        final var expectedDescription = "Um lancamento Samsung 2025";
        final var expectedPrice = 2000;

        final var actualProduct = Product.newProduct(expectedName, expectedBrand, expectedDescription, expectedPrice);

        Assertions.assertNotNull(actualProduct);
        Assertions.assertNotNull(actualProduct.getId());
        Assertions.assertEquals(expectedName, actualProduct.getName());
        Assertions.assertEquals(expectedBrand, actualProduct.getBrand());
        Assertions.assertEquals(expectedDescription, actualProduct.getDescription());
        Assertions.assertEquals(expectedPrice, actualProduct.getPrice());

    }
}
