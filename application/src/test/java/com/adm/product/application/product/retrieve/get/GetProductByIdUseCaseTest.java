package com.adm.product.application.product.retrieve.get;

import com.adm.product.domain.exceptions.DomainException;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class GetProductByIdUseCaseTest {

    @InjectMocks
    private DefaultGetProductByIdUseCase useCase;

    @Mock
    private ProductGateway productGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(productGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetProduct_shouldReturnProduct() {
        final var expectedName = "Iphone 13";
        final var expectedBrand = "Apple";
        final var expectedDescription = "Um lancamento Apple 2025";
        final var expectedPrice = 10.000;

        final var aProduct = Product.newProduct(expectedName, expectedBrand, expectedDescription, expectedPrice);

        final var expectedId = aProduct.getId();

        when(productGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aProduct));

        final var actualProduct = useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId, actualProduct.id());
        Assertions.assertEquals(expectedName, actualProduct.name());
        Assertions.assertEquals(expectedBrand, actualProduct.brand());
        Assertions.assertEquals(expectedDescription, actualProduct.description());
        Assertions.assertEquals(expectedPrice, actualProduct.price());
    }

    @Test
    public void givenAInvalidId_whenCallsGetProduct_shouldReturnNotFound() {
        final var expectedErrorMessage = "Product with ID 123 not found";
        final var expectedId = ProductID.from("123");

        when(productGateway.findById(eq(expectedId)))
                .thenReturn(Optional.empty());

        final var actualException =
                Assertions.assertThrows(DomainException.class,
                        () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        final var expectedErrorMessage = "Gateway error";
        final var expectedId = ProductID.from("123");

        when(productGateway.findById(eq(expectedId)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException =
                Assertions.assertThrows(IllegalStateException.class,
                        () -> useCase.execute(expectedId.getValue())
                );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
