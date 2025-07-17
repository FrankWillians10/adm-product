package com.adm.product.infrastructure.product.presenters;

import com.adm.product.application.product.retrieve.get.ProductOutPut;
import com.adm.product.infrastructure.product.models.ProductApiOutPut;

public interface ProductApiPresenter {

    static ProductApiOutPut present(final ProductOutPut outPut) {
        return new ProductApiOutPut(
                outPut.id().getValue(),
                outPut.name(),
                outPut.brand(),
                outPut.description(),
                outPut.price()
        );
    }
}
