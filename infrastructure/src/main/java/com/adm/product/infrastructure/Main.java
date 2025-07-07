package com.adm.product.infrastructure;

import com.adm.product.domain.product.Product;
import com.adm.product.infrastructure.configuration.WebServerConfig;
import com.adm.product.infrastructure.product.persistence.ProductJpaEntity;
import com.adm.product.infrastructure.product.persistence.ProductRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.AbstractEnvironment;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development");
        SpringApplication.run(WebServerConfig.class, args);

    }

    @Bean
    public ApplicationRunner runner(ProductRepository repository) {
        return args -> {
            List<ProductJpaEntity> all = repository.findAll();
            Product product = Product.newProduct("Apple Iphone 10", "Samsung", "Um lancamento Samsung 2025", 10.000);

            repository.saveAndFlush(ProductJpaEntity.from(product));

            repository.deleteAll();

        };
    }
}