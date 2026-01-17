package hotel.repository;

import hotel.db.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookingRepository {

    public int createBooking(int customerId, int roomId) {
        String sql = "INSERT INTO bookings(customer_id, room_id) VALUES (?, ?) RETURNING id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ps.setInt(2, roomId);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt("id");
            }

        } catch (Exception e) {
            throw new RuntimeException("createBooking failed: " + e.getMessage(), e);
        }
    }
}