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
        checkBrandConstraints();
        checkDescriptionConstraints();
        checkPriceConstraints();
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

    private void checkBrandConstraints() {
        final var brand = this.product.getBrand();
        if (brand == null) {
            this.validationHandler().append(new Error("'brand' should not be null"));
        }

        if (brand.isBlank()) {
            this.validationHandler().append(new Error("'brand' should not be empty"));
        }

        final var length = brand.trim().length();
        if (length > 20 || length < 3) {
            this.validationHandler().append(new Error("'brand' must be between 3 and 20 characters"));
        }
    }

    private void checkDescriptionConstraints() {
        final var description = this.product.getDescription();
        if (description == null) {
            this.validationHandler().append(new Error("'description' should not be null"));
        }

        if (description.isBlank()) {
            this.validationHandler().append(new Error("'description' should not be Empty"));
        }

        final var length = description.trim().length();
        if (length > 255 || length < 3) {
            this.validationHandler().append(new Error("'description' must be between 3 and 255 characters"));
        }
    }

    private void checkPriceConstraints() {
        final var price = this.product.getPrice();
        if (price < 10.00) {
            this.validationHandler().append(new Error("'price' cannot be less than 10.00"));
        }
    }

}
