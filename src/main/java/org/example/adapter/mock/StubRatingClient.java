package org.example.adapter.mock;

import org.example.port.RatingClient;

public class StubRatingClient implements RatingClient {
    @Override
    public Double getRating(String productId) {
        return 4.5;
    }
}
