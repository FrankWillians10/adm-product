package com.adm.product.infrastructure.api.controllers;

import com.adm.product.domain.pagination.Pagination;
import com.adm.product.infrastructure.api.ProductAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController implements ProductAPI {

    @Override
    public ResponseEntity<?> createProduct() {
        return null;
    }

    @Override
    public Pagination<?> listProducts(String search, int page, int perPage, String sort, String direction) {
        return null;
    }

}
