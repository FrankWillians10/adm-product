package com.adm.product.application.product.retrieve.list;

import com.adm.product.domain.pagination.Pagination;
import com.adm.product.domain.product.Product;
import com.adm.product.domain.product.ProductGateway;
import com.adm.product.domain.product.ProductSearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListProductUseCaseTest {

    @InjectMocks
    private DefaultListProductsUseCase useCase;

    @Mock
    private ProductGateway productGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(productGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListProducts_thenShouldReturnProducts() {
        final var products = List.of(
                Product.newProduct("Iphone 13", "Apple", "Um lancamento Apple 2025", 10.000),
                Product.newProduct("Samsung A4", "Samsung", "Um lancamento Samsung 2025", 8.000)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new ProductSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);



        final var expectedPagination =
                new Pagination<>(expectedPage, expectedPerPage, products.size(), products);

        final var expectedItemsCount = 2;
        final var expectedResult = expectedPagination.map(ProductListOutPut::from);

        when(productGateway.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(products.size(), actualResult.total());

    }

    @Test
    public void givenAValidQuery_whenHasNoResults_thenShouldReturnEmptyProducts() {
        final var products = List.<Product>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new ProductSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);



        final var expectedPagination =
                new Pagination<>(expectedPage, expectedPerPage, products.size(), products);

        final var expectedItemsCount = 0;
        final var expectedResult = expectedPagination.map(ProductListOutPut::from);

        when(productGateway.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(products.size(), actualResult.total());
    }

    @Test
    public void givenAValidQuery_whenGatewayThrowsException_shouldReturnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway error";

        final var aQuery =
                new ProductSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        when(productGateway.findAll(eq(aQuery)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException =
                Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(aQuery));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}
