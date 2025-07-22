package com.adm.product.infrastructure.product.persistence;

import com.adm.product.domain.product.Product;
import com.adm.product.domain.product.ProductID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class ProductJpaEntity {

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

    private ProductJpaEntity() {}

    private ProductJpaEntity(
            final String id,
            final String name,
            final String brand,
            final String description,
            final double price) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
    }

    public static ProductJpaEntity from(final Product aProduct) {
        return new ProductJpaEntity(
                aProduct.getId().getValue(),
                aProduct.getName(),
                aProduct.getBrand(),
                aProduct.getDescription(),
                aProduct.getPrice()
        );
    }

    public Product toAggregate() {
        return Product.with(
                ProductID.from(this.getId()),
                this.getName(),
                this.getBrand(),
                this.getDescription(),
                this.getPrice()
        );
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        System.out.println(this.description);
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return this.price;
    }

}
