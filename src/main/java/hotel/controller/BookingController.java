package hotel.controller;

import hotel.entity.Room;
import hotel.repository.BookingRepository;
import hotel.repository.CustomerRepository;
import hotel.repository.RoomRepository;

public class BookingController {

    private final RoomRepository roomRepository = new RoomRepository();
    private final BookingRepository bookingRepository = new BookingRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();

    public void showAllRooms() {
        roomRepository.getAllRooms().forEach(System.out::println);
    }

    public void showAvailableRooms() {
        roomRepository.getAvailableRooms().forEach(System.out::println);
    }

    public void showAllBookings() {
        bookingRepository.getAllBookingsDetails().forEach(System.out::println);
    }

    public String bookRoom(int roomNumber, String customerName, String customerEmail, int nights) {

        if (nights <= 0) {
            return "Nights must be greater than 0.";
        }

        Room room = roomRepository.findByRoomNumber(roomNumber);
        if (room == null) return "Room not found.";

        if (!room.isAvailable()) return "Room is already booked.";

        Integer customerId = customerRepository.findCustomerIdByEmail(customerEmail);
        if (customerId == null) {
            customerId = customerRepository.createCustomer(customerName, customerEmail);
        }

        int bookingId = bookingRepository.createBooking(customerId, room.getId());
        roomRepository.setAvailability(room.getId(), false);

        double total = nights * room.getPricePerNight();

        return "Room booked successfully! Booking ID = " + bookingId +
                "\n--- Invoice ---" +
                "\nRoom: " + room.getRoomNumber() + " (" + room.getRoomType() + ")" +
                "\nPrice per night: " + room.getPricePerNight() +
                "\nNights: " + nights +
                "\nTotal: " + total;
    }

    public String cancelBooking(int roomNumber) {
        Room room = roomRepository.findByRoomNumber(roomNumber);
        if (room == null) return "Room not found.";

        if (room.isAvailable()) {
            return "Room is already available (no active booking).";
        }

        Integer bookingId = bookingRepository.findLatestBookingIdByRoomId(room.getId());
        if (bookingId == null) {
            roomRepository.setAvailability(room.getId(), true);
            return "No booking record found, but room was set to available.";
        }

        bookingRepository.deleteBookingById(bookingId);
        roomRepository.setAvailability(room.getId(), true);

        return "Booking cancelled. Room is available again.";
    }
}