package hotel.repository;

import hotel.db.DatabaseConnection;
import hotel.entity.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoomRepository {

    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT id, room_number, room_type, price_per_night, is_available FROM rooms ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("id"),
                        rs.getInt("room_number"),
                        rs.getString("room_type"),
                        rs.getDouble("price_per_night"),
                        rs.getBoolean("is_available")
                );
                rooms.add(room);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error loading rooms: " + e.getMessage(), e);
        }

        return rooms;
    }

    public List<Room> getAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT id, room_number, room_type, price_per_night, is_available " +
                "FROM rooms WHERE is_available = true ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("id"),
                        rs.getInt("room_number"),
                        rs.getString("room_type"),
                        rs.getDouble("price_per_night"),
                        rs.getBoolean("is_available")
                );
                rooms.add(room);
            }

        } catch (Exception e) {
            throw new RuntimeException("getAvailableRooms failed: " + e.getMessage(), e);
        }

        return rooms;
    }

    public Room findByRoomNumber(int roomNumber) {
        String sql = "SELECT id, room_number, room_type, price_per_night, is_available FROM rooms WHERE room_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Room(
                            rs.getInt("id"),
                            rs.getInt("room_number"),
                            rs.getString("room_type"),
                            rs.getDouble("price_per_night"),
                            rs.getBoolean("is_available")
                    );
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("findByRoomNumber failed: " + e.getMessage(), e);
        }

        return null;
    }

    public void setAvailability(int roomId, boolean available) {
        String sql = "UPDATE rooms SET is_available = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, available);
            ps.setInt(2, roomId);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("setAvailability failed: " + e.getMessage(), e);
        }
    }
}