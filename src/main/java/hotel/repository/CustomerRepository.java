package hotel.repository;

import hotel.db.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerRepository {

    public Integer findCustomerIdByEmail(String email) {
        String sql = "SELECT id FROM customers WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("findCustomerIdByEmail failed: " + e.getMessage(), e);
        }

        return null;
    }

    public int createCustomer(String name, String email) {
        String sql = "INSERT INTO customers(name, email) VALUES (?, ?) RETURNING id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt("id");
            }

        } catch (Exception e) {
            throw new RuntimeException("createCustomer failed: " + e.getMessage(), e);
        }
    }
}