package hotel.ui;

import hotel.controller.BookingController;

import java.util.Scanner;

public class ConsoleUI {

    private final BookingController controller = new BookingController();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\n=== Hotel Booking System ===");
            System.out.println("1. Show all rooms");
            System.out.println("2. Book a room");
            System.out.println("3. Show available rooms");
            System.out.println("4. Cancel booking (free a room)");
            System.out.println("5. Show all bookings");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Please enter a number.");
                continue;
            }

            if (choice == 1) {
                controller.showAllRooms();

            } else if (choice == 2) {
                System.out.print("Enter room number: ");
                int roomNumber = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter customer name: ");
                String name = scanner.nextLine();

                System.out.print("Enter customer email: ");
                String email = scanner.nextLine();

                System.out.print("Enter nights: ");
                int nights = Integer.parseInt(scanner.nextLine());

                System.out.println(controller.bookRoom(roomNumber, name, email, nights));

            } else if (choice == 3) {
                controller.showAvailableRooms();

            } else if (choice == 4) {
                System.out.print("Enter room number to cancel: ");
                int roomNumber = Integer.parseInt(scanner.nextLine());
                System.out.println(controller.cancelBooking(roomNumber));

            } else if (choice == 5) {
                controller.showAllBookings();

            } else if (choice == 0) {
                System.out.println("Goodbye!");
                break;

            } else {
                System.out.println("Invalid option.");
            }
        }
    }
}