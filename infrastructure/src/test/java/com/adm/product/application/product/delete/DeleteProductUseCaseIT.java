package com.adm.product.application.product.delete;

import com.adm.product.IntegrationTest;
import com.adm.product.domain.product.Product;
import com.adm.product.domain.product.ProductGateway;
import com.adm.product.domain.product.ProductID;
import com.adm.product.infrastructure.product.persistence.ProductJpaEntity;
import com.adm.product.infrastructure.product.persistence.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

@IntegrationTest
public class DeleteProductUseCaseIT {

    @Autowired
    private DeleteProductUseCase useCase;

    @Autowired
    private ProductRepository productRepository;

    @Mock
    private ProductGateway productGateway;

    @Test
    public void givenAValidId_whenCallsDeleteProduct_shouldBeOk() {
        final var aProduct = Product.newProduct("Iphone 13", "Apple", "Um lancamento apple 2025", 10.000);
        final var expectedId = aProduct.getId();

        productRepository.saveAndFlush(ProductJpaEntity.from(aProduct));

        Assertions.assertEquals(1, productRepository.count());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(0, productRepository.count());
    }

    @Test
    public void givenAInvalidId_whenCallsDeleteProduct_shouldBeOk() {
        final var expectedId = ProductID.from("123");

        Assertions.assertEquals(0, productRepository.count());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(0, productRepository.count());
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        final var aProduct = Product.newProduct("Iphone 13", "Apple", "Um lancamento Apple 2025", 10.000);
        final var expectedId = aProduct.getId();

        doThrow(new IllegalStateException("Gateway error"))
                .when(productGateway).deleteById(eq(expectedId));

        Assertions.assertThrows(IllegalStateException.class, () ->useCase.execute(expectedId.getValue()));

        Mockito.verify(productGateway, times(1)).deleteById(eq(expectedId));
    }


}
