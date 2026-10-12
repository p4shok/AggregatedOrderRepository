package org.example.port;

import java.math.BigDecimal;

public interface PriceClient {
    BigDecimal getPrice(String productId);
}
