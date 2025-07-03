package com.adm.product.application.product.update;

import com.adm.product.domain.product.Product;
import com.adm.product.domain.product.ProductID;

public record UpdateProductOutput(
        ProductID id
) {
    public static UpdateProductOutput from(final Product aProduct) {
        return new UpdateProductOutput(aProduct.getId());
    }
}
