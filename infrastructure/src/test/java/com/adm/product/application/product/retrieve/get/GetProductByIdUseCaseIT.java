package com.adm.product.application.product.retrieve.get;

import com.adm.product.IntegrationTest;
import com.adm.product.domain.product.Product;
import com.adm.product.domain.product.ProductGateway;
import com.adm.product.domain.product.ProductID;
import com.adm.product.infrastructure.product.persistence.ProductJpaEntity;
import com.adm.product.infrastructure.product.persistence.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

@IntegrationTest
public class GetProductByIdUseCaseIT {

    @Autowired
    public GetProductByIdUseCase useCase;

    @Autowired
    public ProductRepository productRepository;

    @Mock
    public ProductGateway productGateway;

    @Test
    public void givenAValidId_whenCallsGetProduct_shouldReturnProduct() {

        final var expectedName = "Iphone 13";
        final var expectedBrand = "Apple";
        final var expectedDescription = "Um lancamento Apple 2025";
        final var expectedPrice = 10.000;

        final var aProduct = Product.newProduct(expectedName, expectedBrand, expectedDescription, expectedPrice);

        final var expectedId = aProduct.getId().getValue();

        this.productRepository.saveAndFlush(ProductJpaEntity.from(aProduct));

        final var actualProduct = this.useCase.execute(expectedId);
        System.out.println(expectedId);
        System.out.println(actualProduct.isLeft());
        final var actualOutput = actualProduct.getRight();

        Assertions.assertEquals(expectedId, actualOutput.id().getValue());
        Assertions.assertEquals(expectedName, actualOutput.name());
        Assertions.assertEquals(expectedBrand, actualOutput.brand());
        Assertions.assertEquals(expectedDescription, actualOutput.description());
        Assertions.assertEquals(expectedPrice, actualOutput.price());

    }

    @Test
    public void givenAValidId_whenCallsGetProduct_shouldReturnNotFound() {

        final var expectedErrorMessage = "Product with ID 123 not found";
        final var expectedId = ProductID.from("123");

        final var actualException = this.useCase.execute(expectedId.getValue()).getLeft();

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

    }

}
