package com.adm.product.infrastructure.api.controllers;

import com.adm.product.application.product.create.CreateProductCommand;
import com.adm.product.application.product.create.CreateProductUseCase;
import com.adm.product.application.product.delete.DeleteProductUseCase;
import com.adm.product.application.product.retrieve.get.GetProductByIdUseCase;
import com.adm.product.application.product.retrieve.list.ListProductsUseCase;
import com.adm.product.application.product.update.UpdateProductCommand;
import com.adm.product.application.product.update.UpdateProductUseCase;
import com.adm.product.domain.pagination.Pagination;
import com.adm.product.domain.product.ProductSearchQuery;
import com.adm.product.infrastructure.api.ProductAPI;
import com.adm.product.infrastructure.product.models.CreateProductApiInput;
import com.adm.product.infrastructure.product.models.UpdateProductApiInput;
import com.adm.product.infrastructure.product.presenters.ProductApiPresenter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class ProductController implements ProductAPI {

    private final CreateProductUseCase createProductUseCase;

    private final GetProductByIdUseCase getProductByIdUseCase;

    private final ListProductsUseCase listProductsUseCase;

    private final UpdateProductUseCase updateProductUseCase;

    private final DeleteProductUseCase deleteProductUseCase;

    public ProductController(
            final CreateProductUseCase createProductUseCase,
            final GetProductByIdUseCase getProductByIdUseCase,
            final ListProductsUseCase listProductsUseCase,
            final UpdateProductUseCase updateProductUseCase,
            final DeleteProductUseCase deleteProductUseCase
    ) {
        this.createProductUseCase = Objects.requireNonNull(createProductUseCase);
        this.getProductByIdUseCase = Objects.requireNonNull(getProductByIdUseCase);
        this.listProductsUseCase = Objects.requireNonNull(listProductsUseCase);
        this.updateProductUseCase = Objects.requireNonNull(updateProductUseCase);
        this.deleteProductUseCase = Objects.requireNonNull(deleteProductUseCase);
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
    public ResponseEntity<?> updateById(final String id, final UpdateProductApiInput input) {
        final var aCommand = UpdateProductCommand.with(
                id,
                input.name(),
                input.brand(),
                input.description(),
                input.price()
        );
        final var response = this.updateProductUseCase.execute(aCommand);

        if (response.isLeft()){
            final var notification = response.getLeft();
            return  ResponseEntity.unprocessableEntity().body(notification);
        }
        return  ResponseEntity.ok(response.getRight());
    }

    @Override
    public void deleteById(String id) {
        this.deleteProductUseCase.execute(id);
    }

    @Override
    public Pagination<?> listProducts(
            String search,
            int page,
            int perPage,
            String sort,
            String direction
    ) {
        return listProductsUseCase
                .execute(new ProductSearchQuery(page, perPage, search, sort, direction));
    }

    @Override
    public ResponseEntity<?> getById(final String id) {
        final var response = this.getProductByIdUseCase.execute(id);

        if (response.isRight()) {
            final var r = ProductApiPresenter.present(response.getRight());
            return ResponseEntity.status(HttpStatus.OK).body(r);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response.getLeft().getMessage());

    }

}
