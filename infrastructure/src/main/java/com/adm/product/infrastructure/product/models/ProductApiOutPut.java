package com.adm.product.infrastructure.product.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductApiOutPut(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("brand") String brand,
        @JsonProperty("description") String description,
        @JsonProperty("price") double price
) {
}
