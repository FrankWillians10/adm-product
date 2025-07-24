package com.adm.product.infrastructure.product.persistence;

import com.adm.product.domain.product.Product;
import com.adm.product.domain.product.ProductID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product_outbox")
public class ProductOutBox {

    @Id
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "description", length = 4000, nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    private ProductOutBox() {}

    private ProductOutBox(
            final String id,
            final String name,
            final String brand,
            final String description,
            final double price
    ) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
    }

    public static ProductOutBox fromProduct(final Product product) {
        return new ProductOutBox(
                product.getId().getValue(),
                product.getName(),
                product.getBrand(),
                product.getDescription(),
                product.getPrice()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
