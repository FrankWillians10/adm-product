package com.adm.product.application.product.delete;

import com.adm.product.domain.product.Product;
import com.adm.product.domain.product.ProductGateway;
import com.adm.product.domain.product.ProductID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteProductUseCaseTest {

    @InjectMocks
    private DefaultDeleteProductUseCase useCase;

    @Mock
    private ProductGateway productGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(productGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteProduct_shouldBeOk() {
        final var aProduct = Product.newProduct("Iphone 13", "Apple", "Um lancamento Apple 2025", 10.000);
        final var expectedId = aProduct.getId();

        doNothing()
                .when(productGateway).deleteById(eq(expectedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(productGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenAInvalidId_whenCallsDeleteProduct_shouldBeOk() {
        final var expectedId = ProductID.from("123");

        doNothing()
                .when(productGateway).deleteById(eq(expectedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(productGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        final var aProduct = Product.newProduct("Iphone 13", "Apple", "Um lancamento Apple 2025", 10.000);
        final var expectedId = aProduct.getId();

        doThrow(new IllegalStateException("Gateway error"))
                .when(productGateway).deleteById(eq(expectedId));

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        Mockito.verify(productGateway, times(1)).deleteById(eq(expectedId));


    }

}
