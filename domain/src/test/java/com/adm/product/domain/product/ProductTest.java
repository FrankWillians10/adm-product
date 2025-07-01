package com.adm.product.domain.product;

import com.adm.product.domain.exceptions.DomainException;
import com.adm.product.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    public void givenAValidParams_whenCallNewProduct_thenInstantiateAProduct() {
        final var expectedName = "A3";
        final var expectedBrand = "Samsung";
        final var expectedDescription = "Um lancamento Samsung 2025";
        final var expectedPrice = 2000;

        final var actualProduct = Product.newProduct(expectedName, expectedBrand, expectedDescription, expectedPrice);

        Assertions.assertNotNull(actualProduct);
        Assertions.assertNotNull(actualProduct.getId());
        Assertions.assertEquals(expectedName, actualProduct.getName());
        Assertions.assertEquals(expectedBrand, actualProduct.getBrand());
        Assertions.assertEquals(expectedDescription, actualProduct.getDescription());
        Assertions.assertEquals(expectedPrice, actualProduct.getPrice());

    }

    @Test
    public void givenAnInvalidNullName_whenCallNewProductAndValidate_thenShouldReciveError() {
        final String expectedName = null;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var expectedBrand = "Samsung";
        final var expectedDescription = "Um lancamento Samsung 2025";
        final var expectedPrice = 2000;

        final var actualProduct = Product.newProduct(expectedName, expectedBrand, expectedDescription, expectedPrice);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualProduct.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

    }

    @Test
    public void givenAnInvalidEmptyName_whenCallNewProductAndValidate_thenShouldReciveError() {
        final var expectedName = " ";
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;
        final var expectedBrand = "Samsung";
        final var expectedDescription = "Um lancamento Samsung 2025";
        final var expectedPrice = 2000;

        final var actualProduct = Product.newProduct(expectedName, expectedBrand, expectedDescription, expectedPrice);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualProduct.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

    }

    @Test
    public void givenAnInvalidNameLengthLessThan3_whenCallNewProductAndValidate_thenShouldReciveError() {
        final var expectedName = "fi ";
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;
        final var expectedBrand = "Samsung";
        final var expectedDescription = "Um lancamento Samsung 2025";
        final var expectedPrice = 2000;

        final var actualProduct = Product.newProduct(expectedName, expectedBrand, expectedDescription, expectedPrice);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualProduct.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

    }

    @Test
    public void givenAnInvalidNameLengthMoreThan255_whenCallNewProductAndValidate_thenShouldReciveError() {
        final var expectedName = """
                                    Nunca é demais lembrar o peso e o significado destes problemas, uma vez
                                    que a revolução dos costumes causa impacto indireto na reavaliação das
                                    condições inegavelmente apropriadas.
                                    Nunca é demais lembrar o peso e o significado destes problemas, uma vez
                                    que a revolução dos costumes causa impacto indireto na reavaliação das
                                    """;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;
        final var expectedBrand = "Samsung";
        final var expectedDescription = "Um lancamento Samsung 2025";
        final var expectedPrice = 2000;

        final var actualProduct = Product.newProduct(expectedName, expectedBrand, expectedDescription, expectedPrice);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualProduct.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

    }
}
