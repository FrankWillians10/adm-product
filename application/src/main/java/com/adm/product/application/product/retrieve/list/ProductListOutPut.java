package com.adm.product.application.product.retrieve.list;

import com.adm.product.domain.product.Product;
import com.adm.product.domain.product.ProductID;

public record ProductListOutPut(
        ProductID id,
        String name,
        String brand,
        String description,
        double price
) {
    public static ProductListOutPut from(final Product aProduct) {
        return new ProductListOutPut(
                aProduct.getId(),
                aProduct.getName(),
                aProduct.getBrand(),
                aProduct.getDescription(),
                aProduct.getPrice()
        );
    }
}
