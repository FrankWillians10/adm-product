package com.adm.product.infrastructure.product.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOutBoxRepository extends JpaRepository<ProductOutBox, String> {}
