package com.adm.product.application.product.update;

public record UpdateProductCommand(
        String id,
        String name,
        String brand,
        String description,
        double price
) {
    public static UpdateProductCommand with(
            final String anId,
            final String aName,
            final String aBrand,
            final String aDescription,
            final double aPrice
    ) {
        return new UpdateProductCommand(anId, aName, aBrand, aDescription, aPrice);
    }
}
