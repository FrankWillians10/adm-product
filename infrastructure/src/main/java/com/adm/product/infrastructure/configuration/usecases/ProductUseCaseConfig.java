package com.adm.product.infrastructure.configuration.usecases;

import com.adm.product.application.product.create.CreateProductUseCase;
import com.adm.product.application.product.create.DefaultCreateProductUseCase;
import com.adm.product.application.product.delete.DefaultDeleteProductUseCase;
import com.adm.product.application.product.delete.DeleteProductUseCase;
import com.adm.product.application.product.retrieve.get.DefaultGetProductByIdUseCase;
import com.adm.product.application.product.retrieve.get.GetProductByIdUseCase;
import com.adm.product.application.product.retrieve.list.DefaultListProductsUseCase;
import com.adm.product.application.product.retrieve.list.ListProductsUseCase;
import com.adm.product.application.product.update.DefaultUpdateProductUseCase;
import com.adm.product.application.product.update.UpdateProductUseCase;
import com.adm.product.domain.product.ProductGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductUseCaseConfig {

    private final ProductGateway productGateway;

    public ProductUseCaseConfig(final ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @Bean
    public CreateProductUseCase createProductUseCase() {
        return new DefaultCreateProductUseCase(productGateway);
    }

    @Bean
    public UpdateProductUseCase updateProductUseCase() {
        return new DefaultUpdateProductUseCase(productGateway);
    }

    @Bean
    public GetProductByIdUseCase getProductByIdUseCase() {
        return new DefaultGetProductByIdUseCase(productGateway);
    }

    @Bean
    public ListProductsUseCase listProductsUseCase() {
        return new DefaultListProductsUseCase(productGateway);
    }

    @Bean
    public DeleteProductUseCase deleteProductUseCase() {
        return new DefaultDeleteProductUseCase(productGateway);
    }

}
