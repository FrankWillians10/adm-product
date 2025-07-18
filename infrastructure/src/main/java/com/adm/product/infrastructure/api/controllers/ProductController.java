package com.adm.product.infrastructure.api.controllers;

import com.adm.product.application.product.create.CreateProductCommand;
import com.adm.product.application.product.create.CreateProductUseCase;
import com.adm.product.application.product.retrieve.get.GetProductByIdUseCase;
import com.adm.product.domain.pagination.Pagination;
import com.adm.product.infrastructure.api.ProductAPI;
import com.adm.product.infrastructure.product.models.CreateProductApiInput;
import com.adm.product.infrastructure.product.models.ProductApiOutPut;
import com.adm.product.infrastructure.product.presenters.ProductApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class ProductController implements ProductAPI {

    private final CreateProductUseCase createProductUseCase;

    private final GetProductByIdUseCase getProductByIdUseCase;

    public ProductController(
            final CreateProductUseCase createProductUseCase,
            final GetProductByIdUseCase getProductByIdUseCase
    ) {
        this.createProductUseCase = Objects.requireNonNull(createProductUseCase);
        this.getProductByIdUseCase = Objects.requireNonNull(getProductByIdUseCase);
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

    @Override
    public ProductApiOutPut getById(final String id) {
        return ProductApiPresenter.present(this.getProductByIdUseCase.execute(id));
    }

}
