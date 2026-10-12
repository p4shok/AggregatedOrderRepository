import org.example.model.ProductInfo;
import org.example.port.DescriptionClient;
import org.example.port.PriceClient;
import org.example.port.RatingClient;
import org.example.service.ProductInfoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ProductInfoServiceTest {

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    @AfterEach
    void tearDown() {
        executor.shutdownNow();
    }

    @Test
    void aggregatesAllThreeSources() {
        PriceClient price = id -> new BigDecimal("99.90");
        DescriptionClient desc = id -> "Nice product";
        RatingClient rating = id -> 4.7;

        ProductInfoService service =
                new ProductInfoService(price, desc, rating, executor);

        ProductInfo info = service.getProductInfo("SKU-1").join();

        assertEquals("SKU-1", info.id());
        assertEquals(new BigDecimal("99.90"), info.price());
        assertEquals("Nice product", info.description());
        assertEquals(4.7, info.rating());
    }

    @Test
    void fallsBackWhenPriceFails() {
        PriceClient price = id -> { throw new RuntimeException("boom"); };
        DescriptionClient desc = id -> "desc";
        RatingClient rating = id -> 3.0;

        ProductInfoService service =
                new ProductInfoService(price, desc, rating, executor);

        ProductInfo info = service.getProductInfo("SKU-2").join();

        assertEquals(BigDecimal.ZERO, info.price());
        assertEquals("desc", info.description());
    }

    @Test
    void runsSourcesInParallel() {
        PriceClient price = id -> sleepAndReturn(500, new BigDecimal("1"));
        DescriptionClient desc = id -> sleepAndReturn(500, "d");
        RatingClient rating = id -> sleepAndReturn(500, 5.0);

        ProductInfoService service =
                new ProductInfoService(price, desc, rating, executor);

        long start = System.nanoTime();
        service.getProductInfo("SKU-3").join();
        long ms = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

        // Если бы вызовы шли последовательно — было бы ~1500ms.
        assertTrue(ms < 1000, "Ожидали параллельное выполнение, но было " + ms + " ms");
    }

    private <T> T sleepAndReturn(long ms, T value) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        return value;
    }
}