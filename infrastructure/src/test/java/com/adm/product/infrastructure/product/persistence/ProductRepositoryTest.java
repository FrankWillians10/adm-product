package com.adm.product.infrastructure.product.persistence;

import com.adm.product.domain.product.Product;
import com.adm.product.infrastructure.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    // Criar teste de brand, description e price null

    @Test
    public void givenAnInvalidNullName_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "name";
        final var expectedMessage = "not-null property references a null or transient value : com.adm.product.infrastructure.product.persistence.ProductJpaEntity.name";

        final var aProduct = Product.newProduct("Iphone 13", "Apple", "Um lancamento Apple 2025", 10.000);

        final var anEntity = ProductJpaEntity.from(aProduct);
        anEntity.setName(null);

        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> productRepository.save(anEntity));

        final var actualCause =
                Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

}
