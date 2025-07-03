package com.adm.product.application.product.create;


import com.adm.product.domain.exceptions.DomainException;
import com.adm.product.domain.product.Product;
import com.adm.product.domain.product.ProductGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateProductUseCaseTest {

    @InjectMocks
    private DefaultCreateProductUseCase useCase;

    @Mock
    private ProductGateway productGateway;

    // Teste do caminho feliz
    // Teste passando uma propriedade inválida
    // Teste simulando um erro genérico vindo do gateway

    @Test
    public void givenAValidCommand_whenCallsCreateProduct_shouldReturnProductId() {

        final var expectedName = "Iphone 13";
        final var expectedBrand = "Apple";
        final var expectedDescription = "Um lancamento Apple 2025";
        final var expectedPrice = 10.000;

        final var aCommand = CreateProductCommand.with(expectedName, expectedBrand, expectedDescription, expectedPrice);

        when(productGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).getRight();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(productGateway, times(1))
                .create(Mockito.argThat(aProduct -> {

                    return Objects.equals(expectedName, aProduct.getName())
                            && Objects.equals(expectedBrand, aProduct.getBrand())
                            && Objects.equals(expectedDescription, aProduct.getDescription())
                            && Objects.equals(expectedPrice, aProduct.getPrice());
                }
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateProduct_thenShouldReturnDomainException() {
        final String expectedName = null;
        final var expectedBrand = "Apple";
        final var expectedDescription = "Um lancamento Apple 2025";
        final var expectedPrice = 10.000;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand =
                CreateProductCommand.with(expectedName, expectedBrand, expectedDescription, expectedPrice);

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(productGateway, times(0)).create(any());
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnException() {

        final var expectedName = "Iphone 13";
        final var expectedBrand = "Apple";
        final var expectedDescription = "Um lancamento Apple 2025";
        final var expectedPrice = 10.000;
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        final var aCommand =
                CreateProductCommand.with(expectedName, expectedBrand, expectedDescription, expectedPrice);

        when(productGateway.create(any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(productGateway, times(1))
                .create(Mockito.argThat(aProduct -> {

                            return Objects.equals(expectedName, aProduct.getName())
                                    && Objects.equals(expectedBrand, aProduct.getBrand())
                                    && Objects.equals(expectedDescription, aProduct.getDescription())
                                    && Objects.equals(expectedPrice, aProduct.getPrice());
                        }
                ));
    }
}
