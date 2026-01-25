package hotel.controller;

import hotel.entity.Room;
import hotel.repository.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class BookingController {

    private final IRoomRepository roomRepository = new RoomRepository();
    private final IBookingRepository bookingRepository = new BookingRepository();
    private final ICustomerRepository customerRepository = new CustomerRepository();

    public List<Room> showAllRooms() {
        return roomRepository.getAllRooms();
    }

    public List<Room> showAvailableRoomsForDates(LocalDate checkIn, LocalDate checkOut) {
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Invalid dates.");
        }
        return roomRepository.getAvailableRoomsForDates(checkIn, checkOut);
    }

    public List<String> showAllBookings() {
        return bookingRepository.getAllBookingsDetails();
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
        }

        int bookingId = bookingRepository.createBooking(customerId, room.getId(), checkIn, checkOut);

        long nights = bookingRepository.calculateNights(checkIn, checkOut);
        double total = nights * room.getPricePerNight();

        return "Booked. ID=" + bookingId + ", total=" + total;
    }

    public String cancelBooking(int roomNumber) {
        Room room = roomRepository.findByRoomNumber(roomNumber);
        if (room == null) return "Room not found.";

        Integer bookingId = bookingRepository.findActiveBookingIdByRoomId(room.getId());
        if (bookingId == null) return "No active booking.";

        bookingRepository.deleteBookingById(bookingId);
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