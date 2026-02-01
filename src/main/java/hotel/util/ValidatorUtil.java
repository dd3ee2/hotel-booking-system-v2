package hotel.util;

import java.time.LocalDate;

public class ValidatorUtil {

    private ValidatorUtil() {}

    public static void checkRoomNumber(int roomNumber) {
        if (roomNumber <= 0) {
            throw new IllegalArgumentException("Room number must be positive.");
        }
    }

    public static void checkName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is empty.");
        }
    }

    public static void checkEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email is invalid.");
        }
    }

    public static void checkDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Dates are required.");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Invalid dates.");
        }
    }
}