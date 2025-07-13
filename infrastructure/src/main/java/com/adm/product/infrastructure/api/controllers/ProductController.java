package com.adm.product.infrastructure.api.controllers;

import com.adm.product.application.product.create.CreateProductCommand;
import com.adm.product.application.product.create.CreateProductOutput;
import com.adm.product.application.product.create.CreateProductUseCase;
import com.adm.product.domain.pagination.Pagination;
import com.adm.product.domain.validation.handler.Notification;
import com.adm.product.infrastructure.api.ProductAPI;
import com.adm.product.infrastructure.product.models.CreateProductApiInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class ProductController implements ProductAPI {

    private final CreateProductUseCase createProductUseCase;

    public ProductController(final CreateProductUseCase createProductUseCase) {
        this.createProductUseCase = Objects.requireNonNull(createProductUseCase);
    }

    @Override
    public ResponseEntity<?> createProduct(final CreateProductApiInput input) {
        final var aCommand = CreateProductCommand.with(
                input.name(),
                input.brand(),
                input.description(),
                input.price()
        );

        final var response = this.createProductUseCase.execute(aCommand);

        if (response.isLeft()){
            final var notification = response.getLeft();
            return  ResponseEntity.unprocessableEntity().body(notification);
        }

        return  ResponseEntity.created(URI.create("/products/" + response.getRight().id())).body(response.getRight());

    }

    @Override
    public Pagination<?> listProducts(String search, int page, int perPage, String sort, String direction) {
        return null;
    }

}
