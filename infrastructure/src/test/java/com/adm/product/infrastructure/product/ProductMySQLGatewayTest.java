package com.adm.product.infrastructure.product;

import com.adm.product.domain.product.Product;
import com.adm.product.domain.product.ProductID;
import com.adm.product.domain.product.ProductSearchQuery;
import com.adm.product.infrastructure.MySQLGatewayTest;
import com.adm.product.infrastructure.product.persistence.ProductJpaEntity;
import com.adm.product.infrastructure.product.persistence.ProductRepository;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

@MySQLGatewayTest
public class ProductMySQLGatewayTest {

    @Autowired
    public ProductMySQLGateway productGateway;

    @Autowired
    public ProductRepository productRepository;

    @Test
    public void testInjectedDependencies() {

        Assertions.assertNotNull(productGateway);
        Assertions.assertNotNull(productRepository);

    }

    // Criar um teste de update de Product

    @Test
    public void givenAValidProduct_whenCallsCreate_shouldReturnANewProduct() {

        final var expectedName = "Iphone 13";
        final var expectedBrand = "Apple";
        final var expectedDescription = "Um lancamento Apple 2025";
        final var expectedPrice = 10.000;

        final var aProduct =
                Product.newProduct(expectedName, expectedBrand, expectedDescription, expectedPrice);

        Assertions.assertEquals(0, productRepository.count());

        final var actualProduct = productGateway.create(aProduct);

        Assertions.assertEquals(1, productRepository.count());

        Assertions.assertEquals(aProduct.getId().getValue(), actualProduct.getId().getValue());
        Assertions.assertEquals(expectedName, actualProduct.getName());
        Assertions.assertEquals(expectedBrand, actualProduct.getBrand());
        Assertions.assertEquals(expectedDescription, actualProduct.getDescription());
        Assertions.assertEquals(expectedPrice, actualProduct.getPrice());

        final var actualEntity = productRepository.findById(aProduct.getId().getValue()).get();

        Assertions.assertEquals(aProduct.getId().getValue(), actualProduct.getId().getValue());
        Assertions.assertEquals(expectedName, actualProduct.getName());
        Assertions.assertEquals(expectedBrand, actualProduct.getBrand());
        Assertions.assertEquals(expectedDescription, actualProduct.getDescription());
        Assertions.assertEquals(expectedPrice, actualProduct.getPrice());

    }

    @Test
    public void givenAPrePersistedProductAndValidProductId_whenTryToDeleteIt_shouldDeleteProduct() {
        Product aProduct = Product.newProduct("Iphone 13", "Apple", "Um lancamento Apple 2025", 10.000);

        Assertions.assertEquals(0, productRepository.count());

        productRepository.saveAndFlush(ProductJpaEntity.from(aProduct));

        Assertions.assertEquals(1, productRepository.count());

        productGateway.deleteById(aProduct.getId());

        Assertions.assertEquals(0, productRepository.count());

    }

    @Test
    public void givenAInvalidProductId_whenTryToDeleteIt_shouldDeleteProduct() {

        Assertions.assertEquals(0, productRepository.count());

        productGateway.deleteById(ProductID.from("invalid"));

        Assertions.assertEquals(0, productRepository.count());
    }

    @Test
    public void givenAPrePersistedProductAndValidProductId_whenCallsFindById_shouldReturnProduct() {

        final var expectedName = "Iphone 13";
        final var expectedBrand = "Apple";
        final var expectedDescription = "Um lancamento Apple 2025";
        final var expectedPrice = 10.000;

        final var aProduct =
                Product.newProduct(expectedName, expectedBrand, expectedDescription, expectedPrice);

        Assertions.assertEquals(0, productRepository.count());

        productRepository.saveAndFlush(ProductJpaEntity.from(aProduct));

        Assertions.assertEquals(1, productRepository.count());

        final var actualProduct = productGateway.findById(aProduct.getId()).get();

        Assertions.assertEquals(1, productRepository.count());

        Assertions.assertEquals(aProduct.getId(), actualProduct.getId());
        Assertions.assertEquals(expectedName, actualProduct.getName());
        Assertions.assertEquals(expectedBrand, actualProduct.getBrand());
        Assertions.assertEquals(expectedDescription, actualProduct.getDescription());
        Assertions.assertEquals(expectedPrice, actualProduct.getPrice());


    }

    @Test
    public void givenAValidProductIdNotStored_whenCallsFindById_shouldReturnEmpty() {

        Assertions.assertEquals(0, productRepository.count());

        final var actualProduct = productGateway.findById(ProductID.from("empty"));

        Assertions.assertTrue(actualProduct.isEmpty());

    }

    @Test
    public void givenPrePersistedProducts_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var smartphone = Product.newProduct("Iphone 13", "Apple", "Um lancamento Apple 2025", 10.000);
        final var laptops = Product.newProduct("Aspire 5", "Acer", "Um lancamento Acer 2025", 3.500);
        final var monitors = Product.newProduct("Pro Art", "Asus", "Um lancamento Asus 2025", 3.500);

