package org.example.adapter;

import org.example.port.PriceClient;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpPriceClient implements PriceClient {
    private final HttpClient http;
    private final String baseUrl;

    public HttpPriceClient(String baseUrl, Duration timeout) {
        this.baseUrl = baseUrl;
        this.http = HttpClient.newBuilder()
                .connectTimeout(timeout)
                .build();
    }

    @Override
    public BigDecimal getPrice(String productId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/product/" + productId + "/price"))
                .timeout(Duration.ofSeconds(2))
                .GET()
                .build();
        try {
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200) {
                throw new RuntimeException("Price API returned " + response.statusCode());
            }
            return new BigDecimal(response.body().trim());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch price for " + productId, e);
        }
    }
}
