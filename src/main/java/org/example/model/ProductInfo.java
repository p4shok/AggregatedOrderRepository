package org.example.model;

import java.math.BigDecimal;
import java.util.Objects;

public record ProductInfo (
        String id,
        BigDecimal price,
        String description,
        Double rating
) {
    public ProductInfo {
        Objects.requireNonNull(id, "id");
    }
}
