package hotel.controller;

import hotel.entity.Room;
import hotel.repository.BookingRepository;
import hotel.repository.CustomerRepository;
import hotel.repository.RoomRepository;

public class BookingController {

    private final RoomRepository roomRepository = new RoomRepository();
    private final BookingRepository bookingRepository = new BookingRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();

    public String bookRoom(int roomNumber, String customerName, String customerEmail) {

        Room room = roomRepository.findByRoomNumber(roomNumber);
        if (room == null) {
            return "Room not found.";
        }

        if (!room.isAvailable()) {
            return "Room is already booked.";
        }

        Integer customerId = customerRepository.findCustomerIdByEmail(customerEmail);
        if (customerId == null) {
            customerId = customerRepository.createCustomer(customerName, customerEmail);
        }

        bookingRepository.createBooking(customerId, room.getId());
        roomRepository.setAvailability(room.getId(), false);

        return "Room booked successfully!";
    }
}