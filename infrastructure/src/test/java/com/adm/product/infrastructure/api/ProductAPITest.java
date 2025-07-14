package com.adm.product.infrastructure.api;

import com.adm.product.ControllerTest;
import com.adm.product.application.Either;
import com.adm.product.application.product.create.CreateProductCommand;
import com.adm.product.application.product.create.CreateProductOutput;
import com.adm.product.application.product.create.CreateProductUseCase;
import com.adm.product.domain.exceptions.DomainException;
import com.adm.product.domain.product.ProductID;
import com.adm.product.domain.validation.Error;
import com.adm.product.domain.validation.handler.Notification;
import com.adm.product.infrastructure.product.models.CreateProductApiInput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@ControllerTest(controllers = ProductAPI.class)
public class ProductAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateProductUseCase createProductUseCase;

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

}
