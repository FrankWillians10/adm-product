package com.adm.product.infrastructure.api;

import com.adm.product.ControllerTest;
import com.adm.product.application.Either;
import com.adm.product.application.product.create.CreateProductOutput;
import com.adm.product.application.product.create.CreateProductUseCase;
import com.adm.product.application.product.delete.DeleteProductUseCase;
import com.adm.product.application.product.retrieve.get.GetProductByIdUseCase;
import com.adm.product.application.product.retrieve.get.ProductOutPut;
import com.adm.product.application.product.retrieve.list.ListProductsUseCase;
import com.adm.product.application.product.update.UpdateProductUseCase;
import com.adm.product.domain.exceptions.DomainException;
import com.adm.product.domain.product.Product;
import com.adm.product.domain.product.ProductID;
import com.adm.product.domain.validation.Error;
import com.adm.product.domain.validation.handler.Notification;
import com.adm.product.infrastructure.product.models.CreateProductApiInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = ProductAPI.class)
public class ProductAPITest {

    @Autowired
    public MockMvc mvc;

    @Autowired
    public ObjectMapper mapper;

    @MockBean
    public CreateProductUseCase createProductUseCase;

    @MockBean
    public GetProductByIdUseCase getProductByIdUseCase;

    @MockBean
    public ListProductsUseCase listProductsUseCase;

    @MockBean
    public UpdateProductUseCase updateProductUseCase;

    @MockBean
    public DeleteProductUseCase deleteProductUseCase;


    @Test
    public void givenAValidCommand_whenCallsCreateProduct_shouldReturnProductId() throws Exception {
        final var expectedName = "Iphone 13";
        final var expectedBrand = "Apple";
        final var expectedDescription = "Um lancamento Apple 2025";
        final var expectedPrice = 10.000;

        final var aInput = new CreateProductApiInput(expectedName, expectedBrand, expectedDescription, expectedPrice);

        when(createProductUseCase.execute(any()))
                .thenReturn(Either.right(CreateProductOutput.from("123")));

        final var request = post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpectAll(status().isCreated())
                .andExpectAll(header().string("Location", "/products/123"))
                .andExpectAll(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        verify(createProductUseCase, times(1)).execute(argThat(cmd ->
                        Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedBrand, cmd.brand())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedPrice, cmd.price())
                ));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateProduct_thenShouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedBrand = "Apple";
        final var expectedDescription = "Um lancamento Apple 2025";
        final var expectedPrice = 10.000;
        final var expectedErrorMessage = "'name' should not be null";

        final var aInput = new CreateProductApiInput(expectedName, expectedBrand, expectedDescription, expectedPrice);

        when(createProductUseCase.execute(any()))
                .thenReturn(Either.left(Notification.create(new Error(expectedErrorMessage))));

        final var request = post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location",nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createProductUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedBrand, cmd.brand())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedPrice, cmd.price())
        ));
    }

    @Test
    public void givenAInvalidCommand_whenCallsCreateProduct_thenShouldReturnDomainException() throws Exception {
        final String expectedName = null;
        final var expectedBrand = "Apple";
        final var expectedDescription = "Um lancamento Apple 2025";
        final var expectedPrice = 10.000;
        final var expectedErrorMessage = "'name' should not be null";

        final var aInput = new CreateProductApiInput(expectedName, expectedBrand, expectedDescription, expectedPrice);

        when(createProductUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedErrorMessage)));

        final var request = post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location",nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createProductUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedBrand, cmd.brand())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedPrice, cmd.price())
        ));
    }

    @Test
    public void givenAValidId_whenCallsGetProduct_shouldReturnProduct() throws Exception {
        final var expectedName = "Iphone 13";
        final var expectedBrand = "Apple";
        final var expectedDescription = "Um lancamento Apple 2025";
        final var expectedPrice = 10.000;

        final var aProduct = Product.newProduct(expectedName, expectedBrand, expectedDescription, expectedPrice);

        final var expectedId = aProduct.getId().getValue();

        when(getProductByIdUseCase.execute(any()))
                .thenReturn(Either.right(ProductOutPut.from(aProduct)));

        final var request = MockMvcRequestBuilders.get("/products/{id}", expectedId);

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.brand", equalTo(expectedBrand)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.price", equalTo(expectedPrice)))
        ;

        verify(getProductByIdUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenAInvalidId_whenCallsGetProduct_shouldReturnNotFound() throws Exception {
        final var expectedErrorMessage = "Product with ID 456 was not found";
        final String expectedId = ProductID.from("456").getValue();

        when(getProductByIdUseCase.execute(any()))
                .thenThrow(DomainException.with(
                        new Error("Product with ID %s was not found".formatted(expectedId))
                ));

        final var request = MockMvcRequestBuilders.get("/products/{id}", expectedId);

        final var response = this.mvc.perform(request)
                .andDo(print());
        System.out.println(response);
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

    }

}
