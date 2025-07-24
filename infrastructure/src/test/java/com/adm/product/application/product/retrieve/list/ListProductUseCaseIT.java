package com.adm.product.application.product.retrieve.list;

import com.adm.product.IntegrationTest;
import com.adm.product.domain.product.Product;
import com.adm.product.domain.product.ProductSearchQuery;
import com.adm.product.infrastructure.product.persistence.ProductJpaEntity;
import com.adm.product.infrastructure.product.persistence.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

@IntegrationTest
public class ListProductUseCaseIT {

    @Autowired
    private ListProductsUseCase useCase;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void mockUp() {
        final var products = Stream.of(
                Product.newProduct("Iphone 13", "Apple", "Um lancamento Apple 2025", 10.000),
                Product.newProduct("Pro Art", "Asus", "Um lancamento Asus 2025", 3.500),
                Product.newProduct("Aspire 5", "Acer", "Um lancamento Acer 2025", 4.000),
                Product.newProduct("Galaxy A4", "Samsung", "Um lancamento Samsung 2025", 2.000),
                Product.newProduct("Blue yeti", "Blue", "Um lancamento Blue 2025", 1.800),
                Product.newProduct("MX Keys", "Logitech", "Um lancamento Logitech 2025", 700.00)
        )
                .map(ProductJpaEntity::from)
                .toList();

        productRepository.saveAllAndFlush(products);
    }

    @Test
    public void givenAValidTerm_whenTermDoesntMatchsPrePersisted_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "jfjfjf";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final var aQuery =
        new ProductSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
    }

    @ParameterizedTest
    @CsvSource({
        "iph,0,10,1,1,Iphone 13",
        "pro,0,10,1,1,Pro Art",
        "asp,0,10,1,1,Aspire 5",
        "gal,0,10,1,1,Galaxy A4",
        "blue,0,10,1,1,Blue yeti"
    })
    public void givenAValidTerm_whenCallsListProducts_shouldReturnProductsFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedProductName
    ) {

            final var expectedSort = "name";
            final var expectedDirection = "asc";

            final var aQuery =
                    new ProductSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

            final var actualResult = useCase.execute(aQuery);

            Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
            Assertions.assertEquals(expectedPage, actualResult.currentPage());
            Assertions.assertEquals(expectedPerPage, actualResult.perPage());
            Assertions.assertEquals(expectedTotal, actualResult.total());
            Assertions.assertEquals(expectedProductName, actualResult.items().get(0).name());

    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,6,6,Aspire 5",
            "name,desc,0,10,6,6,Pro Art",
            "brand,asc,0,10,6,6,Acer",
            "brand,desc,0,10,6,6,Samsung"
    })
    public void givenAValidSortAndDirection_whenCallsListProducts_thenShouldReturnProductsOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedProductName
    ) {

        final var expectedTerms = "";

        final var aQuery =
                new ProductSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
//        Assertions.assertEquals(expectedProductName, actualResult.items().get(0).name());


    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,6,Aspire 5",
            "1,2,2,6,Galaxy A4",
            "2,2,2,6,MX Keys"
    })
    public void givenAValidPage_whenCallsListProducts_shouldReturnProductsPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedProductName
    ) {

        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTerms = "";

        final var aQuery =
                new ProductSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());

        int index = 0;
        for (final String expectedName : expectedProductName.split(";")) {
            Assertions.assertEquals(expectedProductName, actualResult.items().get(0).name());
        }
    }

}
