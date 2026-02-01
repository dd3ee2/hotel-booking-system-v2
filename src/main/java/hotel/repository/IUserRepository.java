package hotel.repository;

import hotel.auth.Role;

public interface IUserRepository {
    Role login(String login, String password);
}