package com.adm.product.domain.product;

import com.adm.product.domain.validation.Error;
import com.adm.product.domain.validation.ValidationHandler;
import com.adm.product.domain.validation.Validator;

public class ProductValidator extends Validator {

    private final Product product;

    public ProductValidator(final Product aProduct, final ValidationHandler aHandler) {
        super(aHandler);
        this.product = aProduct;
    }

    @Override
    public void validate() {
        if (this.product.getName() == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
        }
    }

}
