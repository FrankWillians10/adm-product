package com.adm.product.application.product.create;

import com.adm.product.IntegrationTest;
import com.adm.product.infrastructure.product.persistence.ProductRepository;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class CreateProductUseCaseIT {

    @Autowired
    private CreateProductUseCase useCase;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void givenAValidCommand_whenCallsCreateProduct_shouldReturnProductId() {
        final var expectedName = "Iphone 13";
        final var expectedBrand = "Apple";
        final var expectedDescription = "Um lancamento Apple 2025";
        final var expectedPrice = 10.000;

        Assertions.assertEquals(0, productRepository.count());

        final var aCommand =
                CreateProductCommand.with(expectedName, expectedBrand, expectedDescription, expectedPrice);

        final var actualOutPut = useCase.execute(aCommand).getRight();

        Assertions.assertNotNull(actualOutPut);
        Assertions.assertNotNull(actualOutPut.id());

        Assertions.assertEquals(1, productRepository.count());

        final var actualProduct = productRepository.findById(actualOutPut.id().getValue()).get();

        Assertions.assertEquals(expectedName, actualProduct.getName());
        Assertions.assertEquals(expectedBrand, actualProduct.getBrand());
        Assertions.assertEquals(expectedDescription, actualProduct.getDescription());
        Assertions.assertEquals(expectedPrice, actualProduct.getPrice());

    }

    @Test
    public void givenAInvalidCommand_whenCallsCreateProduct_shouldReturnDomainException() {
        final String expectedName = null;
        final var expectedBrand = "Apple";
        final var expectedDescription = "Um lancamento Apple 2025";
        final var expectedPrice = 10.000;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        Assertions.assertEquals(0, productRepository.count());

        final var aCommand =
                CreateProductCommand.with(expectedName, expectedBrand, expectedDescription, expectedPrice);

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Assertions.assertEquals(0, productRepository.count());

    }

}
