package hotel.repository;

import hotel.entity.BookingFullInfo;

import java.time.LocalDate;
import java.util.List;

public interface IBookingRepository {
    int createBooking(int customerId, int roomId, LocalDate checkIn, LocalDate checkOut);
    boolean isRoomAvailableForDates(int roomId, LocalDate checkIn, LocalDate checkOut);
    Integer findActiveBookingIdByRoomId(int roomId);
    void deleteBookingById(int bookingId);
    List<String> getAllBookingsDetails();
    long calculateNights(LocalDate checkIn, LocalDate checkOut);

    BookingFullInfo getFullBookingInfo(int bookingId);
}