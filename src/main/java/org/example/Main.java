package org.example;

import org.example.config.AppConfig;
import org.example.model.ProductInfo;
import org.example.service.ProductInfoService;

import java.util.concurrent.TimeUnit;

public class Main {
    static void main(String[] args) throws Exception {
        AppConfig config = new AppConfig();

        var daraSource = new org.h2.jdbcx.JdbcDataSource();
        daraSource.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");

        ProductInfoService productInfoService = config.productInfoService(daraSource);

        long start = System.currentTimeMillis();
        ProductInfo info = productInfoService.getProductInfo("SKU-123")
                .orTimeout(5, TimeUnit.SECONDS)
                .join();
        long elapsed = System.currentTimeMillis() - start;

        System.out.println(info);
        System.out.println("Elapsed: " + elapsed + " ms");

        config.executor().shutdown();
    }
}