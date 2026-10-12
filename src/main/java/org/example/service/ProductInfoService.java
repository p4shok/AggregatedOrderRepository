package org.example.service;

import org.example.model.ProductInfo;
import org.example.port.DescriptionClient;
import org.example.port.PriceClient;
import org.example.port.RatingClient;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

public class ProductInfoService {
    private final PriceClient priceClient;
    private final DescriptionClient descriptionClient;
    private final RatingClient ratingClient;
    private final ExecutorService executor;

    public ProductInfoService(PriceClient priceClient,
                              DescriptionClient descriptionClient,
                              RatingClient ratingClient,
                              ExecutorService executor) {
        this.priceClient = priceClient;
        this.descriptionClient = descriptionClient;
        this.ratingClient = ratingClient;
        this.executor = executor;
    }

    public CompletableFuture<ProductInfo> getProductInfo(String productId) {
        CompletableFuture<BigDecimal> priceF = safe(() -> priceClient.getPrice(productId), BigDecimal.ZERO);
        CompletableFuture<String> descF = safe(() -> descriptionClient.getDescription(productId), "");
        CompletableFuture<Double> ratingF = safe(() -> ratingClient.getRating(productId), 0.0);

        return CompletableFuture
                .allOf(priceF, descF, ratingF)
                .thenApply(v -> new ProductInfo(
                        productId,
                        priceF.join(),
                        descF.join(),
                        ratingF.join()
                ));
    }

    private <T> CompletableFuture<T> safe(Supplier<T> supplier, T fallback) {
        return CompletableFuture.supplyAsync(supplier, executor)
                .exceptionally(ex -> {
                    return fallback;
                });
    }
}
