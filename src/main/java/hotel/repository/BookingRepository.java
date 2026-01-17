package hotel.repository;

import hotel.db.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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

    public Integer findLatestBookingIdByRoomId(int roomId) {
        String sql = "SELECT id FROM bookings WHERE room_id = ? ORDER BY id DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }

        } catch (Exception e) {
            throw new RuntimeException("findLatestBookingIdByRoomId failed: " + e.getMessage(), e);
        }

        return null;
    }

    public void deleteBookingById(int bookingId) {
        String sql = "DELETE FROM bookings WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("deleteBookingById failed: " + e.getMessage(), e);
        }
    }

    public List<String> getAllBookingsDetails() {
        List<String> rows = new ArrayList<>();
        String sql =
                "SELECT b.id AS booking_id, b.booking_date, " +
                        "c.name AS customer_name, c.email AS customer_email, " +
                        "r.room_number, r.room_type, r.price_per_night " +
                        "FROM bookings b " +
                        "JOIN customers c ON b.customer_id = c.id " +
                        "JOIN rooms r ON b.room_id = r.id " +
                        "ORDER BY b.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String line = "Booking{id=" + rs.getInt("booking_id") +
                        ", date=" + rs.getDate("booking_date") +
                        ", customer=" + rs.getString("customer_name") +
                        " (" + rs.getString("customer_email") + ")" +
                        ", room=" + rs.getInt("room_number") +
                        " " + rs.getString("room_type") +
                        ", price=" + rs.getDouble("price_per_night") +
                        "}";
                rows.add(line);
            }

        } catch (Exception e) {
            throw new RuntimeException("getAllBookingsDetails failed: " + e.getMessage(), e);
        }

        return rows;
    }
}