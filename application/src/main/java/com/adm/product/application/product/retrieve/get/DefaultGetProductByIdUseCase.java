package com.adm.product.application.product.retrieve.get;

import com.adm.product.domain.exceptions.DomainException;
import com.adm.product.domain.product.Product;
import com.adm.product.domain.product.ProductGateway;
import com.adm.product.domain.product.ProductID;
import com.adm.product.domain.validation.Error;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class DefaultGetProductByIdUseCase extends GetProductByIdUseCase{

    private final ProductGateway productGateway;

    public DefaultGetProductByIdUseCase(final ProductGateway productGateway) {
        this.productGateway = Objects.requireNonNull(productGateway);
    }

    @Override
    public ProductOutPut execute(String anId) {
        final var anProductID = ProductID.from(anId);

        return this.productGateway.findById(anProductID)
                .map(ProductOutPut::from)
                .orElseThrow(notFound(anProductID));
    }

    private Supplier<DomainException> notFound(final ProductID anId) {
        return () -> DomainException.with(
                new Error("Product with ID %s not found".formatted(anId.getValue()))
                 );
    }
}