        Assertions.assertEquals(0, productRepository.count());

        productRepository.saveAll(List.of(
                ProductJpaEntity.from(smartphone),
                ProductJpaEntity.from(laptops),
                ProductJpaEntity.from(monitors)
        ));

        Assertions.assertEquals(expectedTotal, productRepository.count());

        final var query = new ProductSearchQuery(0, 1, "", "name", "asc");

        final var actualResult = productGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(laptops.getId(), actualResult.items().get(0).getId());

    }

    @Test
    public void givenEmptyProductsTable_whenCallsFindAll_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        Assertions.assertEquals(0, productRepository.count());

        final var query = new ProductSearchQuery(0, 1, "", "name", "asc");

        final var actualResult = productGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(0, actualResult.items().size());
    }

    @Test
    public void givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var smartphone = Product.newProduct("Iphone 13", "Apple", "Um lancamento Apple 2025", 10.000);
        final var laptops = Product.newProduct("Aspire 5", "Acer", "Um lancamento Acer 2025", 3.500);
        final var monitors = Product.newProduct("Pro Art", "Asus", "Um lancamento Asus 2025", 3.500);

        Assertions.assertEquals(0, productRepository.count());

        productRepository.saveAll(List.of(
                ProductJpaEntity.from(smartphone),
                ProductJpaEntity.from(laptops),
                ProductJpaEntity.from(monitors)
        ));

        Assertions.assertEquals(expectedTotal, productRepository.count());

        // Page 1
        final var query = new ProductSearchQuery(0, 1, "", "name", "asc");

        final var actualResult = productGateway.findAll(query);

        System.out.println("Name: " + actualResult.items().get(0).getName() + " Id: " + actualResult.items().get(0).getId().getValue());
        System.out.println("Id: " + actualResult.items().get(0).getId().getValue());
        System.out.println("Id laptop: " + laptops.getId().getValue());

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(laptops.getId().getValue(), actualResult.items().get(0).getId().getValue());

        // Page 2
        final var expectedPage1 = 1;
        final var query1 = new ProductSearchQuery(1, 1, "", "name", "asc");

        final var actualResult1 = productGateway.findAll(query1);

        Assertions.assertEquals(expectedPage1, actualResult1.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult1.perPage());
        Assertions.assertEquals(expectedTotal, actualResult1.total());
        Assertions.assertEquals(expectedPerPage, actualResult1.items().size());
        Assertions.assertEquals(smartphone.getId(), actualResult1.items().get(0).getId());

        // Page 3
        final var expectedPage2 = 2;
        final var query2 = new ProductSearchQuery(2, 1, "", "name", "asc");

        final var actualResult2 = productGateway.findAll(query2);

        Assertions.assertEquals(expectedPage2, actualResult2.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult2.perPage());
        Assertions.assertEquals(expectedTotal, actualResult2.total());
        Assertions.assertEquals(expectedPerPage, actualResult2.items().size());
        Assertions.assertEquals(monitors.getId(), actualResult2.items().get(0).getId());
    }

    @Test
    public void givenPrePersistedProductsAndIphAsTerms_whenCallsFindAllAndMatchsProductName_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var smartphone = Product.newProduct("Iphone 13", "Apple", "Um lancamento Apple 2025", 10.000);
        final var laptops = Product.newProduct("Aspire 5", "Acer", "Um lancamento Acer 2025", 3.500);
        final var monitors = Product.newProduct("Pro Art", "Asus", "Um lancamento Asus 2025", 3.500);

        Assertions.assertEquals(0, productRepository.count());

        productRepository.saveAll(List.of(
                ProductJpaEntity.from(smartphone),
                ProductJpaEntity.from(laptops),
                ProductJpaEntity.from(monitors)
        ));

        Assertions.assertEquals(expectedTotal, productRepository.count());

        final var query = new ProductSearchQuery(0, 1, "Iph", "name", "asc");

        final var actualResult = productGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(1, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(smartphone.getId(), actualResult.items().get(0).getId());

    }

    @Test
    public void givenPrePersistedProductsAndAceAsTerms_whenCallsFindAllAndMatchsProductBrand_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var smartphone = Product.newProduct("Iphone 13", "Apple", "Um lancamento Apple 2025", 10.000);
        final var laptops = Product.newProduct("Aspire 5", "Acer", "Um lancamento Acer 2025", 3.500);
        final var monitors = Product.newProduct("Pro Art", "Asus", "Um lancamento Asus 2025", 3.500);

        Assertions.assertEquals(0, productRepository.count());

        productRepository.saveAll(List.of(
                ProductJpaEntity.from(smartphone),
                ProductJpaEntity.from(laptops),
                ProductJpaEntity.from(monitors)
        ));

        Assertions.assertEquals(expectedTotal, productRepository.count());

        final var query = new ProductSearchQuery(0, 1, "Ace", "name", "asc");

        final var actualResult = productGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(1, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(laptops.getId(), actualResult.items().get(0).getId());

    }

}


