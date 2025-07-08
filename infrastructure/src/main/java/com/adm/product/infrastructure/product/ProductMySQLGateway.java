package com.adm.product.infrastructure.product;


import com.adm.product.domain.pagination.Pagination;
import com.adm.product.domain.product.Product;
import com.adm.product.domain.product.ProductGateway;
import com.adm.product.domain.product.ProductID;
import com.adm.product.domain.product.ProductSearchQuery;
import com.adm.product.infrastructure.product.persistence.ProductJpaEntity;
import com.adm.product.infrastructure.product.persistence.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductMySQLGateway implements ProductGateway {

    private final ProductRepository repository;

    public ProductMySQLGateway(final ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product create(final Product aProduct) {
        return this.repository.save(ProductJpaEntity.from(aProduct)).toAggregate();
    }

    @Override
    public void deleteById(ProductID anId) {

    }

    @Override
    public Optional<Product> findById(ProductID anId) {
        return Optional.empty();
    }

    @Override
    public Product update(Product aProduct) {
        return null;
    }

    @Override
    public Pagination<Product> findAll(ProductSearchQuery aQuery) {
        return null;
    }
}
