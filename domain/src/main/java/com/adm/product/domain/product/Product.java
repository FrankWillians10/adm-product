package com.adm.product.domain.product;

import com.adm.product.domain.AggregateRoot;
import com.adm.product.domain.validation.ValidationHandler;

import java.util.UUID;

public class Product extends AggregateRoot<ProductID> {
    private String name;
    private String brand;
    private String description;
    private double price;

    private Product(
            final ProductID anId,
            final String aName,
            final String aBrand,
            final String aDescription,
            final double aPrice
    ) {
        super(anId);
        this.name = aName;
        this.brand = aBrand;
        this.description = aDescription;
        this.price = aPrice;
    }

    public static Product newProduct(final String aName, final String aBrand, final String aDescription, final double aPrice) {
        final var anId = ProductID.unique();
        return new Product(anId, aName, aBrand, aDescription, aPrice);

    }

    @Override
    public void validate(ValidationHandler handler) {
        new ProductValidator(this, handler).validate();
    }

    public ProductID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

}