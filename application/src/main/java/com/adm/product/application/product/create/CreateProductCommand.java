package com.adm.product.application.product.create;

public record CreateProductCommand(
        String name,
        String brand,
        String description,
        double price
) {
    public static CreateProductCommand with(
       final String aName,
       final String aBrand,
       final String aDescription,
       final double aPrice
    ) {
        return new CreateProductCommand(aName, aBrand, aDescription, aPrice);
    }
}
