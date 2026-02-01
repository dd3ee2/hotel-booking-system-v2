package hotel.entity;

import java.time.LocalDate;

public class BookingFullInfo {
    private int bookingId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String customerName;
    private String customerEmail;
    private int roomNumber;
    private String categoryName;
    private double pricePerNight;

    public BookingFullInfo(int bookingId, LocalDate checkIn, LocalDate checkOut,
                           String customerName, String customerEmail,
                           int roomNumber, String categoryName, double pricePerNight) {
        this.bookingId = bookingId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.roomNumber = roomNumber;
        this.categoryName = categoryName;
        this.pricePerNight = pricePerNight;
    }

    public int getBookingId() {
        return bookingId;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    @Override
    public String toString() {
        return "BookingFullInfo{" +
                "bookingId=" + bookingId +
                ", customer='" + customerName + " (" + customerEmail + ")'" +
                ", roomNumber=" + roomNumber +
                ", category='" + categoryName + '\'' +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                ", pricePerNight=" + pricePerNight +
                '}';
    }
}