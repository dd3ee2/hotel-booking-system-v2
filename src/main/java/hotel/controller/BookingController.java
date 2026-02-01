package hotel.controller;

import hotel.auth.AuthContext;
import hotel.auth.Role;
import hotel.entity.BookingFullInfo;
import hotel.entity.Room;
import hotel.factory.RepositoryFactory;
import hotel.repository.*;
import hotel.util.LoggerUtil;
import hotel.util.ValidatorUtil;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Logger;

public class BookingController {

    private static final Logger log = LoggerUtil.getLogger();

    private final IRoomRepository roomRepository = RepositoryFactory.createRoomRepository();
    private final IBookingRepository bookingRepository = RepositoryFactory.createBookingRepository();
    private final ICustomerRepository customerRepository = RepositoryFactory.createCustomerRepository();

    public List<Room> getAllRooms() {
        log.info("Get all rooms");
        return roomRepository.getAllRooms();
    }

    public List<Room> getAvailableRoomsForDates(LocalDate checkIn, LocalDate checkOut) {
        ValidatorUtil.checkDates(checkIn, checkOut);
        log.info("Get available rooms: " + checkIn + " -> " + checkOut);

        List<Room> rooms = roomRepository.getAvailableRoomsForDates(checkIn, checkOut);

        rooms.sort((a, b) -> Integer.compare(a.getRoomNumber(), b.getRoomNumber()));

        return rooms;
    }

    public List<String> getAllBookings() {
        checkRole(Role.MANAGER);
        log.info("Get all bookings");
        return bookingRepository.getAllBookingsDetails();
    }

    public BookingFullInfo getFullBookingInfo(int bookingId) {
        checkRole(Role.MANAGER);
        log.info("Get full booking info: " + bookingId);

        if (bookingId <= 0) throw new IllegalArgumentException("bookingId must be positive");

        return bookingRepository.getFullBookingInfo(bookingId);
    }

    public String bookRoom(int roomNumber, String name, String email,
                           LocalDate checkIn, LocalDate checkOut) {

        try {
            ValidatorUtil.checkRoomNumber(roomNumber);
            ValidatorUtil.checkName(name);
            ValidatorUtil.checkEmail(email);
            ValidatorUtil.checkDates(checkIn, checkOut);
        } catch (Exception e) {
            return e.getMessage();
        }

        Room room = roomRepository.findByRoomNumber(roomNumber);
        if (room == null) return "Room not found.";

        boolean available = bookingRepository.isRoomAvailableForDates(room.getId(), checkIn, checkOut);
        if (!available) {
            log.info("Booking failed: room not available. room=" + roomNumber);
            return "Room is not available.";
        }

        Integer customerId = customerRepository.findCustomerIdByEmail(email);
        if (customerId == null) {
            customerId = customerRepository.createCustomer(name, email);
            log.info("Customer created: " + email);
        }

        int bookingId = bookingRepository.createBooking(customerId, room.getId(), checkIn, checkOut);

        long nights = bookingRepository.calculateNights(checkIn, checkOut);
        double total = nights * room.getPricePerNight();

        log.info("Booking created: id=" + bookingId + ", room=" + roomNumber + ", total=" + total);
        return "Booked. ID=" + bookingId + ", total=" + total;
    }

    public String cancelBooking(int roomNumber) {
        checkRole(Role.ADMIN);

        ValidatorUtil.checkRoomNumber(roomNumber);

        Room room = roomRepository.findByRoomNumber(roomNumber);
        if (room == null) return "Room not found.";

        Integer bookingId = bookingRepository.findActiveBookingIdByRoomId(room.getId());
        if (bookingId == null) return "No active booking.";

        bookingRepository.deleteBookingById(bookingId);
        log.info("Booking cancelled: id=" + bookingId + ", room=" + roomNumber);

        return "Booking cancelled.";
    }

    public LocalDate parseDate(String value) {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            log.warning("Wrong date format: " + value);
            throw new IllegalArgumentException("Wrong date format");
        }
    }

    private void checkRole(Role minRole) {
        Role current = AuthContext.getInstance().getCurrentRole();

        boolean allowed = isAllowed(current, minRole);
        if (!allowed) {
            throw new IllegalStateException("Access denied for role: " + current);
        }
    }

    private boolean isAllowed(Role current, Role minRole) {
        if (minRole == Role.USER) return true;
        if (minRole == Role.MANAGER) return current == Role.MANAGER || current == Role.ADMIN;
        return current == Role.ADMIN;
    }
}