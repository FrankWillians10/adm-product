package com.adm.product.application.product.create;


import com.adm.product.domain.product.Product;
import com.adm.product.domain.product.ProductGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;


public class CreateProductUseCaseTest {

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

        final ProductGateway productGateway = Mockito.mock(ProductGateway.class);
        Mockito.when(productGateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var useCase = new DefaultCreateProductUseCase(productGateway);

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(productGateway, Mockito.times(1))
                .create(Mockito.argThat(aProduct -> {

                    return Objects.equals(expectedName, aProduct.getName())
                            && Objects.equals(expectedBrand, aProduct.getBrand())
                            && Objects.equals(expectedDescription, aProduct.getDescription())
                            && Objects.equals(expectedPrice, aProduct.getPrice());
                }
        ));
    }
}
