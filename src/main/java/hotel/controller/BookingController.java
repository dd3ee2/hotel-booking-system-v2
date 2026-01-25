package hotel.controller;

import hotel.entity.Room;
import hotel.repository.*;
import hotel.util.LoggerUtil;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

public class BookingController {

    private static final Logger log = LoggerUtil.getLogger();

    private final IRoomRepository roomRepository = new RoomRepository();
    private final IBookingRepository bookingRepository = new BookingRepository();
    private final ICustomerRepository customerRepository = new CustomerRepository();

    public void showAllRooms() {
        roomRepository.getAllRooms().forEach(System.out::println);
    }

    public void showAvailableRoomsForDates(LocalDate checkIn, LocalDate checkOut) {
        if (!checkOut.isAfter(checkIn)) {
            System.out.println("Invalid dates.");
            return;
        }
        roomRepository.getAvailableRoomsForDates(checkIn, checkOut).forEach(System.out::println);
    }

    public void showAllBookings() {
        bookingRepository.getAllBookingsDetails().forEach(System.out::println);
    }

    public String bookRoom(int roomNumber, String name, String email,
                           LocalDate checkIn, LocalDate checkOut) {

        if (roomNumber <= 0) return "Room number must be positive.";
        if (name == null || name.trim().isEmpty()) return "Name is empty.";
        if (email == null || !email.contains("@")) return "Email is invalid.";
        if (!checkOut.isAfter(checkIn)) return "Invalid dates.";

        Room room = roomRepository.findByRoomNumber(roomNumber);
        if (room == null) return "Room not found.";

        if (!bookingRepository.isRoomAvailableForDates(room.getId(), checkIn, checkOut)) {
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

        log.info("Booking created. id=" + bookingId + ", room=" + roomNumber);

        return "Booked. ID=" + bookingId + ", total=" + total;
    }

    public String cancelBooking(int roomNumber) {
        Room room = roomRepository.findByRoomNumber(roomNumber);
        if (room == null) return "Room not found.";

        Integer bookingId = bookingRepository.findActiveBookingIdByRoomId(room.getId());
        if (bookingId == null) return "No active booking.";

        bookingRepository.deleteBookingById(bookingId);
        log.info("Booking cancelled. id=" + bookingId + ", room=" + roomNumber);

        return "Booking cancelled.";
    }

    public LocalDate parseDate(String value) {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Wrong date format");
        }
    }
}