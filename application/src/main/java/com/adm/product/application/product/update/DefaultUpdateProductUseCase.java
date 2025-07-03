package com.adm.product.application.product.update;

import com.adm.product.application.Either;
import com.adm.product.domain.exceptions.DomainException;
import com.adm.product.domain.product.Product;
import com.adm.product.domain.product.ProductGateway;
import com.adm.product.domain.product.ProductID;
import com.adm.product.domain.validation.Error;
import com.adm.product.domain.validation.handler.Notification;

import java.util.ArrayList;

public class DefaultUpdateProductUseCase extends UpdateProductUseCase {

    private final ProductGateway productGateway;

    public DefaultUpdateProductUseCase(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @Override
    public Either<Notification, UpdateProductOutput> execute(final UpdateProductCommand aCommand) {
        final var anId = ProductID.from(aCommand.id());
        final var aName = aCommand.name();
        final var aBrand = aCommand.brand();
        final var aDescription = aCommand.description();
        final var aPrice = aCommand.price();

        final var aProduct = this.productGateway.findById(anId)
                .orElseThrow(() -> DomainException.with(new Error("Product with ID %s was not found".formatted(anId.getValue()))));

        final var notification = Notification.create();

        aProduct.update(aName, aBrand, aDescription, aPrice)
                .validate(notification);
        return notification.hasError() ? Either.left(notification) : update(aProduct);
    }

    public Either<Notification, UpdateProductOutput> update(final Product aProduct) {
        try {
            return Either.right(UpdateProductOutput.from(this.productGateway.update(aProduct)));
        } catch (Throwable t) {
            return Either.left(Notification.create(t));
        }
    }
}
