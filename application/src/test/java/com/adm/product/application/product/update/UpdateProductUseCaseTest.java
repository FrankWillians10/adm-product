package com.adm.product.application.product.update;

import com.adm.product.domain.product.Product;
import com.adm.product.domain.product.ProductGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateProductUseCaseTest {

    @InjectMocks
    private DefaultUpdateProductUseCase useCase;

    @Mock
    private ProductGateway productGateway;

    // Teste do caminho feliz
    // Teste passando uma propriedade inválida
    // Teste simulando um erro genérico vindo do gateway

    @Test
    public void givenAValidCommand_whenCallsUpdateProduct_shouldReturnProductId(){
        final var aProduct = Product.newProduct("Iphon", "Appl", null, 10.000);
        final var expectedName = "Iphone 13";
        final var expectedBrand = "Apple";
        final var expectedDescription = "Um lancamento Apple 2025";
        final var expectedPrice = 10.000;

        final var expectedId = aProduct.getId();

        final var aCommand = UpdateProductCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedBrand,
                expectedDescription,
                expectedPrice
        );

        when(productGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aProduct));

        when(productGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).getRight();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(productGateway, times(1)).findById(eq(expectedId));
        verify(productGateway, times(1)).update(argThat(
                aUpdateProduct ->
                    Objects.equals(expectedName, aUpdateProduct.getName())
                            && Objects.equals(expectedId, aUpdateProduct.getId())
                            && Objects.equals(expectedBrand, aUpdateProduct.getBrand())
                            && Objects.equals(expectedDescription, aUpdateProduct.getDescription())
                            && Objects.equals(expectedPrice, aUpdateProduct.getPrice())
        ));
    }
}
