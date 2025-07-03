package com.adm.product.application.product.create;

import com.adm.product.application.Either;
import com.adm.product.domain.product.Product;
import com.adm.product.domain.product.ProductGateway;
import com.adm.product.domain.validation.handler.Notification;

import java.util.Objects;

public class DefaultCreateProductUseCase extends CreateProductUseCase{

    private final ProductGateway productGateway;

    public DefaultCreateProductUseCase(final ProductGateway aProductGateway) {
        this.productGateway = Objects.requireNonNull(aProductGateway);
    }

    @Override
    public Either<Notification, CreateProductOutput> execute(final CreateProductCommand aCommand) {
        final var aName = aCommand.name();
        final var aBrand = aCommand.brand();
        final var aDescription = aCommand.description();
        final var aPrice = aCommand.price();

        final var notification = Notification.create();

        final var aProduct = Product.newProduct(aName, aBrand, aDescription, aPrice);
        aProduct.validate(notification);

        return notification.hasError() ? Either.left(notification) : create(aProduct);

    }

    public Either<Notification, CreateProductOutput> create(final Product aProduct){
        try {
            return Either.right(CreateProductOutput.from(this.productGateway.create(aProduct)));
        } catch (Throwable t) {
            return Either.left(Notification.create(t));
        }
    };
}
