package com.adm.product.application.product;

import com.adm.product.IntegrationTest;
import com.adm.product.application.product.create.CreateProductUseCase;
import com.adm.product.infrastructure.product.persistence.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class SampleIT {

    @Autowired
    private CreateProductUseCase useCase;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testInject() {
        Assertions.assertNotNull(useCase);
        Assertions.assertNotNull(productRepository);
    }

}
