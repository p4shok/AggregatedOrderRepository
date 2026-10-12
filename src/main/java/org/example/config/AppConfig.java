package org.example.config;

import org.example.adapter.DbDescriptionClient;
import org.example.adapter.HttpPriceClient;
import org.example.adapter.mock.StubRatingClient;
import org.example.port.DescriptionClient;
import org.example.port.PriceClient;
import org.example.port.RatingClient;
import org.example.service.ProductInfoService;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppConfig {
    private final ExecutorService executorService = Executors.newFixedThreadPool(16, r -> {
        Thread t = new Thread(r, "product-info-worker");
        t.setDaemon(true);
        return t;
    });

    public ProductInfoService productInfoService(DataSource dataSource) {
        PriceClient priceClient = new HttpPriceClient("http://price.example.com", Duration.ofSeconds(2));
        DescriptionClient descriptionClient = new DbDescriptionClient(dataSource);
        RatingClient ratingClient = new StubRatingClient();

        return new ProductInfoService(
                priceClient, descriptionClient, ratingClient, executorService
        );
    }

    public ExecutorService executor() {
        return executorService;
    }
}
