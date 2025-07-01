package com.adm.product.domain.product;

import com.adm.product.domain.pagination.Pagination;

import java.util.Optional;

public interface ProductGateway {

    Product create(Product aProduct);

    void deleteById(ProductID anId);

    Optional<Product> findById(ProductID anId);

    Product update(Product aProduct);

    Pagination<Product> findAll(ProductSearchQuery aQuery);

}
