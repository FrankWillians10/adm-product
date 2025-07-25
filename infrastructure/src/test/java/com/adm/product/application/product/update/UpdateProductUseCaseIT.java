package com.adm.product.application.product.update;

import com.adm.product.IntegrationTest;
import com.adm.product.domain.exceptions.DomainException;
import com.adm.product.domain.product.Product;
import com.adm.product.domain.product.ProductGateway;
import com.adm.product.infrastructure.product.persistence.ProductJpaEntity;
import com.adm.product.infrastructure.product.persistence.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@IntegrationTest
public class UpdateProductUseCaseIT {

    @Autowired
    public UpdateProductUseCase useCase;

    @Autowired
    public ProductRepository productRepository;

    @Mock
    public ProductGateway productGateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateProduct_shouldReturnProductId() {

        final var aProduct = Product.newProduct("Iphone 13", "Apple", "Um lancamento Apple 2025", 10.000);

        this.productRepository.saveAndFlush(ProductJpaEntity.from(aProduct));

        final var expectedName = "Iphone 13";
        final var expectedBrand = "Apple";
        final var expectedDescription = "Um lancamento Apple 2025";
        final var expectedPrice = 10.000;
        final var expectedId = aProduct.getId().getValue();

        final var aCommand = UpdateProductCommand.with(
                expectedId,
                expectedName,
                expectedBrand,
                expectedDescription,
                expectedPrice
        );

        Assertions.assertEquals(1, this.productRepository.count());

        final var actualOutPut = this.useCase.execute(aCommand).getRight();

        Assertions.assertNotNull(actualOutPut);
        Assertions.assertNotNull(actualOutPut.id());

        final var actualProduct = this.productRepository.findById(expectedId).get();


        Assertions.assertEquals(expectedId, actualProduct.getId());
        Assertions.assertEquals(expectedName, actualProduct.getName());
        Assertions.assertEquals(expectedBrand, actualProduct.getBrand());
        Assertions.assertEquals(expectedDescription, actualProduct.getDescription());
        Assertions.assertEquals(expectedPrice, actualProduct.getPrice());

    }

    @Test
    public void givenAInvalidName_whenCallsUpdateProduct_thenShouldReturnDomainException() {

        final var aProduct = Product.newProduct("Iphone 13", "Apple", "Um lancamento Apple 2025", 10.000);

        this.productRepository.saveAndFlush(ProductJpaEntity.from(aProduct));

        final String expectedName = null;
        final var expectedBrand = "Apple";
        final var expectedDescription = "Um lancamento Apple 2025";
        final var expectedPrice = 10.000;
        final var expectedId = aProduct.getId();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateProductCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedBrand,
                expectedDescription,
                expectedPrice
        );

        final var notification = this.useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(this.productGateway, Mockito.times(0)).update(any());

    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateProduct_shouldReturnNotFoundException() {

        final var expectedName = "Iphone 13";
        final var expectedBrand = "Apple";
        final var expectedDescription = "Um lancamento Apple 2025";
        final var expectedPrice = 10.000;
        final var expectedId = "123";
        final var expectedErrorMessage = "Product with ID 123 was not found";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateProductCommand.with(
                expectedId,
                expectedName,
                expectedBrand,
                expectedDescription,
                expectedPrice
        );

        final var actualException = Assertions.assertThrows(DomainException.class, () -> this.useCase.execute(aCommand).getLeft());

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }
}
