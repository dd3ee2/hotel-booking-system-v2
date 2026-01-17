package hotel.ui;

import hotel.controller.BookingController;
import hotel.repository.RoomRepository;

import java.util.Scanner;

public class ConsoleUI {

    private final RoomRepository roomRepository = new RoomRepository();
    private final BookingController bookingController = new BookingController();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\n=== Hotel Booking System ===");
            System.out.println("1. Show rooms");
            System.out.println("2. Book a room");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 1) {
                roomRepository.getAllRooms().forEach(System.out::println);

            } else if (choice == 2) {
                System.out.print("Enter room number: ");
                int roomNumber = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter customer name: ");
                String name = scanner.nextLine();

                System.out.print("Enter customer email: ");
                String email = scanner.nextLine();

                String result = bookingController.bookRoom(roomNumber, name, email);
                System.out.println(result);

            } else if (choice == 0) {
                System.out.println("Goodbye!");
                break;

            } else {
                System.out.println("Invalid option.");
            }
        }
    }
}