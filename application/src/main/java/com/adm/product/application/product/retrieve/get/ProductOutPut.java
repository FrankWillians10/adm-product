package com.adm.product.application.product.retrieve.get;

import com.adm.product.domain.product.Product;
import com.adm.product.domain.product.ProductID;

public record ProductOutPut(
        ProductID id,
        String name,
        String brand,
        String description,
        double price
) {
    public static ProductOutPut from(final Product aProduct) {
        return new ProductOutPut(
          aProduct.getId(),
          aProduct.getName(),
          aProduct.getBrand(),
          aProduct.getDescription(),
          aProduct.getPrice()
        );
    }
}
