package com.adm.product.application.product.update;

import com.adm.product.application.product.create.CreateProductCommand;
import com.adm.product.domain.exceptions.DomainException;
import com.adm.product.domain.product.Product;
import com.adm.product.domain.product.ProductGateway;
import com.adm.product.domain.product.ProductID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    @Test
    public void givenAInvalidName_whenCallsUpdateProduct_thenShouldReturnDomainException() {

        final var aProduct = Product.newProduct("Iphon", "Appl", null, 10.000);

        final String expectedName = null;
        final var expectedBrand = "Apple";
        final var expectedDescription = "Um lancamento Apple 2025";
        final var expectedPrice = 10.000;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var expectedId = aProduct.getId();

        final var aCommand =
                UpdateProductCommand.with(expectedId.getValue(), expectedName, expectedBrand, expectedDescription, expectedPrice);

        when(productGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aProduct));

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        verify(productGateway, times(0)).update(any());
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_ShouldReturnAException() {
        final var aProduct = Product.newProduct("Iphon", "Appl", null, 10.000);

        final var expectedName = "Iphone 13";
        final var expectedBrand = "Apple";
        final var expectedDescription = "Um lancamento Apple 2025";
        final var expectedPrice = 10.000;
        final var expectedId = aProduct.getId();
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        final var aCommand =
                UpdateProductCommand.with(expectedId.getValue(), expectedName, expectedBrand, expectedDescription, expectedPrice);

        when(productGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aProduct));

        when(productGateway.update(any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        verify(productGateway, times(1)).update(argThat(
                aUpdateProduct ->
                        Objects.equals(expectedName, aUpdateProduct.getName())
                                && Objects.equals(expectedId, aUpdateProduct.getId())
                                && Objects.equals(expectedBrand, aUpdateProduct.getBrand())
                                && Objects.equals(expectedDescription, aUpdateProduct.getDescription())
                                && Objects.equals(expectedPrice, aUpdateProduct.getPrice())
        ));

    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateProduct_ShouldReturnNotFoundException() {
        final var expectedName = "Iphone 13";
        final var expectedBrand = "Apple";
        final var expectedDescription = "Um lancamento Apple 2025";
        final var expectedPrice = 10.000;
        final var expectedId = "123";

        final var expectedErrorMessage = "Product with ID 123 was not found";
        final var expectedErrorCount = 1;


        final var aCommand =
                UpdateProductCommand.with(expectedId, expectedName, expectedBrand, expectedDescription, expectedPrice);

        when(productGateway.findById(eq(ProductID.from(expectedId))))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(DomainException.class, () -> useCase.execute(aCommand).getLeft());


        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(productGateway, times(1)).findById(eq(ProductID.from(expectedId)));

        verify(productGateway, times(0)).update(any());
    }
}
