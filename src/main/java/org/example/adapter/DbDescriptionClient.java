package org.example.adapter;

import org.example.port.DescriptionClient;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DbDescriptionClient implements DescriptionClient {
    private final DataSource dataSource;

    public DbDescriptionClient(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String getDescription(String productId) {
        String sql = "SELECT description FROM product_descriptions WHERE product_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("description") : " ";
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch description for " + productId, e);
        }
    }
}
