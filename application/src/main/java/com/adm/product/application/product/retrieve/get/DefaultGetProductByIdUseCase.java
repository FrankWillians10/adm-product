package com.adm.product.application.product.retrieve.get;

import com.adm.product.application.Either;
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
    public Either<DomainException, ProductOutPut> execute(String anId) {
        final var anProductID = ProductID.from(anId);

        final var aProduct = this.productGateway.findById(anProductID);

        if (aProduct.isPresent()) {
            return Either.right(ProductOutPut.from(aProduct.get()));
        }
        return Either.left(DomainException.with(new Error("Product with ID %s not found".formatted(anId))));
    }
}
