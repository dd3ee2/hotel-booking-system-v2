package hotel.repository;

import hotel.auth.Role;
import hotel.db.DatabaseConnection;
import hotel.util.LoggerUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

public class UserRepository implements IUserRepository {

    private static final Logger log = LoggerUtil.getLogger();

    @Override
    public Role login(String login, String password) {
        String sql = "SELECT role FROM users WHERE login = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, login);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String roleStr = rs.getString("role");
                return Role.valueOf(roleStr);
            }

            return null;

        } catch (Exception e) {
            log.warning("login error: " + e.getMessage());
            throw new RuntimeException("login failed");
        }
    }
}