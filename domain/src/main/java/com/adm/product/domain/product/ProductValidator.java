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
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.product.getName();
        if (name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
        }

        if (name.isBlank()) {
            this.validationHandler().append(new Error("'name' should not be empty"));
        }

        final var length = name.trim().length();
        if (length > 255 || length < 3) {
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
        }
    }

}
