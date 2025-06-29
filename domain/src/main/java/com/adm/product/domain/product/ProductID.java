package com.adm.product.domain.product;

import com.adm.product.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class ProductID extends Identifier {

    private final String value;

    private ProductID(String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static ProductID unique() {
        return new ProductID(UUID.randomUUID().toString().toLowerCase());
    }

    public static ProductID from(final String anId) {
        return new ProductID(anId);
    }

    public static ProductID from(final UUID anId) {
        return new ProductID(anId.toString().toLowerCase());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductID)) return false;
        final ProductID productID = (ProductID) o;
        return getValue().equals(productID.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
