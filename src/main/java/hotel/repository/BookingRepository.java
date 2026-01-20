package hotel.repository;

import hotel.db.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository {

    public int createBooking(int customerId, int roomId, LocalDate checkIn, LocalDate checkOut) {
        String sql = "INSERT INTO bookings(customer_id, room_id, check_in, check_out) VALUES (?, ?, ?, ?) RETURNING id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ps.setInt(2, roomId);
            ps.setDate(3, Date.valueOf(checkIn));
            ps.setDate(4, Date.valueOf(checkOut));

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("id");

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean isRoomAvailableForDates(int roomId, LocalDate checkIn, LocalDate checkOut) {
        String sql =
                "SELECT COUNT(*) FROM bookings " +
                        "WHERE room_id = ? " +
                        "AND check_out > CURRENT_DATE " +
                        "AND NOT (check_out <= ? OR check_in >= ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomId);
            ps.setDate(2, Date.valueOf(checkIn));
            ps.setDate(3, Date.valueOf(checkOut));

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) == 0;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Integer findActiveBookingIdByRoomId(int roomId) {
        String sql =
                "SELECT id FROM bookings " +
                        "WHERE room_id = ? AND check_out > CURRENT_DATE " +
                        "ORDER BY check_in DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
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
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<String> getAllBookingsDetails() {
        List<String> list = new ArrayList<>();

        String sql =
                "SELECT b.id, b.booking_date, b.check_in, b.check_out, " +
                        "c.name, c.email, r.room_number, r.room_type, r.price_per_night " +
                        "FROM bookings b " +
                        "JOIN customers c ON b.customer_id = c.id " +
                        "JOIN rooms r ON b.room_id = r.id " +
                        "ORDER BY b.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(
                        "Booking{id=" + rs.getInt("id") +
                                ", check_in=" + rs.getDate("check_in") +
                                ", check_out=" + rs.getDate("check_out") +
                                ", customer=" + rs.getString("name") +
                                ", room=" + rs.getInt("room_number") +
                                "}"
                );
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return list;
    }

    public long calculateNights(LocalDate checkIn, LocalDate checkOut) {
        return java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
    }
}