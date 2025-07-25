package com.adm.product.domain.product;

import com.adm.product.domain.AggregateRoot;
import com.adm.product.domain.validation.ValidationHandler;

import java.util.Objects;
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

    public static Product with(
            final ProductID anId,
            final String aName,
            final String aBrand,
            final String aDescription,
            final double aPrice) {
        return new Product(
                anId,
                aName,
                aBrand,
                aDescription,
                aPrice);
    }

    public static Product with(final Product aProduct) {
        return with(
                aProduct.getId(),
                aProduct.name,
                aProduct.brand,
                aProduct.description,
                aProduct.price
        );
    }


    @Override
    public void validate(ValidationHandler handler) {
        new ProductValidator(this, handler).validate();
    }

    public Product update(
            final String aName,
            final String aBrand,
            final String aDescription,
            final double aPrice
    ) {
        this.name = aName;
        this.brand = aBrand;
        this.description = aDescription;
        this.price = aPrice;

        return this;
    }

    public ProductID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getBrand() {
        return this.brand;
    }

    public String getDescription() {
        return this.description;
    }

    public double getPrice() {
        return this.price;
    }

}