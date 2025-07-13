package com.adm.product.application.product.create;

import com.adm.product.domain.product.Product;
import com.adm.product.domain.product.ProductID;

public record CreateProductOutput(
        String id
) {

    public static CreateProductOutput from(final String anId) {
        return new CreateProductOutput(anId);
    }

    public static CreateProductOutput from(final Product aProduct) {
        return new CreateProductOutput(aProduct.getId().getValue());
    }
}
