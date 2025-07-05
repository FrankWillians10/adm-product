package com.adm.product.application.product.retrieve.list;

import com.adm.product.domain.pagination.Pagination;
import com.adm.product.domain.product.ProductGateway;
import com.adm.product.domain.product.ProductSearchQuery;

import java.util.Objects;

public class DefaultListProductsUseCase extends ListProductsUseCase{

    private final ProductGateway productGateway;

    public DefaultListProductsUseCase(final ProductGateway productGateway) {
        this.productGateway = Objects.requireNonNull(productGateway);
    }

    @Override
    public Pagination<ProductListOutPut> execute(final ProductSearchQuery aQuery) {
        return this.productGateway.findAll(aQuery)
                .map(ProductListOutPut::from);
    }
}
