package com.adm.product.domain.product;

public record ProductSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction
) {
}
